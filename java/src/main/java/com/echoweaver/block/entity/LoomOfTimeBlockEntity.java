package com.echoweaver.block.entity;

import com.echoweaver.block.ModBlocks;
import com.echoweaver.echo.EchoRecording;
import com.echoweaver.echo.RecordingManager;
import com.echoweaver.entity.EchoEntity;
import com.echoweaver.entity.ModEntities;
import com.echoweaver.item.ModItems;
import com.echoweaver.sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class LoomOfTimeBlockEntity extends BlockEntity implements Inventory, NamedScreenHandlerFactory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private boolean recording = false;

    public LoomOfTimeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.LOOM_OF_TIME_ENTITY, pos, state);
    }

    public boolean isRecording() {
        return recording;
    }

    public void toggleRecording(ServerPlayerEntity player) {
        if (recording) {
            stopRecording(player);
        } else {
            startRecording(player);
        }
    }

    private void startRecording(ServerPlayerEntity player) {
        ItemStack thread = getStack(0);
        if (!thread.isOf(ModItems.THREAD_OF_RESONANCE)) return;

        thread.decrement(1);
        RecordingManager.startRecording(player);
        recording = true;
        markDirty();
        world.playSound(null, pos, ModSounds.LOOM_WEAVE, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }

    private void stopRecording(ServerPlayerEntity player) {
        EchoRecording echoRecording = RecordingManager.stopRecording(player);
        recording = false;
        markDirty();

        if (echoRecording != null && !echoRecording.isEmpty() && hasCrystal()) {
            consumeCrystal();
            spawnEcho(player, echoRecording);
        }
    }

    private boolean hasCrystal() {
        return getStack(1).isOf(ModItems.SOURCE_CRYSTAL);
    }

    private void consumeCrystal() {
        getStack(1).decrement(1);
    }

    private void spawnEcho(ServerPlayerEntity player, EchoRecording echoRecording) {
        if (!(world instanceof ServerWorld serverWorld)) return;

        EchoEntity echo = new EchoEntity(ModEntities.ECHO, serverWorld);
        echo.setPosition(player.getX(), player.getY(), player.getZ());
        echo.setRecording(echoRecording);
        serverWorld.spawnEntity(echo);
        world.playSound(null, pos, ModSounds.ECHO_ACTIVATE, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        Inventories.writeNbt(nbt, inventory, registries);
        nbt.putBoolean("Recording", recording);
        super.writeNbt(nbt, registries);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        Inventories.readNbt(nbt, inventory, registries);
        recording = nbt.getBoolean("Recording");
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return pos.isWithinDistance(player.getBlockPos(), 4.5);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.echoweaver.loom_of_time");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return null;
    }
}

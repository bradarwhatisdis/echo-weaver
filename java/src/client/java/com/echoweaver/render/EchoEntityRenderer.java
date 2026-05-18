package com.echoweaver.render;

import com.echoweaver.EchoWeaver;
import com.echoweaver.entity.EchoEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;

public class EchoEntityRenderer extends EntityRenderer<EchoEntity> {
    private static final Identifier TEXTURE = Identifier.of(EchoWeaver.MOD_ID, "textures/entity/echo.png");

    public EchoEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(EchoEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(EchoEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        matrices.push();
        matrices.translate(0.0, 0.5, 0.0);
        matrices.multiply(dispatcher.getRotation());

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(getTexture(entity)));
        int alpha = (int) (128 * MathHelper.sin((entity.age + tickDelta) * 0.05f) * 0.5f + 0.5f + 64);
        int color = entity.getDataTracker().get(com.echoweaver.entity.EchoEntity.ECHO_COLOR);

        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        float size = 0.4f;
        matrices.scale(size, size, size);

        MatrixStack.Entry entry = matrices.peek();
        drawBillboard(entry, vertexConsumer, light, r, g, b, alpha);

        matrices.pop();
    }

    private void drawBillboard(MatrixStack.Entry entry, VertexConsumer consumer,
                                int light, float r, float g, float b, int alpha) {
        float half = 0.5f;
        float z = 0.0f;

        consumer.vertex(entry, -half, -half, z).color(r, g, b, alpha).texture(0.0f, 1.0f).light(light);
        consumer.vertex(entry, -half, half, z).color(r, g, b, alpha).texture(0.0f, 0.0f).light(light);
        consumer.vertex(entry, half, half, z).color(r, g, b, alpha).texture(1.0f, 0.0f).light(light);
        consumer.vertex(entry, half, -half, z).color(r, g, b, alpha).texture(1.0f, 1.0f).light(light);
    }
}

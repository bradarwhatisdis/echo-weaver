package com.echoweaver.render;

import com.echoweaver.EchoWeaver;
import com.echoweaver.entity.EchoEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class EchoEntityRenderer extends EntityRenderer<EchoEntity, EntityRenderState> {
    private static final Identifier TEXTURE = Identifier.of(EchoWeaver.MOD_ID, "textures/entity/echo.png");

    public EchoEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public Identifier getTexture(EntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public void render(EntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(state, matrices, vertexConsumers, light);
    }
}

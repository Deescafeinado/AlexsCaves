package com.github.alexmodguy.alexscaves.client.particle;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.ClientProxy;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexthe666.citadel.client.shader.PostEffectRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ProtonParticle extends MagneticOrbitParticle {

    private static final ResourceLocation CENTER_TEXTURE = new ResourceLocation(AlexsCaves.MODID, "textures/particle/proton_core.png");
    private static final ResourceLocation PROTON_TRAIL_TEXTURE = new ResourceLocation(AlexsCaves.MODID, "textures/particle/teletor_trail.png");

    protected ProtonParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.orbitAxis = random.nextInt(3);
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.orbitOffset = new Vec3(0, 0, 0);
        this.orbitDistance = 1;
        this.orbitSpeed = 4;
        this.alpha = 1;
        this.hasPhysics = false;
        this.lifetime = 30 + this.random.nextInt(20);
    }

    public int getLightColor(float partialTicks) {
        return 240;
    }

    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        PostEffectRegistry.renderEffectForNextTick(ClientProxy.IRRADIATED_SHADER);
        Vec3 vec3 = camera.getPosition();
        float f = (float) (Mth.lerp((double) partialTick, this.xo, this.x) - vec3.x());
        float f1 = (float) (Mth.lerp((double) partialTick, this.yo, this.y) - vec3.y());
        float f2 = (float) (Mth.lerp((double) partialTick, this.zo, this.z) - vec3.z());
        Quaternionf quaternion;
        if (this.roll == 0.0F) {
            quaternion = camera.rotation();
        } else {
            quaternion = new Quaternionf(camera.rotation());
            float f3 = Mth.lerp(partialTick, this.oRoll, this.roll);
            quaternion.mul(Axis.ZP.rotation(f3));
        }
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer portalStatic = multibuffersource$buffersource.getBuffer(ACRenderTypes.getRadiationGlow(CENTER_TEXTURE));
        PoseStack posestack = new PoseStack();
        PoseStack.Pose posestack$pose = posestack.last();
        Matrix3f matrix3f = posestack$pose.normal();

        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.rotate(quaternion);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = 0.3F;

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }
        float f7 = 0;
        float f8 = 1;
        float f5 = 0;
        float f6 = 1;
        float alpha = 1;
        int j = 240;
        portalStatic.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).color(this.rCol, this.gCol, this.bCol, alpha).uv(f8, f6).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        portalStatic.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).color(this.rCol, this.gCol, this.bCol, alpha).uv(f8, f5).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        portalStatic.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).color(this.rCol, this.gCol, this.bCol, alpha).uv(f7, f5).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        portalStatic.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).color(this.rCol, this.gCol, this.bCol, alpha).uv(f7, f6).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();

        multibuffersource$buffersource.endBatch();
        super.render(vertexConsumer, camera, partialTick);
    }

    public void tick() {
        this.xd *= 0.9;
        this.yd *= 0.9;
        this.zd *= 0.9;
        super.tick();
        this.trailA = 0.8F * Mth.clamp(age / (float) this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public Vec3 getOrbitPosition(float angle) {
        Vec3 center = new Vec3(orbitX, orbitY, orbitZ);
        float f = reverseOrbit ? -1 : 1;
        Vec3 add = Vec3.ZERO;
        float rot = angle * 3 * orbitSpeed * (float) (Math.PI / 180F);
        switch (orbitAxis) {
            case 0:
                add = new Vec3(0, orbitDistance * 0.5F, orbitDistance * 0.5F).xRot(rot * f);
                break;
            case 1:
                add = new Vec3(orbitDistance * 0.5F, 0, orbitDistance * 0.5F).yRot(rot * f);
                break;
            case 2:
                add = new Vec3(orbitDistance * 0.5F, orbitDistance * 0.5F, 0).zRot(rot * f);
                break;
        }
        return center.add(add);
    }

    @Override
    public float getTrailHeight() {
        return 0.2F;
    }

    @Override
    public RenderType getTrailRenderType() {
        return ACRenderTypes.getParticleTrail(PROTON_TRAIL_TEXTURE);
    }


    @Override
    public int sampleCount() {
        return Math.min(10, lifetime - age);
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ProtonParticle particle = new ProtonParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.trailR = 0F;
            particle.trailG = 1.0F;
            particle.trailB = 0F;
            return particle;
        }
    }
}

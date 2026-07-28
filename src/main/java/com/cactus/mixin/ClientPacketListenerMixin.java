package com.cactus.mixin;

import com.cactus.CustomRenderPipeline;
import com.cactus.LogoutSpot;
import com.cactus.LogoutSpots;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

    @Inject(
            method = "handlePlayerInfoRemove",
            at = @At("HEAD")
    )
    private void onHandlePlayerInfoRemove(ClientboundPlayerInfoRemovePacket packet, CallbackInfo ci) {
        if(LogoutSpots.isEnabled) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;


            for (var profileId : packet.profileIds()) {

                Player player = mc.level.getPlayerByUUID(profileId);

                if (player != null) {
                    String name = player.getScoreboardName();
                    float x = (float) player.getX();
                    float y = (float) player.getY();
                    float z = (float) player.getZ();

                    Vec3 logoutCoords = new Vec3(x, y, z);
                    CustomRenderPipeline.getInstance().addSpot(new LogoutSpot(name, logoutCoords));

                    if (player == mc.player) continue;

                    if (mc.player != null) {
                        Component message = Component.literal(String.format("Player %s logged out at X: %.1f Y: %.1f Z: %.1f", name, x, y, z));


                        mc.execute(() -> {
                            if (mc.player != null) {
                                mc.player.displayClientMessage(message, false);
                            }
                        });
                    }
                }
            }
        } else return;


    }
}

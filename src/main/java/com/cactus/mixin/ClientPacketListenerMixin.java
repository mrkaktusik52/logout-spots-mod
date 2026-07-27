package com.cactus.mixin;

import com.cactus.LogoutSpots;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.world.entity.player.Player;
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
                    int x = (int) player.getX();
                    int y = (int) player.getY();
                    int z = (int) player.getZ();

                    if (player == mc.player) continue;

                    if (mc.player != null) {
                        Component message = Component.literal("Player " + name + " logged out at X: " + x + " Y: " + y + " Z: " + z);

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

package com.cactus;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogoutSpots implements ModInitializer {
	public static final String MOD_ID = "logoutspots";
	public static boolean isEnabled = true;
	public static KeyMapping toggleKeyBinding;
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		toggleKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.cactus.toggle",
				GLFW.GLFW_KEY_X,
				KeyMapping.CATEGORY_MISC
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (toggleKeyBinding.consumeClick()) {
				isEnabled = !isEnabled;

				if (client.player != null) {

					Component status = isEnabled ? Component.translatable("lgtospt.stat.on") : Component.translatable("lgtospt.stat.off");
					client.player.displayClientMessage(
							Component.literal("Logout Spots ").append(status),
							true
					);
				}
			}
		});
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}

package com.dinc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class NoCrystals implements ModInitializer {
	public static final String MOD_ID = "no-crystals";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public class ModSounds {
        public static final String MOD_ID = "no-crystals";

        public static final Identifier VANISH_ID = Identifier.fromNamespaceAndPath(MOD_ID, "vanish");
    
        public static final SoundEvent VANISH = Registry.register(
            BuiltInRegistries.SOUND_EVENT, 
            VANISH_ID,
            SoundEvent.createVariableRangeEvent(VANISH_ID)
        );

        public static void registerSounds() {}
    }

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        ModSounds.registerSounds();

		ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                
                boolean itemRemoved = false;

                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);

                    if (!stack.isEmpty() && stack.is(Items.ECHO_SHARD)) {
                        
                        player.getInventory().setItem(i, ItemStack.EMPTY);

                        itemRemoved = true;
                    }
                }

                if (itemRemoved) {
                    player.containerMenu.broadcastChanges();

                    player.level().playSound(
                            null, 
                            player.getX(), player.getY(), player.getZ(), 
                            ModSounds.VANISH,
                            SoundSource.PLAYERS,
                            1.0F,
                            1.0F
                    );
                }
            }
        });
	}
}
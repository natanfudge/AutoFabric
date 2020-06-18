package net.fabricmc.example

import fudge.Entrypoint
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer

@Entrypoint(Entrypoint.MAIN, Entrypoint.CLIENT)
class KotlinExampleMod : ModInitializer, ClientModInitializer {
    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        println("Kotlin Entrypoint!")
    }

    override fun onInitializeClient() {
        println("Client Kotlin Entrypoint!")
    }

    companion object {
        @Entrypoint(Entrypoint.MAIN)
        @JvmStatic
        val companionField = KotlinExampleMod()
    }
}


package net.fabricmc.example

import fudge.Entrypoint
import net.fabricmc.api.ModInitializer

@Entrypoint(Entrypoint.MAIN)
class KotlinExampleMod : ModInitializer {
    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        println("Kotlin Entrypoint!")
    }
}
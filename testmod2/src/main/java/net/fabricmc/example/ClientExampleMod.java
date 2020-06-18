package net.fabricmc.example;

import fudge.Entrypoint;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

@Entrypoint(Entrypoint.CLIENT)
public class ClientExampleMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("Client Entrypoint!");
    }

}

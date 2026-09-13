// Goes in: src/main/java/com/acentra/pmotools/PmoToolsClient.java
package com.acentra.pmotools;

import com.acentra.pmotools.gui.PunishTypesScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;

public class PmoToolsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // "/pmo" is a client-only command — never sent to the server.
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(
                ClientCommandManager.literal("pmo")
                    .executes(context -> {
                        MinecraftClient client = MinecraftClient.getInstance();
                        client.execute(() -> client.setScreen(new PunishTypesScreen()));
                        return 1;
                    })
            )
        );

        // PunishLogger: observe every command you actually send and log the
        // ones that match our list. Always returns true (never blocks).
        ClientSendMessageEvents.ALLOW_COMMAND.register(command -> {
            PunishLogger.maybeLog("/" + command);
            return true;
        });
    }
}

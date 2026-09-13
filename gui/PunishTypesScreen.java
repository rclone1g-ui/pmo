// Goes in: src/main/java/com/acentra/pmotools/gui/PunishTypesScreen.java
package com.acentra.pmotools.gui;

import com.acentra.pmotools.PunishmentConfig;
import com.acentra.pmotools.PunishmentType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;

public class PunishTypesScreen extends Screen {

    public PunishTypesScreen() {
        super(Text.of("Punishment Reasons"));
    }

    @Override
    protected void init() {
        List<PunishmentType> types = PunishmentConfig.load();
        int y = 30;
        int rowHeight = 24;
        int buttonWidth = 160;

        for (PunishmentType type : types) {
            this.addDrawableChild(
                ButtonWidget.builder(Text.of(type.name), btn -> openType(type))
                    .dimensions(10, y, buttonWidth, 20)
                    .build()
            );
            y += rowHeight;
        }

        this.addDrawableChild(
            ButtonWidget.builder(Text.of("+ Add New Type"), btn ->
                    MinecraftClient.getInstance().setScreen(new AddTypeScreen()))
                .dimensions(10, y + 6, buttonWidth, 20)
                .build()
        );

        this.addDrawableChild(
            ButtonWidget.builder(Text.of("Close"), btn -> this.close())
                .dimensions(10, y + 32, buttonWidth, 20)
                .build()
        );
    }

    private void openType(PunishmentType type) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (type.requiresReason && !type.reasons.isEmpty()) {
            client.setScreen(new ReasonsScreen(type));
        } else if (type.requiresDuration && !type.durations.isEmpty()) {
            client.setScreen(new DurationScreen(type, null));
        } else if (type.requiresPlayer) {
            client.setScreen(new NameInputScreen(type, null, null));
        } else {
            // No player/reason/duration needed at all — just build and send.
            client.setScreen(new net.minecraft.client.gui.screen.ChatScreen(
                com.acentra.pmotools.CommandBuilder.build(type, "", null, "")));
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawText(this.textRenderer, this.title, 10, 12, 0xFFFFFF, true);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}

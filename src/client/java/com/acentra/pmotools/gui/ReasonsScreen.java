// Goes in: src/main/java/com/acentra/pmotools/gui/ReasonsScreen.java
package com.acentra.pmotools.gui;

import com.acentra.pmotools.PunishmentConfig;
import com.acentra.pmotools.PunishmentType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class ReasonsScreen extends Screen {
    private final PunishmentType type;
    private TextFieldWidget newReasonField;

    public ReasonsScreen(PunishmentType type) {
        super(Text.of(type.name + " — Reason"));
        this.type = type;
    }

    @Override
    protected void init() {
        int y = 30;
        for (String reason : type.reasons) {
            this.addDrawableChild(
                ButtonWidget.builder(Text.of(reason), btn -> next(reason))
                    .dimensions(10, y, 200, 20)
                    .build()
            );
            y += 24;
        }

        newReasonField = new TextFieldWidget(this.textRenderer, 10, y + 8, 200, 20, Text.of("New reason"));
        this.addDrawableChild(newReasonField);
        this.addDrawableChild(
            ButtonWidget.builder(Text.of("Add + Use"), btn -> {
                String reason = newReasonField.getText().trim();
                if (!reason.isEmpty()) {
                    PunishmentConfig.addReason(type.name, reason);
                    next(reason);
                }
            }).dimensions(10, y + 32, 200, 20).build()
        );

        this.addDrawableChild(
            ButtonWidget.builder(Text.of("Back"), btn ->
                    MinecraftClient.getInstance().setScreen(new PunishTypesScreen()))
                .dimensions(10, y + 58, 200, 20)
                .build()
        );
    }

    private void next(String reason) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (type.requiresDuration && !type.durations.isEmpty()) {
            client.setScreen(new DurationScreen(type, reason));
        } else if (type.requiresPlayer) {
            client.setScreen(new NameInputScreen(type, null, reason));
        } else {
            client.setScreen(new net.minecraft.client.gui.screen.ChatScreen(
                com.acentra.pmotools.CommandBuilder.build(type, "", null, reason)));
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

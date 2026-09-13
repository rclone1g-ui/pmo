// Goes in: src/main/java/com/acentra/pmotools/gui/NameInputScreen.java
package com.acentra.pmotools.gui;

import com.acentra.pmotools.CommandBuilder;
import com.acentra.pmotools.PunishmentType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class NameInputScreen extends Screen {
    private final PunishmentType type;
    private final String duration; // nullable
    private final String reason;   // nullable
    private TextFieldWidget nameField;

    public NameInputScreen(PunishmentType type, String duration, String reason) {
        super(Text.of("Enter Player Name"));
        this.type = type;
        this.duration = duration;
        this.reason = reason;
    }

    @Override
    protected void init() {
        int fieldWidth = 200;
        int x = (this.width - fieldWidth) / 2;
        int y = this.height / 2 - 10;

        nameField = new TextFieldWidget(this.textRenderer, x, y, fieldWidth, 20, Text.of("Player name"));
        nameField.setMaxLength(32);
        this.addDrawableChild(nameField);
        this.setInitialFocus(nameField);

        this.addDrawableChild(
            ButtonWidget.builder(Text.of("Confirm"), btn -> confirm())
                .dimensions(x, y + 26, 96, 20)
                .build()
        );
        this.addDrawableChild(
            ButtonWidget.builder(Text.of("Cancel"), btn -> this.close())
                .dimensions(x + fieldWidth - 96, y + 26, 96, 20)
                .build()
        );
    }

    // NOTE: keyPressed's exact signature can differ across 1.21.x builds
    // (some moved to a KeyInput-object parameter). If this specific
    // override fails to compile, check what Screen.keyPressed expects in
    // your version — the confirm()/close() logic doesn't need to change.
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 || keyCode == 335) { // Enter / numpad Enter
            confirm();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void confirm() {
        String name = nameField.getText().trim();
        MinecraftClient client = MinecraftClient.getInstance();
        if (name.isEmpty()) {
            this.close();
            return;
        }
        String finalCommand = CommandBuilder.build(type, name, duration, reason);
        this.close();
        client.setScreen(new ChatScreen(finalCommand));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(
            this.textRenderer, "Enter player name for " + type.name,
            this.width / 2, this.height / 2 - 30, 0xFFFFFF
        );
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}

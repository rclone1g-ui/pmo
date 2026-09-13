// Goes in: src/main/java/com/acentra/pmotools/gui/DurationScreen.java
package com.acentra.pmotools.gui;

import com.acentra.pmotools.PunishmentType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class DurationScreen extends Screen {
    private final PunishmentType type;
    private final String reason; // may be null if type doesn't use reasons
    private TextFieldWidget customField;

    public DurationScreen(PunishmentType type, String reason) {
        super(Text.of(type.name + " — Duration"));
        this.type = type;
        this.reason = reason;
    }

    @Override
    protected void init() {
        int y = 30;
        int x = 10;
        int col = 0;
        for (String duration : type.durations) {
            this.addDrawableChild(
                ButtonWidget.builder(Text.of(duration), btn -> next(duration))
                    .dimensions(x + col * 70, y, 64, 20)
                    .build()
            );
            col++;
            if (col >= 3) { col = 0; y += 24; }
        }
        if (col != 0) y += 24;

        // Fully custom duration — e.g. "45m", "2w", whatever your server's
        // command accepts. This is what makes it "completely customizable"
        // rather than locked to only the preset buttons.
        customField = new TextFieldWidget(this.textRenderer, x, y + 10, 150, 20, Text.of("Custom (e.g. 45m)"));
        this.addDrawableChild(customField);
        this.addDrawableChild(
            ButtonWidget.builder(Text.of("Use"), btn -> {
                String custom = customField.getText().trim();
                if (!custom.isEmpty()) next(custom);
            }).dimensions(x + 154, y + 10, 50, 20).build()
        );
    }

    private void next(String duration) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (type.requiresPlayer) {
            client.setScreen(new NameInputScreen(type, duration, reason));
        } else {
            client.setScreen(new net.minecraft.client.gui.screen.ChatScreen(
                com.acentra.pmotools.CommandBuilder.build(type, "", duration, reason)));
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

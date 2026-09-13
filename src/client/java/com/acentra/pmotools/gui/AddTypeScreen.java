// Goes in: src/main/java/com/acentra/pmotools/gui/AddTypeScreen.java
package com.acentra.pmotools.gui;

import com.acentra.pmotools.PunishmentConfig;
import com.acentra.pmotools.PunishmentType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class AddTypeScreen extends Screen {
    private TextFieldWidget nameField;
    private TextFieldWidget templateField;
    private TextFieldWidget reasonsField;
    private TextFieldWidget durationsField;
    private boolean needsDuration = true;
    private boolean needsReason = true;
    private ButtonWidget durationToggle;
    private ButtonWidget reasonToggle;

    public AddTypeScreen() {
        super(Text.of("Add New Punishment Type"));
    }

    @Override
    protected void init() {
        int x = 10, y = 30, w = 260;

        nameField = new TextFieldWidget(this.textRenderer, x, y, w, 20, Text.of("Name (e.g. Freeze)"));
        this.addDrawableChild(nameField);
        y += 26;

        templateField = new TextFieldWidget(this.textRenderer, x, y, w, 20,
            Text.of("Command, e.g. /freeze {player} {duration} {reason}"));
        this.addDrawableChild(templateField);
        y += 26;

        reasonsField = new TextFieldWidget(this.textRenderer, x, y, w, 20,
            Text.of("Reasons, comma separated (optional)"));
        this.addDrawableChild(reasonsField);
        y += 26;

        durationsField = new TextFieldWidget(this.textRenderer, x, y, w, 20,
            Text.of("Durations, comma separated (optional)"));
        this.addDrawableChild(durationsField);
        y += 30;

        reasonToggle = ButtonWidget.builder(Text.of("Needs Reason: Yes"), btn -> {
            needsReason = !needsReason;
            btn.setMessage(Text.of("Needs Reason: " + (needsReason ? "Yes" : "No")));
        }).dimensions(x, y, 128, 20).build();
        this.addDrawableChild(reasonToggle);

        durationToggle = ButtonWidget.builder(Text.of("Needs Duration: Yes"), btn -> {
            needsDuration = !needsDuration;
            btn.setMessage(Text.of("Needs Duration: " + (needsDuration ? "Yes" : "No")));
        }).dimensions(x + 132, y, 138, 20).build();
        this.addDrawableChild(durationToggle);
        y += 28;

        this.addDrawableChild(
            ButtonWidget.builder(Text.of("Save"), btn -> save())
                .dimensions(x, y, 126, 20).build()
        );
        this.addDrawableChild(
            ButtonWidget.builder(Text.of("Cancel"), btn ->
                    MinecraftClient.getInstance().setScreen(new PunishTypesScreen()))
                .dimensions(x + 134, y, 126, 20).build()
        );
    }

    private void save() {
        String name = nameField.getText().trim();
        String template = templateField.getText().trim();
        if (name.isEmpty() || template.isEmpty() || !template.contains("{player}")) {
            return; // silently ignore invalid input; could add an on-screen error label later
        }

        ArrayList<String> reasons = new ArrayList<>();
        for (String r : reasonsField.getText().split(",")) {
            String trimmed = r.trim();
            if (!trimmed.isEmpty()) reasons.add(trimmed);
        }
        ArrayList<String> durations = new ArrayList<>();
        for (String d : durationsField.getText().split(",")) {
            String trimmed = d.trim();
            if (!trimmed.isEmpty()) durations.add(trimmed);
        }

        PunishmentType type = new PunishmentType(name, template, true, needsReason, needsDuration, reasons, durations);
        PunishmentConfig.addType(type);
        MinecraftClient.getInstance().setScreen(new PunishTypesScreen());
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

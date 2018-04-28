package com.chattriggers.ctjs.utils;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.event.WorldChangeEvent;
import com.chattriggers.ctjs.Reference;
import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import com.chattriggers.ctjs.minecraft.libs.FileLib;
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.minecraft.objects.message.Message;
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent;
import com.chattriggers.ctjs.minecraft.wrappers.World;
import com.chattriggers.ctjs.utils.config.Config;
import lombok.Getter;

public class UpdateChecker {
    @Getter
    private static UpdateChecker instance;

    private boolean worldLoaded;

    private boolean updateAvailable;
    private boolean warned;

    public UpdateChecker() {
        instance = this;
        EventBus.INSTANCE.register(this);

        this.worldLoaded = false;

        getUpdate();
        this.warned = !Config.getInstance().getShowUpdatesInChat().value;
    }

    private void getUpdate() {
        String fileName = "ctjs-" + Reference.MODVERSION + ".jar";
        String get = FileLib.getUrlContent("https://www.chattriggers.com/versions/latest/").trim();
        this.updateAvailable = !fileName.equals(get);
    }

    @InvokeEvent
    public void worldLoad(WorldChangeEvent event) {
        this.worldLoaded = true;
    }

    @InvokeEvent
    public void renderOverlay(RenderHUDEvent event) {
        if (!this.worldLoaded) return;
        this.worldLoaded = false;

        if (!this.updateAvailable || this.warned) return;

        World.playSound("note.bass", 1000, 1);
        new Message(
                "&c&m" + ChatLib.getChatBreak("-"),
                "\n",
                "&cChatTrigger requires an update to work properly!",
                "\n",
                new TextComponent("&a[Download]").setClick("open_url", "https://www.chattriggers.com/#download"),
                " ",
                new TextComponent("&e[Changelog]").setClick("open_url", "https://github.com/ChatTriggers/ct.js/releases"),
                "\n",
                "&c&m" + ChatLib.getChatBreak("-")
        ).chat();

        this.warned = true;
    }

    public void drawUpdateMessage() {
        if (!this.updateAvailable) return;

        Renderer.drawString("&cChatTrigger requires an update to work properly!", 2, 2);
    }
}

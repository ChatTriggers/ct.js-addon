package com.chattriggers.ctjs.commands;

import cc.hyperium.commands.BaseCommand;
import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import com.chattriggers.ctjs.triggers.OnTrigger;
import com.chattriggers.ctjs.utils.console.Console;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Command implements BaseCommand {
    private String name;
    private String usage;
    @Setter
    private List<String> tabComplete;
    @Getter
    private ArrayList<OnTrigger> triggers = new ArrayList<>();

    public Command(OnTrigger trigger, String name, String usage) {
        this.triggers.add(trigger);
        this.name = name;
        this.usage = usage;
    }

    public void addTabComplete(String option) {
        this.tabComplete.add(option);
    }

    public String getName() {
        return getCommandName();
    }
    public String getCommandName() {
        return name;
    }

    public String getUsage() {
        return usage;
    }

    public void onExecute(String[] args) {
        try {
            for (OnTrigger trigger : triggers)
                trigger.trigger((Object[]) args);
        } catch (Exception exception) {
            ChatLib.chat("&cSomething went wrong while running that command");
            ChatLib.chat("&cCheck the ct console for more information");
            Console.getInstance().printStackTrace(exception);
        }
    }
}

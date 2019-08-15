package ru.mdashlw.rankedwho.command.impl;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import ru.mdashlw.rankedwho.RankedWho;
import ru.mdashlw.rankedwho.command.Command;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RankedWhoCommand extends Command {
    private static final Map<String, String> COMMANDS = new LinkedHashMap<>();

    public RankedWhoCommand(RankedWho mod) {
        super(mod);

        COMMANDS.put("/rankedwho help", "Prints this message.");
        COMMANDS.put("/rankedwho toggle", "Turn the mod on/off.");
        COMMANDS.put("/rankedwho autoWho", "Turn the auto /who option on/off.");
        COMMANDS.put("/rankedwho <api key>", "Change the Hypixel API key.");
        COMMANDS.put("/rating", "Get your current rating.");
        COMMANDS.put("/rating <player>", "Get current rating of the player.");
    }

    @Override
    public String getName() {
        return "rankedwho";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("rw");
    }

    @Override
    public void execute(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            replyHelp(sender);
            return;
        }

        String option = args[0];

        if (option.equalsIgnoreCase("help") || option.equalsIgnoreCase("h")) {
            replyHelp(sender);
        } else if (option.equalsIgnoreCase("toggle") || option.equalsIgnoreCase("t")) {
            mod.setToggled(!mod.isToggled());
            mod.saveConfiguration();

            IChatComponent message = new ChatComponentText("RankedWho: ")
                    .setChatStyle(new ChatStyle()
                            .setColor(EnumChatFormatting.GRAY));

            if (mod.isToggled()) {
                message.appendSibling(new ChatComponentText("Enabled")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.GREEN)));
            } else {
                message.appendSibling(new ChatComponentText("Disabled")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.RED)));
            }

            sender.addChatMessage(message);
        } else if (option.equalsIgnoreCase("autowho")) {
            mod.setAutoWho(!mod.isAutoWho());
            mod.saveConfiguration();

            IChatComponent message = new ChatComponentText("[RankedWho] ")
                    .setChatStyle(new ChatStyle()
                            .setColor(EnumChatFormatting.AQUA))
                    .appendSibling(new ChatComponentText("AutoWho: ")
                            .setChatStyle(new ChatStyle()
                                    .setColor(EnumChatFormatting.GRAY)));

            if (mod.isAutoWho()) {
                message.appendSibling(new ChatComponentText("Enabled")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.GREEN)));
            } else {
                message.appendSibling(new ChatComponentText("Disabled")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.RED)));
            }

            sender.addChatMessage(message);
        } else {
            if (option.split("-").length != 5) {
                replyHelp(sender);
                return;
            }

            mod.setApiKey(option);
            mod.saveConfiguration();

            sender.addChatMessage(new ChatComponentText("[RankedWho] ")
                    .setChatStyle(new ChatStyle()
                            .setColor(EnumChatFormatting.AQUA))
                    .appendSibling(new ChatComponentText("Hypixel API key has been changed.")
                            .setChatStyle(new ChatStyle()
                                    .setColor(EnumChatFormatting.GREEN))));
        }
    }

    private void replyHelp(ICommandSender sender) {
        IChatComponent message = new ChatComponentText("-----------------------------------------------------\n")
                .setChatStyle(new ChatStyle()
                        .setColor(EnumChatFormatting.GOLD))
                .appendSibling(new ChatComponentText("RankedWho Commands:\n")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.GREEN)));

        COMMANDS.forEach((name, description) -> message.appendSibling(new ChatComponentText(name)
                .setChatStyle(new ChatStyle()
                        .setColor(EnumChatFormatting.YELLOW)))
                .appendSibling(new ChatComponentText(" - " + description + '\n')
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.AQUA))));

        message.appendSibling(new ChatComponentText("-----------------------------------------------------")
                .setChatStyle(new ChatStyle()
                        .setColor(EnumChatFormatting.GOLD)));

        sender.addChatMessage(message);
    }
}

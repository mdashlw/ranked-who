package ru.mdashlw.rankedwho.command.impl;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import ru.mdashlw.hypixel.ranked.exceptions.HypixelRankedApiException;
import ru.mdashlw.rankedwho.RankedWho;
import ru.mdashlw.rankedwho.command.Command;
import ru.mdashlw.rankedwho.util.HypixelUtil;

public class RatingCommand extends Command {
    public RatingCommand(RankedWho mod) {
        super(mod);
    }

    @Override
    public String getName() {
        return "rating";
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void execute(ICommandSender sender, String[] args) {
        String name;

        if (args.length == 0) {
            name = sender.getName();
        } else {
            name = args[0];
        }

        mod.getHypixelAPI().retrievePlayerByName(name).thenAccept(player -> {
            if (player == null) {
                sender.addChatMessage(new ChatComponentText("Can't find a player by the name of '" + name + '\'')
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.RED)));
                return;
            }

            String uuid = player.getUuid();

            mod.getHypixelRankedAPI().retrievePlayer(uuid).whenComplete((rankedPlayer, exception) -> {
                if (exception instanceof HypixelRankedApiException) {
                    sender.addChatMessage(new ChatComponentText("Unable to reach website.")
                            .setChatStyle(new ChatStyle()
                                    .setColor(EnumChatFormatting.RED)));
                    return;
                }

                int rating = rankedPlayer.getRating();
                int position = rankedPlayer.getPosition();

                sender.addChatMessage(new ChatComponentText("Rating of ")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.WHITE))
                        .appendText(HypixelUtil.getFormattedDisplayname(player))
                        .appendSibling(new ChatComponentText(": ")
                                .setChatStyle(new ChatStyle()
                                        .setColor(EnumChatFormatting.WHITE)))
                        .appendSibling(new ChatComponentText(Integer.toString(rating))
                                .setChatStyle(new ChatStyle()
                                        .setColor(EnumChatFormatting.YELLOW)))
                        .appendSibling(new ChatComponentText(" #" + position)
                                .setChatStyle(new ChatStyle()
                                        .setColor(EnumChatFormatting.AQUA))));
            });
        });
    }
}

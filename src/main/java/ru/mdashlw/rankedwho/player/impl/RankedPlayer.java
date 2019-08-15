package ru.mdashlw.rankedwho.player.impl;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import ru.mdashlw.rankedwho.player.Player;

public class RankedPlayer implements Player {
    private String name;
    private int rating;
    private int level;
    private String kit;

    public RankedPlayer(String name, int rating, int level, String kit) {
        this.name = name;
        this.rating = rating;
        this.level = level;
        this.kit = kit;
    }

    @Override
    public IChatComponent getMessage(int team) {
        return new ChatComponentText("Team #" + team + ": ")
                .setChatStyle(new ChatStyle()
                        .setColor(EnumChatFormatting.WHITE))
                .appendText(name + ' ')
                .appendSibling(new ChatComponentText(Integer.toString(rating) + ' ')
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.YELLOW)))
                .appendSibling(new ChatComponentText(Integer.toString(level) + ' ')
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.GREEN)))
                .appendSibling(new ChatComponentText(kit)
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.RED)));
    }
}

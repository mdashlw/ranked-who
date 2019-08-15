package ru.mdashlw.rankedwho.player.impl;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import ru.mdashlw.rankedwho.player.Player;

public class NickedPlayer implements Player {
    private String name;

    public NickedPlayer(String name) {
        this.name = name;
    }

    @Override
    public IChatComponent getMessage(int team) {
        return new ChatComponentText("Team #" + team + ": ")
                .setChatStyle(new ChatStyle()
                        .setColor(EnumChatFormatting.WHITE))
                .appendSibling(new ChatComponentText(name)
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.GRAY)))
                .appendSibling(new ChatComponentText(" NICKED")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.RED)));
    }
}

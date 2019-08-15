package ru.mdashlw.rankedwho.listeners;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.mdashlw.rankedwho.RankedWho;
import ru.mdashlw.rankedwho.events.PlayerConnectedToServerEvent;

public class KeyChecker {
    private RankedWho mod;

    public KeyChecker(RankedWho mod) {
        this.mod = mod;
    }

    @SubscribeEvent
    public void onPlayerConnectedToServer(PlayerConnectedToServerEvent event) {
        if (!mod.getApiKey().isEmpty() || !mod.isOnHypixel()) {
            return;
        }

        EntityPlayerSP player = event.getPlayer();
        IChatComponent message = new ChatComponentText("-----------------------------------------------------\n")
                .setChatStyle(new ChatStyle()
                        .setColor(EnumChatFormatting.RED))
                .appendSibling(new ChatComponentText("[RankedWho] ")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.AQUA)))
                .appendSibling(new ChatComponentText("Hypixel API key is not set.\n\nSet it up with ")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.WHITE)))
                .appendSibling(new ChatComponentText("/rankedwho <your API key>")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.GREEN)))
                .appendSibling(new ChatComponentText(".\nUse ")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.WHITE)))
                .appendSibling(new ChatComponentText("/api new ")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.RED)))
                .appendSibling(new ChatComponentText("to obtain your key.\n")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.WHITE)))
                .appendSibling(new ChatComponentText("-----------------------------------------------------")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.RED)));

        player.addChatMessage(message);
    }
}

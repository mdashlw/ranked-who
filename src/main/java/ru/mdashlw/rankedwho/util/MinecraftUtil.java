package ru.mdashlw.rankedwho.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.EnumChatFormatting;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Set;
import java.util.stream.Collectors;

public class MinecraftUtil {
    public static Set<String> getPlayers() {
        return Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap().stream()
                .map(player -> player.getGameProfile().getName())
                .filter(name -> !name.startsWith("§"))
                .collect(Collectors.toSet());
    }

    public static boolean isBot(EntityPlayer player) {
        return !getPlayers().contains(player.getName());
    }

    public static String getServerAddress(NetworkManager manager) {
        SocketAddress remoteAddress = manager.getRemoteAddress();

        if (!(remoteAddress instanceof InetSocketAddress)) {
            return null;
        }

        return ((InetSocketAddress) remoteAddress).getHostName();
    }

    public static String getChatColor(String name) {
        return EnumChatFormatting.valueOf(name).toString();
    }
}

package ru.mdashlw.rankedwho.util;

import ru.mdashlw.hypixel.entities.Rank;
import ru.mdashlw.hypixel.entities.player.HypixelPlayer;

public class HypixelUtil {
    public static String getFormattedDisplayname(HypixelPlayer player) {
        String displayname = player.getDisplayname();
        String prefix = player.getPrefix();

        if (prefix != null) {
            return prefix + ' ' + displayname;
        }

        Rank rank = player.getRank();
        String rankPlusColor = MinecraftUtil.getChatColor(player.getRankPlusColor());
        String monthlyRankColor = MinecraftUtil.getChatColor(player.getMonthlyRankColor());
        String formattedRank = getFormattedRank(rank, rankPlusColor, monthlyRankColor);

        return formattedRank + displayname;
    }

    public static String getFormattedRank(Rank rank, String rankPlusColor, String monthlyRankColor) {
        String formattedRank;

        switch (rank) {
            case NORMAL:
                formattedRank = "§7";
                break;
            case VIP:
                formattedRank = "§a[VIP] ";
                break;
            case VIP_PLUS:
                formattedRank = "§a[VIP§c+§a] ";
                break;
            case MVP:
                formattedRank = "§b[MVP] ";
                break;
            case MVP_PLUS:
                formattedRank = "§b[MVP" + rankPlusColor + "+§b] ";
                break;
            case SUPERSTAR:
                formattedRank = monthlyRankColor + "[MVP" + rankPlusColor + "++" + monthlyRankColor + "] ";
                break;
            case YOUTUBER:
                formattedRank = "§c[§fYOUTUBE§c] ";
                break;
            case HELPER:
                formattedRank = "§9[HELPER] ";
                break;
            case MODERATOR:
                formattedRank = "§2[MODERATOR] ";
                break;
            case ADMIN:
                formattedRank = "§c[ADMIN] ";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + rank);
        }

        return formattedRank;
    }
}

package ru.mdashlw.rankedwho.updater;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.kohsuke.github.*;
import ru.mdashlw.rankedwho.RankedWho;
import ru.mdashlw.rankedwho.base.VersionInfo;
import ru.mdashlw.rankedwho.events.PlayerConnectedToServerEvent;

import java.io.IOException;

public class Updater {
    private static final String USERNAME = "mdashlw";
    private static final String REPOSITORY = "ranked-who";

    private IChatComponent message;

    public void checkForUpdates() {
        String currentVersion = RankedWho.class.getPackage().getImplementationVersion();
        VersionInfo latestVersion = retrieveLatestVersion();

        System.out.println("[RankedWho] Current version: " + currentVersion);

        if (latestVersion == null) {
            System.out.println("[RankedWho] Unable to check for updates.");
            return;
        }

        String tag = latestVersion.getTag();
        String downloadUrl = latestVersion.getDownloadUrl();

        if (tag.equals(currentVersion)) {
            System.out.println("[RankedWho] The mod is up to date.");
            return;
        }

        message = new ChatComponentText("-----------------------------------------------------\n")
                .setChatStyle(new ChatStyle()
                        .setColor(EnumChatFormatting.BLUE))
                .appendSibling(new ChatComponentText("RankedWho ")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.GOLD)))
                .appendSibling(new ChatComponentText("is out of date.\nYour version: ")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.WHITE)))
                .appendSibling(new ChatComponentText(currentVersion)
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.RED)))
                .appendSibling(new ChatComponentText(" | ")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.GRAY)))
                .appendSibling(new ChatComponentText("Latest version: ")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.WHITE)))
                .appendSibling(new ChatComponentText(tag)
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.GREEN)))
                .appendSibling(new ChatComponentText("\n\nDownload link:\n")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.GOLD)
                                .setBold(true)))
                .appendSibling(new ChatComponentText(downloadUrl)
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.AQUA)
                                .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click to open!")))
                                .setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, downloadUrl))))
                .appendSibling(new ChatComponentText("\n-----------------------------------------------------")
                        .setChatStyle(new ChatStyle()
                                .setColor(EnumChatFormatting.BLUE)));
        MinecraftForge.EVENT_BUS.register(this);
    }

    private VersionInfo retrieveLatestVersion() {
        try {
            GitHub gitHub = GitHub.connectAnonymously();
            GHUser user = gitHub.getUser(USERNAME);
            GHRepository repository = user.getRepository(REPOSITORY);
            GHRelease latestRelease = repository.getLatestRelease();

            if (latestRelease == null) {
                return null;
            }

            String tag = latestRelease.getTagName();
            String downloadUrl = null;

            for (GHAsset asset : latestRelease.getAssets()) {
                if (asset.getName().endsWith(".jar")) {
                    downloadUrl = asset.getBrowserDownloadUrl();
                }
            }

            return new VersionInfo(tag, downloadUrl);
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @SubscribeEvent
    public void onPlayerConnectedToServer(PlayerConnectedToServerEvent event) {
        if (message == null) {
            return;
        }

        event.getPlayer().addChatMessage(message);
    }
}

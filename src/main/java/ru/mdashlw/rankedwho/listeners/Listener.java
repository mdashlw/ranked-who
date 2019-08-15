package ru.mdashlw.rankedwho.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.mdashlw.rankedwho.RankedWho;
import ru.mdashlw.rankedwho.events.PlayerJoinedRankedEvent;
import ru.mdashlw.rankedwho.events.PlayerJoinedWorldEvent;
import ru.mdashlw.rankedwho.player.Player;
import ru.mdashlw.rankedwho.util.ChatUtil;
import ru.mdashlw.rankedwho.util.MinecraftUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Listener {
    private static final String GAME_START = "Teaming is not allowed on Ranked mode!";
    private static final Pattern WHO_PLAYER_PATTERN = Pattern.compile("Team #(\\d+): (\\w{1,16})");

    private RankedWho mod;

    public Listener(RankedWho mod) {
        this.mod = mod;
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        IChatComponent message = event.message;
        String text = message.getUnformattedText();

        if (!mod.isInRanked()) {
            return;
        }

        if (text.equals(GAME_START)) {
            if (mod.isAutoWho()) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/who");
            }

            event.setCanceled(true);
            return;
        }

        if (!mod.isToggled()) {
            return;
        }

        Matcher matcher = WHO_PLAYER_PATTERN.matcher(text);

        if (!matcher.matches()) {
            return;
        }

        event.setCanceled(true);

        int team = Integer.parseInt(matcher.group(1));
        String name = matcher.group(2);
        int messageId = ThreadLocalRandom.current().nextInt(1500) + team;

        ChatUtil.addChatMessageWithId(messageId, message);

        CompletableFuture<Player> future = mod.getPlayers().computeIfAbsent(name, mod::retrievePlayer);

        future.thenAccept(player -> ChatUtil.editMessage(messageId, player.getMessage(team)));
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        mod.getPlayers().clear();
    }

    @SubscribeEvent
    public void onPlayerJoinedRanked(PlayerJoinedRankedEvent event) {
        MinecraftUtil.getPlayers().forEach(player -> mod.getPlayers().put(player, mod.retrievePlayer(player)));
    }

    @SubscribeEvent
    public void onPlayerJoinedWorld(PlayerJoinedWorldEvent event) {
        if (!mod.isInRanked()) {
            return;
        }

        String player = event.getPlayer().getName();

        if (!mod.getPlayers().containsKey(player)) {
            mod.getPlayers().put(player, mod.retrievePlayer(player));
        }
    }
}

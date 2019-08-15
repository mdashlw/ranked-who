package ru.mdashlw.rankedwho.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import ru.mdashlw.rankedwho.RankedWho;
import ru.mdashlw.rankedwho.base.LocationRaw;
import ru.mdashlw.rankedwho.events.PlayerJoinedRankedEvent;
import ru.mdashlw.rankedwho.events.SelfPlayerJoinedWorldEvent;
import ru.mdashlw.rankedwho.util.MinecraftUtil;

import java.io.IOException;

public class LocationObserver {
    private static final String HYPIXEL_IP = "hypixel.net";
    private static final String WHEREAMI_COMMAND = "/whereami";
    private static final String LOCRAW_COMMAND = "/locraw";
    private static final String SKYWARS_TYPE = "SKYWARS";
    private static final String RANKED_MODE = "ranked_normal";
    private static final String WHEREAMI_RESPONSE = "You are currently connected to server ";
    private static final String WHEREAMI_LIMBO_RESPONSE = "You are currently in limbo";

    private RankedWho mod;

    private boolean waitingForWhereami;
    private boolean waitingForLocraw;

    public LocationObserver(RankedWho mod) {
        this.mod = mod;
    }

    @SubscribeEvent
    public void onSelfPlayerJoinedWorld(SelfPlayerJoinedWorldEvent event) {
        if (!mod.isOnHypixel() || waitingForWhereami || waitingForLocraw) {
            return;
        }

        EntityPlayerSP player = event.getPlayer();

        waitingForWhereami = true;
        player.sendChatMessage(WHEREAMI_COMMAND);
    }

    @SubscribeEvent
    public void onClientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        NetworkManager manager = event.manager;
        String address = MinecraftUtil.getServerAddress(manager);

        if (address == null) {
            return;
        }

        mod.setOnHypixel(address.contains(HYPIXEL_IP));
    }

    @SubscribeEvent
    public void onClientDisconnectionFromServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        mod.setOnHypixel(false);
        mod.setInRanked(false);
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();

        if (waitingForWhereami && message.startsWith(WHEREAMI_RESPONSE)) {
            waitingForWhereami = false;
            event.setCanceled(true);

            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

            waitingForLocraw = true;
            player.sendChatMessage(LOCRAW_COMMAND);
        } else if (waitingForWhereami && message.equals(WHEREAMI_LIMBO_RESPONSE)) {
            waitingForWhereami = false;
            event.setCanceled(true);
            mod.setInRanked(false);
        } else if (waitingForLocraw && message.startsWith("{") && message.endsWith("}")) {
            event.setCanceled(true);

            LocationRaw location;

            try {
                location = mod.getObjectMapper().readValue(message, LocationRaw.class);
            } catch (IOException e) {
                e.printStackTrace();
                waitingForLocraw = false;
                return;
            }

            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            String server = location.getServer();
            String gameType = location.getGameType();
            String mode = location.getMode();

            if (server != null && server.equals("limbo")) {
                player.sendChatMessage(LOCRAW_COMMAND);
                return;
            }

            waitingForLocraw = false;
            mod.setInRanked(gameType != null && mode != null && gameType.equals(SKYWARS_TYPE) && mode.equals(RANKED_MODE));

            if (mod.isInRanked()) {
                MinecraftForge.EVENT_BUS.post(new PlayerJoinedRankedEvent());
            }
        }
    }
}

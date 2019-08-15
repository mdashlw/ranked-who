package ru.mdashlw.rankedwho.events;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SelfPlayerJoinedWorldEvent extends Event {
    private EntityPlayerSP player;

    public SelfPlayerJoinedWorldEvent(EntityPlayerSP player) {
        this.player = player;
    }

    public EntityPlayerSP getPlayer() {
        return player;
    }
}

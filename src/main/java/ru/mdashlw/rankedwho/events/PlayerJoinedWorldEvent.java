package ru.mdashlw.rankedwho.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerJoinedWorldEvent extends Event {
    private EntityPlayer player;

    public PlayerJoinedWorldEvent(EntityPlayer player) {
        this.player = player;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}

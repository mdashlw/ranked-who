package ru.mdashlw.rankedwho.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import ru.mdashlw.rankedwho.events.PlayerConnectedToServerEvent;
import ru.mdashlw.rankedwho.events.PlayerJoinedWorldEvent;
import ru.mdashlw.rankedwho.events.SelfPlayerJoinedWorldEvent;
import ru.mdashlw.rankedwho.util.MinecraftUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventDispatcher {
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private long lastWorldLoadEvent;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (System.currentTimeMillis() - lastWorldLoadEvent < 5000) {
            lastWorldLoadEvent = System.currentTimeMillis();
            return;
        }

        lastWorldLoadEvent = System.currentTimeMillis();

        World world = event.world;
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.thePlayer;

        if (world.isRemote) {
            world.addWorldAccess(new WorldAccess());
        }

        if (player == null) {
            executor.submit(() -> {
                while (minecraft.thePlayer == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }

                MinecraftForge.EVENT_BUS.post(new SelfPlayerJoinedWorldEvent(minecraft.thePlayer));
            });
        } else {
            MinecraftForge.EVENT_BUS.post(new SelfPlayerJoinedWorldEvent(player));
        }
    }

    @SubscribeEvent
    public void onClientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.thePlayer;

        if (player == null) {
            executor.submit(() -> {
                while (minecraft.thePlayer == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }

                MinecraftForge.EVENT_BUS.post(new PlayerConnectedToServerEvent(minecraft.thePlayer));
            });
        } else {
            MinecraftForge.EVENT_BUS.post(new PlayerConnectedToServerEvent(player));
        }
    }

    public static class WorldAccess implements IWorldAccess {
        @Override
        public void markBlockForUpdate(BlockPos pos) {
        }

        @Override
        public void notifyLightSet(BlockPos pos) {
        }

        @Override
        public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
        }

        @Override
        public void playSound(String soundName, double x, double y, double z, float volume, float pitch) {
        }

        @Override
        public void playSoundToNearExcept(EntityPlayer except, String soundName, double x, double y, double z, float volume, float pitch) {
        }

        @Override
        public void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... parameters) {
        }

        @Override
        public void onEntityAdded(Entity entityIn) {
            if (!(entityIn instanceof EntityPlayer)) {
                return;
            }

            EntityPlayer player = (EntityPlayer) entityIn;

            if (MinecraftUtil.isBot(player)) {
                return;
            }

            MinecraftForge.EVENT_BUS.post(new PlayerJoinedWorldEvent(player));
        }

        @Override
        public void onEntityRemoved(Entity entityIn) {
        }

        @Override
        public void playRecord(String recordName, BlockPos blockPosIn) {
        }

        @Override
        public void broadcastSound(int soundID, BlockPos pos, int data) {
        }

        @Override
        public void playAuxSFX(EntityPlayer player, int sfxType, BlockPos blockPosIn, int data) {
        }

        @Override
        public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
        }
    }
}

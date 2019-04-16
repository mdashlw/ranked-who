package ru.mdashlw.rankedwho.eventdispatcher

import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.BlockPos
import net.minecraft.world.IWorldAccess
import net.minecraftforge.common.MinecraftForge
import ru.mdashlw.rankedwho.events.PlayerJoinWorldEvent
import ru.mdashlw.rankedwho.util.isBot

class CustomWorldAccess : IWorldAccess {
    override fun onEntityAdded(entityIn: Entity) {
        if (entityIn !is EntityPlayer || entityIn.isBot) {
            return
        }

        MinecraftForge.EVENT_BUS.post(PlayerJoinWorldEvent(entityIn))
    }

    override fun broadcastSound(soundID: Int, pos: BlockPos?, data: Int) = Unit

    override fun playSoundToNearExcept(
        except: EntityPlayer?,
        soundName: String?,
        x: Double,
        y: Double,
        z: Double,
        volume: Float,
        pitch: Float
    ) = Unit

    override fun markBlockRangeForRenderUpdate(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int) = Unit

    override fun playAuxSFX(player: EntityPlayer?, sfxType: Int, blockPosIn: BlockPos?, data: Int) = Unit

    override fun onEntityRemoved(entityIn: Entity) = Unit

    override fun notifyLightSet(pos: BlockPos?) = Unit

    override fun spawnParticle(
        particleID: Int,
        ignoreRange: Boolean,
        xCoord: Double,
        yCoord: Double,
        zCoord: Double,
        xOffset: Double,
        yOffset: Double,
        zOffset: Double,
        vararg parameters: Int
    ) = Unit

    override fun playSound(soundName: String?, x: Double, y: Double, z: Double, volume: Float, pitch: Float) = Unit

    override fun playRecord(recordName: String?, blockPosIn: BlockPos?) = Unit

    override fun markBlockForUpdate(pos: BlockPos?) = Unit

    override fun sendBlockBreakProgress(breakerId: Int, pos: BlockPos?, progress: Int) = Unit
}

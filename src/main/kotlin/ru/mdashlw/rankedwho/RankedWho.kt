package ru.mdashlw.rankedwho

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.asCoroutineDispatcher
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import ru.mdashlw.hypixel.api.HypixelApi
import ru.mdashlw.rankedwho.command.impl.RankedWhoCommand
import ru.mdashlw.rankedwho.command.impl.RatingCommand
import ru.mdashlw.rankedwho.configuration.ConfigurationManager
import ru.mdashlw.rankedwho.eventdispatcher.EventDispatcher
import ru.mdashlw.rankedwho.events.PlayerJoinRankedEvent
import ru.mdashlw.rankedwho.keychecker.HypixelKeyChecker
import ru.mdashlw.rankedwho.locationobserver.LocationObserver
import ru.mdashlw.rankedwho.nickedwinnerreveal.NickedWinnerReveal
import ru.mdashlw.rankedwho.updater.Updater
import ru.mdashlw.rankedwho.util.isEmpty
import java.io.File
import java.util.concurrent.Executors

@Mod(
    modid = "rankedwho",
    name = "RankedWho",
    version = "3.0.0",
    clientSideOnly = true,
    acceptedMinecraftVersions = "[1.8.9]"
)
class RankedWho {
    @Mod.EventHandler
    fun FMLPreInitializationEvent.onPreInitialization() {
        setupConfiguration(suggestedConfigurationFile)
    }

    @Mod.EventHandler
    fun FMLInitializationEvent.onInitialization() {
        setupHypixelApi()
        registerListeners()
        registerCommands()
        initUpdater()
    }

    private fun setupConfiguration(file: File) {
        ConfigurationManager.run {
            registerDefaultAdapters()
            registerMainConfiguration()
        }

        Configuration(file).run {
            load()
            configuration = this

            if (isEmpty()) {
                ConfigurationManager.save()
            }

            ConfigurationManager.load()
        }
    }

    private fun setupHypixelApi() {
        HypixelApi.outputMode = HypixelApi.OutputMode.COLORIZED
    }

    private fun registerListeners() {
        MinecraftForge.EVENT_BUS.run {
            register(LocationObserver)
            register(EventDispatcher)
            register(NickedWinnerReveal)
            register(HypixelKeyChecker)
            register(Listener)
        }
    }

    private fun registerCommands() {
        RankedWhoCommand.register()
        RatingCommand.register()
    }

    private fun initUpdater() {
        Updater.init()
    }

    companion object {
        val jackson = jacksonObjectMapper()

        lateinit var configuration: Configuration

        val pool = Executors.newFixedThreadPool(6).asCoroutineDispatcher()
        val waitPool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        val singlePool = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

        var onHypixel: Boolean = false
        var inRanked: Boolean = false
            set(value) {
                field = value

                if (value) {
                    MinecraftForge.EVENT_BUS.post(PlayerJoinRankedEvent())
                }
            }
    }
}

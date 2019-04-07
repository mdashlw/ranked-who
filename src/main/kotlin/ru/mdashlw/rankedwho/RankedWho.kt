package ru.mdashlw.rankedwho

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import okhttp3.OkHttpClient
import ru.mdashlw.hypixel.HypixelApi
import ru.mdashlw.rankedwho.evdis.EventDispatcher
import ru.mdashlw.rankedwho.events.PlayerJoinRankedEvent
import ru.mdashlw.rankedwho.locobv.LocationObserver
import ru.mdashlw.rankedwho.reference.API_KEY
import ru.mdashlw.rankedwho.reference.RATING_POSITION_PATTERN
import ru.mdashlw.rankedwho.updater.Updater
import ru.mdashlw.rankedwho.util.newCall
import ru.mdashlw.util.matchresult.get
import java.util.concurrent.Executors

@JvmField
val OBJECT_MAPPER: ObjectMapper = jacksonObjectMapper()
@JvmField
val OK_HTTP_CLIENT: OkHttpClient = OkHttpClient()

@Mod(
    modid = "rankedwho",
    name = "RankedWho",
    version = "1.0.0",
    clientSideOnly = true,
    acceptedMinecraftVersions = "[1.8.9]"
)
class RankedWho {
    companion object {
        private val POOL = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        @JvmField
        val WAITING_POOL = Executors.newFixedThreadPool(4).asCoroutineDispatcher()

        val jobs: HashMap<String, Deferred<String>> = HashMap()
        var team: Int = 1

        var isOnHypixel: Boolean = false
        var isRanked: Boolean = false
            set(value) {
                field = value

                if (value) {
                    MinecraftForge.EVENT_BUS.post(PlayerJoinRankedEvent())
                }
            }

        fun add(name: String) {
            jobs += wrap(name)
        }

        private fun wrap(name: String): Pair<String, Deferred<String>> = name to retrieveInformationAsync(name)

        fun retrieveInformationAsync(name: String): Deferred<String> =
            GlobalScope.async(POOL) {
                retrieveInformation(name)
            }

        private fun retrieveInformation(name: String): String {
            val player = HypixelApi.getPlayerByName(name)
                ?: return "Team #{team}: §7$name §cNICKED"

            val rating = getRatingPosition(player.displayname).first
            val level = player.level.toInt()
            val kit = player.stats.SkyWars.activeRankedKit.localizedName

            return "Team #{team}: ${player.formattedDisplayname} §e$rating §f[§a$level§f] §f(§c$kit§f)"
        }

        private fun getRatingPosition(player: String): Pair<String, String> {
            val content = OK_HTTP_CLIENT.newCall("https://hypixel.net/player/$player")
                ?: return "0" to "0"

            val match = RATING_POSITION_PATTERN.find(content)
                ?: return "0" to "0"

            val rating = match[1]
            val position = match[2]

            return rating to position
        }
    }

    @Mod.EventHandler
    fun FMLInitializationEvent.onInitialization() {
        setupHypixelAPI()
        registerListeners()
        initUpdater()
    }

    private fun setupHypixelAPI() {
        HypixelApi.apiKey = API_KEY
        HypixelApi.mode = HypixelApi.Mode.COLORIZED
    }

    private fun registerListeners() {
        MinecraftForge.EVENT_BUS.register(LocationObserver)
        MinecraftForge.EVENT_BUS.register(EventDispatcher)
        MinecraftForge.EVENT_BUS.register(Listener)
    }

    private fun initUpdater() {
        Updater.init()
    }
}

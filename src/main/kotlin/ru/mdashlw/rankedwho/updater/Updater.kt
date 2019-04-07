package ru.mdashlw.rankedwho.updater

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.util.IChatComponent
import net.minecraftforge.common.MinecraftForge
import org.kohsuke.github.GitHub
import ru.mdashlw.rankedwho.RankedWho
import ru.mdashlw.rankedwho.util.toChatComponent

object Updater {
    private const val USERNAME = "mdashlw"
    private const val REPOSITORY = "ranked-who"

    private lateinit var currentVersion: String
    private lateinit var latestVersion: String

    val message: IChatComponent by lazy {
        buildString {
            appendln("§9§m--------------------------------------------------")
            appendln("§6RankedWho §fis out of date.")
            appendln("§fYour version: §c$currentVersion §7| §fLatest version: §a$latestVersion")
            appendln()
            appendln("§6§lDownload from GitHub")
            appendln("§bhttps://github.com/$USERNAME/$REPOSITORY")
            append("§9§m--------------------------------------------------")
        }.toChatComponent()
    }

    fun init() {
        GlobalScope.launch {
            currentVersion = RankedWho::class.java.`package`.implementationVersion
            latestVersion = retrieveLatestVersion()
                ?: throw IllegalStateException("Unable to check for updates")

            if (currentVersion == latestVersion) {
                println("RankedWho is up to date.")
                return@launch
            }

            println("RankedWho is out of date, latest version: $latestVersion.")

            MinecraftForge.EVENT_BUS.register(Listener)
        }
    }

    private fun retrieveLatestVersion(): String? {
        val ghUser = GitHub.connectAnonymously().getUser(USERNAME)
        val ghRepository = ghUser.getRepository(REPOSITORY)
        val ghRelease = ghRepository.latestRelease

        return ghRelease?.tagName
    }
}

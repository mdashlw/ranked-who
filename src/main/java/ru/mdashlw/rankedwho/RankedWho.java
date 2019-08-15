package ru.mdashlw.rankedwho;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ru.mdashlw.hypixel.HypixelAPI;
import ru.mdashlw.hypixel.entities.player.Stats;
import ru.mdashlw.hypixel.entities.player.stats.SkyWars;
import ru.mdashlw.hypixel.ranked.HypixelRankedAPI;
import ru.mdashlw.hypixel.util.LevelingUtil;
import ru.mdashlw.rankedwho.command.impl.RankedWhoCommand;
import ru.mdashlw.rankedwho.command.impl.RatingCommand;
import ru.mdashlw.rankedwho.listeners.EventDispatcher;
import ru.mdashlw.rankedwho.listeners.KeyChecker;
import ru.mdashlw.rankedwho.listeners.Listener;
import ru.mdashlw.rankedwho.listeners.LocationObserver;
import ru.mdashlw.rankedwho.player.Player;
import ru.mdashlw.rankedwho.player.impl.NickedPlayer;
import ru.mdashlw.rankedwho.player.impl.RankedPlayer;
import ru.mdashlw.rankedwho.updater.Updater;
import ru.mdashlw.rankedwho.util.HypixelUtil;

import java.io.File;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mod(
        modid = "rankedwho",
        name = "RankedWho",
        version = "4.0.0",
        clientSideOnly = true,
        acceptedMinecraftVersions = "[1.8.9]"
)
public class RankedWho {
    private ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private ExecutorService commandHandler = Executors.newSingleThreadExecutor();
    private Map<String, CompletableFuture<Player>> players = new ConcurrentHashMap<>();
    private HypixelAPI hypixelAPI;
    private HypixelRankedAPI hypixelRankedAPI;
    private Configuration configuration;
    private boolean onHypixel;
    private boolean inRanked;
    private boolean toggled;
    private boolean autoWho;
    private String apiKey;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        File suggestedConfigurationFile = event.getSuggestedConfigurationFile();

        configuration = new Configuration(suggestedConfigurationFile);

        loadConfiguration();
        registerListeners();
        registerCommands();

        hypixelAPI = new HypixelAPI(apiKey);
        hypixelRankedAPI = new HypixelRankedAPI();
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        checkForUpdates();
    }

    private void updateConfiguration(boolean load) {
        Property property = configuration.get(Configuration.CATEGORY_GENERAL, "toggled", true);

        if (load) {
            toggled = property.getBoolean();
        } else {
            property.set(toggled);
        }

        property = configuration.get(Configuration.CATEGORY_GENERAL, "autoWho", true);

        if (load) {
            autoWho = property.getBoolean();
        } else {
            property.set(autoWho);
        }

        property = configuration.get(Configuration.CATEGORY_GENERAL, "apiKey", "");

        if (load) {
            apiKey = property.getString();
        } else {
            property.set(apiKey);
        }
    }

    private void loadConfiguration() {
        updateConfiguration(true);
    }

    public void saveConfiguration() {
        updateConfiguration(false);
        configuration.save();
    }

    private void registerListeners() {
        MinecraftForge.EVENT_BUS.register(new EventDispatcher());
        MinecraftForge.EVENT_BUS.register(new LocationObserver(this));
        MinecraftForge.EVENT_BUS.register(new KeyChecker(this));
        MinecraftForge.EVENT_BUS.register(new Listener(this));
    }

    private void registerCommands() {
        new RankedWhoCommand(this).register();
        new RatingCommand(this).register();
    }

    private void checkForUpdates() {
        new Updater().checkForUpdates();
    }

    public CompletableFuture<Player> retrievePlayer(String name) {
        return hypixelAPI.retrievePlayerByName(name)
                .thenCompose(player -> {
                    if (player == null) {
                        return CompletableFuture.completedFuture(new NickedPlayer(name));
                    }

                    return hypixelRankedAPI.retrievePlayer(name)
                            .handle((rankedPlayer, exception) -> {
                                String formattedDisplayname = HypixelUtil.getFormattedDisplayname(player);
                                int rating = rankedPlayer != null && exception == null ? rankedPlayer.getRating() : -1;
                                int level = (int) LevelingUtil.getLevel(player.getNetworkExp());
                                Stats stats = player.getStats();
                                SkyWars skyWars = stats == null ? null : stats.getSkyWars();
                                String kit = skyWars == null ? "Default" : skyWars.<SkyWars.RankedKit>getActiveKit(SkyWars.Type.RANKED).getLocalizedName();

                                return new RankedPlayer(formattedDisplayname, rating, level, kit);
                            });
                });
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public ExecutorService getCommandHandler() {
        return commandHandler;
    }

    public Map<String, CompletableFuture<Player>> getPlayers() {
        return players;
    }

    public HypixelAPI getHypixelAPI() {
        return hypixelAPI;
    }

    public HypixelRankedAPI getHypixelRankedAPI() {
        return hypixelRankedAPI;
    }

    public boolean isOnHypixel() {
        return onHypixel;
    }

    public void setOnHypixel(boolean onHypixel) {
        this.onHypixel = onHypixel;
    }

    public boolean isInRanked() {
        return inRanked;
    }

    public void setInRanked(boolean inRanked) {
        this.inRanked = inRanked;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    public boolean isAutoWho() {
        return autoWho;
    }

    public void setAutoWho(boolean autoWho) {
        this.autoWho = autoWho;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        hypixelAPI.setKey(apiKey);
    }
}

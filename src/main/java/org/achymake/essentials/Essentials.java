package org.achymake.essentials;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.event.events.player.*;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.achymake.essentials.commands.*;
import org.achymake.essentials.components.Account;
import org.achymake.essentials.components.Death;
import org.achymake.essentials.components.Homes;
import org.achymake.essentials.handlers.*;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.achymake.essentials.listeners.*;
import org.achymake.essentials.system.*;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class Essentials extends JavaPlugin {
    private ComponentType<EntityStore, Death> deathComponentType;
    private ComponentType<EntityStore, Homes> homesComponentType;
    private ComponentType<EntityStore, Account> accountComponentType;
    private static Essentials instance;
    private EconomyHandler economyHandler;
    private FileHandler fileHandler;
    private MessageHandler messageHandler;
    private PlayerHandler playerHandler;
    private RandomHandler randomHandler;
    private ScheduleHandler scheduleHandler;
    private TeleportHandler teleportHandler;
    private UniverseHandler universeHandler;
    private VanishHandler vanishHandler;
    public Essentials(@NonNullDecl JavaPluginInit init) {
        super(init);
    }
    @Override
    protected void setup() {
        super.setup();
        accountComponentType = getEntityStoreRegistry().registerComponent(
                Account.class,
                "Account",
                Account.CODEC
        );
        homesComponentType = getEntityStoreRegistry().registerComponent(
                Homes.class,
                "Homes",
                Homes.CODEC
        );
        deathComponentType = getEntityStoreRegistry().registerComponent(
                Death.class,
                "DeathLocationComponent",
                Death.CODEC
        );
        instance = this;
        economyHandler = new EconomyHandler();
        fileHandler = new FileHandler();
        messageHandler = new MessageHandler();
        playerHandler = new PlayerHandler();
        randomHandler = new RandomHandler();
        scheduleHandler = new ScheduleHandler();
        teleportHandler = new TeleportHandler();
        universeHandler = new UniverseHandler();
        vanishHandler = new VanishHandler();
        getFileHandler().setup();
        commands();
        events();
    }
    private void commands() {
        var commandRegistry = getCommandRegistry();
        commandRegistry.registerCommand(new AnnouncementCommand());
        commandRegistry.registerCommand(new BackCommand());
        commandRegistry.registerCommand(new BalanceCommand());
        commandRegistry.registerCommand(new BedCommand());
        commandRegistry.registerCommand(new DelHomeCommand());
        commandRegistry.registerCommand(new DepositCommand());
        commandRegistry.registerCommand(new EconomyCommand());
        commandRegistry.registerCommand(new GMACommand());
        commandRegistry.registerCommand(new GMCCommand());
        commandRegistry.registerCommand(new HomeCommand());
        commandRegistry.registerCommand(new HomesCommand());
        commandRegistry.registerCommand(new PayCommand());
        commandRegistry.registerCommand(new PvPCommand());
        commandRegistry.registerCommand(new SetHomeCommand());
        commandRegistry.registerCommand(new SpawnCommand());
        commandRegistry.registerCommand(new StoreCommand());
        commandRegistry.registerCommand(new TPAcceptCommand());
        commandRegistry.registerCommand(new TPACommand());
        commandRegistry.registerCommand(new TPCancelCommand());
        commandRegistry.registerCommand(new TPDenyCommand());
        commandRegistry.registerCommand(new VanishCommand());
        commandRegistry.registerCommand(new WithdrawCommand());
    }
    private void events() {
        var eventRegistry = getEventRegistry();
        var entityStoreRegistry = getEntityStoreRegistry();
        eventRegistry.registerGlobal(AddPlayerToWorldEvent.class, AddPlayerToWorld::onAddPlayerToWorld);
        eventRegistry.registerGlobal(PlayerChatEvent.class, PlayerChat::onPlayerChat);
        entityStoreRegistry.registerSystem(new ChangeGameMode());
        entityStoreRegistry.registerSystem(new DamageEvent());
        entityStoreRegistry.registerSystem(new DeathEvent());
        entityStoreRegistry.registerSystem(new PlayerAdded());
    }
    public ComponentType<EntityStore, Account> getAccountComponentType() {
        return accountComponentType;
    }
    public ComponentType<EntityStore, Homes> getHomesComponentType() {
        return homesComponentType;
    }
    public ComponentType<EntityStore, Death> getDeathComponentType() {
        return deathComponentType;
    }
    public VanishHandler getVanishHandler() {
        return vanishHandler;
    }
    public UniverseHandler getUniverseHandler() {
        return universeHandler;
    }
    public TeleportHandler getTeleportHandler() {
        return teleportHandler;
    }
    public ScheduleHandler getScheduleHandler() {
        return scheduleHandler;
    }
    public RandomHandler getRandomHandler() {
        return randomHandler;
    }
    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }
    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
    public FileHandler getFileHandler() {
        return fileHandler;
    }
    public EconomyHandler getEconomyHandler() {
        return economyHandler;
    }
    public static Essentials getInstance() {
        return instance;
    }
}
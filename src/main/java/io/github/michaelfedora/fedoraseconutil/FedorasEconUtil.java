/**
 * Created by MichaelFedora on 1/17/2016.
 *
 * This file is released under the MIT License. Please see the LICENSE file for
 * more information. Thank you.
 */
package io.github.michaelfedora.fedoraseconutil;

import com.google.inject.Inject;
import io.github.michaelfedora.fedoraseconutil.cmdexecutors.fedoraseconutil.FeuExecutor;
import io.github.michaelfedora.fedoraseconutil.cmdexecutors.fedoraseconutil.FeuHelpExecutor;
import io.github.michaelfedora.fedoraseconutil.config.FeuConfig;
import io.github.michaelfedora.fedoraseconutil.listeners.BlockPlaceListener;
import io.github.michaelfedora.fedoraseconutil.listeners.PlayerInteractListener;
import me.flibio.updatifier.Updatifier;
import net.minecrell.mcstats.SpongeStatsLite;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.sql.SqlService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

/**
 * The main class yo!
 */
@Updatifier(repoName = "FedorasEconUtil", repoOwner = "MichaelFedora", version = PluginInfo.VERSION)
@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, description = PluginInfo.DESCRIPTION, authors = PluginInfo.AUTHORS)
public class FedorasEconUtil {

    private static FedorasEconUtil instance;

    @Inject
    private Logger logger;
    public static Logger getLogger() { return instance.logger; }

    private ConsoleSource console;
    public static ConsoleSource getConsole() { return instance.console; }

    @Inject
    private SpongeStatsLite stats;

    @Inject
    @ConfigDir(sharedRoot = true)
    private Path sharedConfigDir;
    public static Path getSharedConfigDir() { return instance.sharedConfigDir; }

    public static Path getCurrenciesConfigDir() { return instance.sharedConfigDir.resolve("currencies"); }

    private static SqlService SQL;
    public static javax.sql.DataSource getDataSource(String jdbcUrl) throws SQLException {
        if(SQL == null)
            SQL = Sponge.getServiceManager().provide(SqlService.class).orElseThrow(() -> new SQLException("Could not get SqlService!"));

        return SQL.getDataSource(jdbcUrl);
    }

    public static Connection getAccountsConnection() throws SQLException {
        return getDataSource("jdbc:h2:./mods/FedorasData/econAccounts.db").getConnection();
    }

    public static Connection getTransactionListsConnection() throws SQLException {
        return getDataSource("jdbc:h2:./mods/FedorasData/econTransactionLists.db").getConnection();
    }

    //connBal.prepareStatement("CREATE TABLE IF NOT EXISTS " + identifier + "(currency VARCHAR(255), balance DECIMAL)").execute();
    // ex: fedorascurrency:coins | 500.25
    // ex: fedorascurrency:doubloons | 9001

    //connTrans.prepareStatement("CREATE TABLE IF NOT EXISTS " + identifier + "(id IDENTITY, stamp TIMESTAMP, otherParty VARCHAR(255), operation VARCHAR(255), amount DECIMAL, currency VARCHAR(255)");
    // ex: 101 | 2016-03-21 10:43:50 | null | DEPOSIT | 14.25 | fedorascurrency:coins
    // ex: 102 | 2016-03-21 10:45:29 | {uuid} | TRANSFER | 7 | fedorascurrency:doubloons

    private final LinkedHashMap<List<String>, CommandSpec> subCommands = new LinkedHashMap<>();
    public static LinkedHashMap<List<String>, CommandSpec> getSubCommands() {
        return instance.subCommands;
    }

    private final LinkedHashMap<String, LinkedHashMap<List<String>, CommandSpec>> grandChildCommands = new LinkedHashMap<>();
    public static Optional<LinkedHashMap<List<String>, CommandSpec>> getGrandChildCommands(String name) {
        return Optional.ofNullable(instance.grandChildCommands.get(name));
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent gpie) {
        instance = this;
        this.stats.start();
    }

    @Listener
    public void onInit(GameInitializationEvent gie) {

        this.console = Sponge.getServer().getConsole();

        //getLogger().info("===== " + PluginInfo.NAME + " v" + PluginInfo.VERSION + ": Initializing! =====");
        console.sendMessage(Text.of(TextStyles.BOLD, TextColors.GOLD, "===== ",
                TextStyles.RESET, TextColors.AQUA, PluginInfo.NAME, TextColors.GRAY, " v", TextColors.AQUA, PluginInfo.VERSION, TextColors.RESET, ": Initializing!",
                TextStyles.BOLD, TextColors.GOLD, " ====="));

        // register listeners - transaction list
        Sponge.getEventManager().registerListeners(this, new BlockPlaceListener());
        Sponge.getEventManager().registerListeners(this, new PlayerInteractListener());

        // config stuff

        File currencyConfigDir = getCurrenciesConfigDir().toFile();
        if(!currencyConfigDir.exists()) {
            try {
                currencyConfigDir.mkdir();
            } catch(SecurityException e) {
                logger.error("Could not make private directory!", e);
            }
        }

        FeuConfig.initValue = "text";
        FeuConfig.initialize();

        registerCommands();

        //getLogger().info("===== " + PluginInfo.NAME + " v" + PluginInfo.VERSION + ": Done! =====");
        console.sendMessage(Text.of(TextStyles.BOLD, TextColors.GOLD, "===== ",
                TextStyles.RESET, TextColors.AQUA, PluginInfo.NAME, TextColors.GRAY, " v", TextColors.AQUA, PluginInfo.VERSION, TextColors.RESET, ": Done!",
                TextStyles.BOLD, TextColors.GOLD, " ====="));
    }

    @Listener
    public void OnGameLoadComplete(GameLoadCompleteEvent glce) { }

    private void registerCommands() {
        CommandManager commandManager = Sponge.getCommandManager();

        subCommands.put(FeuHelpExecutor.ALIASES, FeuHelpExecutor.create());

        commandManager.register(this, FeuExecutor.create(subCommands), FeuExecutor.ALIASES);
    }
}

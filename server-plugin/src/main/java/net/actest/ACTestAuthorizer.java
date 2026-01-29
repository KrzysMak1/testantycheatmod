package net.actest;

import net.actest.commands.TestCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ACTestAuthorizer extends JavaPlugin implements Listener, PluginMessageListener {

    public static final String CHANNEL = "acts:auth";

    private final Set<UUID> whitelistedPlayers = ConcurrentHashMap.newKeySet();
    private final Map<UUID, Long> activeSessions = new ConcurrentHashMap<>();
    private boolean testingEnabled = false;
    private String authKey;
    private long sessionTimeoutMs;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfigValues();
        authKey = generateAuthKey();

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, CHANNEL);
        getServer().getMessenger().registerIncomingPluginChannel(this, CHANNEL, this);

        getCommand("actest").setExecutor(new TestCommand(this));

        new BukkitRunnable() {
            @Override
            public void run() {
                broadcastAuthPackets();
                cleanupExpiredSessions();
            }
        }.runTaskTimer(this, 0L, getConfig().getLong("broadcast-interval-ticks", 100L));

        getLogger().info(ChatColor.GREEN + "AC-Test-Authorizer enabled");
        getLogger().info(ChatColor.YELLOW + "Auth Key: " + authKey);
    }

    @Override
    public void onDisable() {
        activeSessions.clear();
        getLogger().info("AC-Test-Authorizer disabled.");
    }

    private void loadConfigValues() {
        sessionTimeoutMs = getConfig().getLong("session-timeout-ms", 30000L);
        List<String> entries = getConfig().getStringList("whitelist");
        for (String entry : entries) {
            try {
                whitelistedPlayers.add(UUID.fromString(entry));
            } catch (IllegalArgumentException ignored) {
                getLogger().warning("Invalid UUID in whitelist: " + entry);
            }
        }
    }

    private String generateAuthKey() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (isPlayerAuthorized(player.getUniqueId())) {
            player.sendMessage(ChatColor.GREEN + "AC Test: AUTHORIZED");
        } else {
            player.sendMessage(ChatColor.RED + "AC Test: NOT AUTHORIZED");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        activeSessions.remove(event.getPlayer().getUniqueId());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!CHANNEL.equals(channel)) {
            return;
        }
        String payload = new String(message);
        if (payload.startsWith("ACTS-AUTH-REQ")) {
            sendAuthPacket(player);
        }
    }

    private void broadcastAuthPackets() {
        if (!testingEnabled) {
            return;
        }
        for (Player player : getServer().getOnlinePlayers()) {
            if (isPlayerAuthorized(player.getUniqueId())) {
                sendAuthPacket(player);
            }
        }
    }

    private void sendAuthPacket(Player player) {
        String data = "ACTS-AUTH|" + authKey + "|" + System.currentTimeMillis() + "|" + player.getUniqueId() + "|VALID";
        player.sendPluginMessage(this, CHANNEL, data.getBytes());
        activeSessions.put(player.getUniqueId(), System.currentTimeMillis());
    }

    private void cleanupExpiredSessions() {
        long now = System.currentTimeMillis();
        activeSessions.entrySet().removeIf(entry -> now - entry.getValue() > sessionTimeoutMs);
    }

    public boolean isPlayerAuthorized(UUID playerId) {
        return testingEnabled && whitelistedPlayers.contains(playerId);
    }

    public void setTestingEnabled(boolean enabled) {
        this.testingEnabled = enabled;
        if (!enabled) {
            activeSessions.clear();
        }
    }

    public boolean isTestingEnabled() {
        return testingEnabled;
    }

    public void addToWhitelist(UUID playerId) {
        whitelistedPlayers.add(playerId);
        saveWhitelist();
    }

    public void removeFromWhitelist(UUID playerId) {
        whitelistedPlayers.remove(playerId);
        saveWhitelist();
    }

    public Set<UUID> getWhitelistedPlayers() {
        return Collections.unmodifiableSet(whitelistedPlayers);
    }

    public String getAuthKey() {
        return authKey;
    }

    private void saveWhitelist() {
        List<String> entries = new ArrayList<>();
        for (UUID uuid : whitelistedPlayers) {
            entries.add(uuid.toString());
        }
        getConfig().set("whitelist", entries);
        saveConfig();
    }
}

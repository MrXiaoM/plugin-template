package top.mrxiaom.example.utils;

import com.google.common.collect.Lists;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"unused"})
public class Util {
    public static String getPlayerName(String s) {
        return getOnlinePlayer(s).map(HumanEntity::getName).orElse(s);
    }

    public static Optional<OfflinePlayer> getOfflinePlayer(String s) {
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            if (p.getName() != null && p.getName().equalsIgnoreCase(s)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    public static List<String> startsWith(String s, String... texts) {
        return startsWith(s, Lists.newArrayList(texts));
    }
    public static List<String> startsWith(String s, Iterable<String> texts) {
        List<String> list = new ArrayList<>();
        s = s.toLowerCase();
        for (String text : texts) {
            if (text.toLowerCase().startsWith(s)) list.add(text);
        }
        return list;
    }
    public static Location toLocation(String world, String loc) {
        Location l = null;
        try {
            String[] s = loc.split(",");
            double x = Double.parseDouble(s[0].trim());
            double y = Double.parseDouble(s[1].trim());
            double z = Double.parseDouble(s[2].trim());
            float yaw = s.length > 3 ? Float.parseFloat(s[3].trim()) : 0;
            float pitch = s.length > 4 ? Float.parseFloat(s[4].trim()) : 0;
            l = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        } catch (Throwable ignored) {
        }
        return l;
    }

    public static String toString(Location loc) {
        return String.format("%.2f, %.2f, %.2f, %.2f, %.2f", loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }
    public static Optional<Player> getOnlinePlayer(String name) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name)) return Optional.of(player);
        }
        return Optional.empty();
    }
    public static List<Player> getOnlinePlayers(List<UUID> uuidList) {
        List<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (uuidList.contains(player.getUniqueId())) players.add(player);
        }
        return players;
    }
    public static Optional<Double> parseDouble(String s) {
        if (s == null) return Optional.empty();
        try {
            return Optional.of(Double.parseDouble(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
    public static Optional<Integer> parseInt(String s) {
        if (s == null) return Optional.empty();
        try {
            return Optional.of(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
    public static Optional<Long> parseLong(String s) {
        if (s == null) return Optional.empty();
        try {
            return Optional.of(Long.parseLong(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
    public static <T extends Enum<?>> T valueOr(Class<T> c, String s, T def) {
        if (s == null) return def;
        for (T t : c.getEnumConstants()) {
            if (t.name().equalsIgnoreCase(s)) return t;
        }
        return def;
    }

    public static void registerPlaceholder(Logger logger, PlaceholderExpansion placeholder) {
        PlaceholderAPIPlugin.getInstance()
                .getLocalExpansionManager()
                .findExpansionByIdentifier(placeholder.getIdentifier())
                .ifPresent(PlaceholderExpansion::unregister);
        if (placeholder.isRegistered()) return;
        if (!placeholder.register()) {
            logger.info("无法注册 " + placeholder.getName() + " PAPI 变量");
        }
    }

    public static boolean isMoved(PlayerMoveEvent e) {
        Location loc1 = e.getFrom();
        Location loc2 = e.getTo();
        return loc1.getBlockX() != loc2.getBlockX()
                || loc1.getBlockZ() != loc2.getBlockZ();
    }

    public static boolean isPresent(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    public static String stackTraceToString(Throwable t) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            t.printStackTrace(pw);
        }
        return sw.toString();
    }

    public static <T> List<T> split(Pattern regex, String s, Function<RegexResult, T> transform) {
        List<T> list = new ArrayList<>();
        int index = 0;
        Matcher m = regex.matcher(s);
        while (m.find()) {
            int first = m.start();
            int last = m.end();
            if (first > index) {
                T value = transform.apply(new RegexResult(false, s.substring(index, first)));
                if (value != null) list.add(value);
            }
            T value = transform.apply(new RegexResult(true, s.substring(first, last)));
            if (value != null) list.add(value);
            index = last;
        }
        if (index < s.length()) {
            T value = transform.apply(new RegexResult(false, s.substring(index)));
            if (value != null) list.add(value);
        }
        return list;
    }

    public static class RegexResult {
        public boolean isMatched;
        public String text;

        public RegexResult(boolean isMatched, String text) {
            this.isMatched = isMatched;
            this.text = text;
        }
    }
}

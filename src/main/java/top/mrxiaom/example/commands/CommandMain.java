package top.mrxiaom.example.commands;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.example.ExamplePlugin;
import top.mrxiaom.example.func.AbstractPluginHolder;

import java.util.ArrayList;
import java.util.List;

public class CommandMain extends AbstractPluginHolder implements CommandExecutor, TabCompleter {

    public CommandMain(ExamplePlugin plugin) {
        super(plugin);
        registerCommand("example", this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            Player player = sender instanceof Player ? (Player) sender : null;
            if (args[0].equalsIgnoreCase("open") && player != null) {
                // plugin.getGuiManager().openGui();
                return true;
            }
            if (args[0].equalsIgnoreCase("reload") && sender.isOp()) {
                plugin.reloadConfig();
                return t(sender, "&a配置文件已重载");
            }
        }
        return true;
    }

    private static final List<String> emptyList = Lists.newArrayList();
    private static final List<String> listArg0 = Lists.newArrayList(
            "open");
    private static final List<String> listOpArg0 = Lists.newArrayList(
            "open", "reload");
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return startsWith(sender.isOp() ? listOpArg0 : listArg0, args[0]);
        }
        return emptyList;
    }

    public List<String> startsWith(List<String> list, String s) {
        String s1 = s.toLowerCase();
        List<String> stringList = new ArrayList<>(list);
        stringList.removeIf(it -> !it.toLowerCase().startsWith(s1));
        return stringList;
    }
}

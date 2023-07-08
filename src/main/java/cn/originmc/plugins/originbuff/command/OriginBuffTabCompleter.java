package cn.originmc.plugins.originbuff.command;

import cn.originmc.plugins.originbuff.data.BuffData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OriginBuffTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(Arrays.asList("reload", "add", "clear", "remove", "increase", "reduce"));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("clear") ||
                    args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("increase") ||
                    args[0].equalsIgnoreCase("reduce")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove") ||
                    args[0].equalsIgnoreCase("increase") || args[0].equalsIgnoreCase("reduce")) {
                completions.addAll(BuffData.getBuffFormatIds());
            }
        }
        completions.sort(String.CASE_INSENSITIVE_ORDER);
        return completions;
    }
}

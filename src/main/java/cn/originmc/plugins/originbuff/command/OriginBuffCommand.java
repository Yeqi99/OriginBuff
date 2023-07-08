package cn.originmc.plugins.originbuff.command;

import cn.originmc.plugins.originbuff.BuffManager;
import cn.originmc.plugins.originbuff.OriginBuff;
import cn.originmc.plugins.originbuff.data.BuffData;
import cn.originmc.plugins.originbuff.object.Buff;
import cn.originmc.plugins.originbuff.object.BuffFormat;
import cn.originmc.plugins.origincore.util.command.CommandUtil;
import cn.originmc.plugins.origincore.util.text.Sender;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;


public class OriginBuffCommand implements CommandExecutor {
    public static Sender s=new Sender(OriginBuff.getInstance());
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        CommandUtil cu = new CommandUtil(sender, command, label, args);
        if (cu.getParameterAmount() == 0) {
            return true;
        }
        if (cu.is(0, "reload")) {
            if (!hasPerm(sender, "reload")) {
                return true;
            }
            BuffData.load();
            OriginBuff.getInstance().reloadConfig();
            s.sendToSender(sender, "&a重载成功！");
        } else if (cu.is(0, "add")) {
            if (!hasPerm(sender, "add")) {
                return true;
            }
            String playerName = cu.getParameter(1);
            String id = cu.getParameter(2);
            String duration = cu.getParameter(3);
            if (playerName == null || id == null || duration == null) {
                s.sendToSender(sender, "&c用法: /buff add <玩家名字> <Buff ID> <持续时间>");
                return true;
            }
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                s.sendToSender(sender, "&c玩家不在线或不存在！");
                return true;
            }
            BuffFormat buffFormat = BuffData.getBuffFormat(id);
            if (buffFormat == null) {
                s.sendToSender(sender, "&c指定的Buff ID不存在！");
                return true;
            }
            Buff buff = buffFormat.createBuff(Arrays.copyOfRange(args, 4, args.length), Integer.parseInt(duration));
            OriginBuff.getBuffManager().addBuff(player.getUniqueId(), buff);
            s.sendToSender(sender, "&aBuff添加成功！你给玩家" + player.getName() + "添加了名为" + buff.getName() + "的buff");
        } else if (cu.is(0, "clear")) {
            if (!hasPerm(sender, "clear")) {
                return true;
            }
            String playerName = cu.getParameter(1);
            if (playerName == null) {
                s.sendToSender(sender, "&c用法: /buff clear <玩家名字>");
                return true;
            }
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                s.sendToSender(sender, "&c玩家不在线或不存在！");
                return true;
            }
            OriginBuff.getBuffManager().clear(player);
            s.sendToSender(sender, "&a已清除" + player.getName() + "的全部Buff");
        } else if (cu.is(0, "remove")) {
            if (!hasPerm(sender, "remove")) {
                return true;
            }
            String playerName = cu.getParameter(1);
            String name = cu.getParameter(2);
            if (playerName == null || name == null) {
                s.sendToSender(sender, "&c用法: /buff remove <玩家名字> <Buff名字>");
                return true;
            }
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                s.sendToSender(sender, "&c玩家不在线或不存在！");
                return true;
            }
            OriginBuff.getBuffManager().removeBuff(player.getUniqueId(), name);
            s.sendToSender(sender, "&a已移除" + player.getName() + "名为" + name + "的Buff");
        } else if (cu.is(0, "increase")) {
            if (!hasPerm(sender, "increase")) {
                return true;
            }
            String playerName = cu.getParameter(1);
            String name = cu.getParameter(2);
            String duration = cu.getParameter(3);
            if (playerName == null || name == null || duration == null) {
                s.sendToSender(sender, "&c用法: /buff increase <玩家名字> <Buff名字> <持续时间>");
                return true;
            }
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                s.sendToSender(sender, "&c玩家不在线或不存在！");
                return true;
            }
            OriginBuff.getBuffManager().increase(player, name, Integer.parseInt(duration));
            s.sendToSender(sender, "&a已增加玩家" + player.getName() + "名为" + name + "的Buff" + duration + "秒");
        } else if (cu.is(0, "reduce")) {
            if (!hasPerm(sender, "reduce")) {
                return true;
            }
            String playerName = cu.getParameter(1);
            String name = cu.getParameter(2);
            String duration = cu.getParameter(3);
            if (playerName == null || name == null || duration == null) {
                s.sendToSender(sender, "&c用法: /buff reduce <玩家名字> <Buff名字> <持续时间>");
                return true;
            }
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                s.sendToSender(sender, "&c玩家不在线或不存在！");
                return true;
            }
            OriginBuff.getBuffManager().reduce(player, name, Integer.parseInt(duration));
            s.sendToSender(sender, "&a已减少玩家" + player.getName() + "名为" + name + "的Buff" + duration + "秒");
        }
        return true;
    }

    public static boolean hasPerm(CommandSender sender, String command){
        if (sender instanceof Player){
            Player player= (Player) sender;
            if (player.isOp()){
                return true;
            }
            return player.hasPermission("OriginBuff." + command);
        }else {
            return true;
        }
    }

}

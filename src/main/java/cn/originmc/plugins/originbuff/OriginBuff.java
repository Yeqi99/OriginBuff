package cn.originmc.plugins.originbuff;

import cn.originmc.plugins.originbuff.command.OriginBuffCommand;
import cn.originmc.plugins.originbuff.command.OriginBuffTabCompleter;
import cn.originmc.plugins.originbuff.data.BuffData;
import cn.originmc.plugins.originbuff.event.BuffEvent;
import cn.originmc.plugins.originbuff.object.Buff;
import cn.originmc.plugins.origincore.util.register.CommandRegister;
import cn.originmc.plugins.origincore.util.register.CompleterRegister;
import cn.originmc.plugins.origincore.util.register.ListenerRegister;
import cn.originmc.plugins.origincore.util.text.Sender;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public final class OriginBuff extends JavaPlugin {
    private static JavaPlugin instance;
    private static BuffManager buffManager;
    private static BukkitTask buffExpirationTask;
    private static Map<Player, BossBar> playerBossBars;
    private static final String VERSION = "1.0";
    public static BuffManager getBuffManager() {
        return buffManager;
    }

    public static void setBuffManager(BuffManager buffManager) {
        OriginBuff.buffManager = buffManager;
    }

    public static BukkitTask getBuffExpirationTask() {
        return buffExpirationTask;
    }

    public static void setBuffExpirationTask(BukkitTask buffExpirationTask) {
        OriginBuff.buffExpirationTask = buffExpirationTask;
    }

    public static Map<Player, BossBar> getPlayerBossBars() {
        return playerBossBars;
    }

    public static void setPlayerBossBars(Map<Player, BossBar> playerBossBars) {
        OriginBuff.playerBossBars = playerBossBars;
    }

    public static JavaPlugin getInstance() {
        return instance;
    }

    public static void setInstance(JavaPlugin instance) {
        OriginBuff.instance = instance;
    }

    @Override
    public void onEnable() {
        instance=this;
        // 加载配置文件数据
        BuffData.load();
        // 初始化BuffManager
        buffManager = new BuffManager();
        // 初始化playerBossBars
        playerBossBars = new HashMap<>();
        // 注册事件监听器
        ListenerRegister.register(this,new BuffEvent());
        // 创建异步定时任务，每秒更新所有玩家的 BossBar和Buff剩余时间
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, this::updateBuffsAndBossBars, 0L, 20L);
        // 注册指令
        CommandRegister.register(this,new OriginBuffCommand(),"OriginBuff");
        // 注册指令补全
        CompleterRegister.register(this,new OriginBuffTabCompleter(),"OriginBuff");
        new Sender(this).sendOnEnableMsgToLogger("OriginBuff","Yeqi",VERSION,"Public");
    }

    @Override
    public void onDisable() {
        new Sender(this).sendOnDisableMsgToLogger("OriginBuff","Yeqi",VERSION,"Public");
    }

    private void updateBuffsAndBossBars() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID playerId = player.getUniqueId();

            // 获取玩家的 Buff 列表
            List<Buff> buffs = buffManager.getPlayerBuffs(playerId);
            if (buffs.size() == 0) {
                BossBar bossBar = playerBossBars.get(player);
                if (bossBar != null) {
                    bossBar.removePlayer(player);
                }
                continue;
            }

            // 根据剩余时间进行排序
            buffs.sort(Comparator.comparingInt(Buff::getDuration));

            // 只选择最多 5 个 Buff 进行显示
            int maxDisplayedBuffs = 5;
            List<Buff> displayedBuffs = buffs.subList(0, Math.min(buffs.size(), maxDisplayedBuffs));

            // 记录剩下的 Buff 的数量
            int remainingBuffs = Math.max(0, buffs.size() - maxDisplayedBuffs);

            // 将选中的 Buff 转换为符号-剩余时间的字符串表示，并用空格拼接起来
            StringJoiner buffJoiner = new StringJoiner(" ");
            for (Buff buff : displayedBuffs) {
                buffJoiner.add(buff.getSymbolAndRemainingTime());
            }

            // 如果还有剩下的 Buff，添加一个 "...+n" 的后缀
            if (remainingBuffs > 0) {
                buffJoiner.add("..." + remainingBuffs);
            }

            // 获取玩家对应的 BossBar
            BossBar bossBar = playerBossBars.get(player);
            if (bossBar == null) {
                // 如果玩家还没有对应的 BossBar，创建一个新的 BossBar
                bossBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);
                playerBossBars.put(player, bossBar);
            }

            // 设置 BossBar 的显示内容
            String bossBarContent = buffJoiner.toString();
            bossBar.setTitle(bossBarContent);

            // 更新 BossBar 的显示
            bossBar.setVisible(true);
            bossBar.setProgress(1.0f);
            if (!bossBar.getPlayers().contains(player)) {
                bossBar.addPlayer(player);
            }

            // 更新 Buff 的持续时间并处理过期的 Buff
            Iterator<Buff> iterator = buffs.iterator();
            while (iterator.hasNext()) {
                Buff buff = iterator.next();
                buff.decreaseDuration(1); // 减少 1 秒的持续时间

                if (buff.getDuration() <= 0) {
                    // Buff过期，执行相应的操作
                    handleBuffExpiration(player, buff);
                    iterator.remove(); // 使用迭代器进行元素删除
                    buffManager.removeBuff(playerId, buff);
                }
            }
        }
    }
    // 处理Buff过期的操作
    private void handleBuffExpiration(Player player, Buff buff) {
        // 执行Buff过期后的操作，例如移除Buff增加的效果等
    }
}

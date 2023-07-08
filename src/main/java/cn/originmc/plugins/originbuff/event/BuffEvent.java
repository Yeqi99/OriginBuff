package cn.originmc.plugins.originbuff.event;


import cn.originmc.plugins.originbuff.OriginBuff;
import cn.originmc.plugins.originbuff.object.Buff;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.UUID;

public class BuffEvent implements Listener {
    // 玩家进入游戏事件,添加入服buff
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

    }

    // 玩家离开游戏事件
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // 移除玩家的所有Buff
        List<Buff> buffs = OriginBuff.getBuffManager().getPlayerBuffs(playerId);
        for (Buff buff : buffs) {
            OriginBuff.getBuffManager().removeBuff(playerId, buff);
        }
    }
}

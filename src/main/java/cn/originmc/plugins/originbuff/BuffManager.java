package cn.originmc.plugins.originbuff;

import cn.originmc.plugins.originbuff.object.Buff;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class BuffManager {
    private Map<UUID, List<Buff>> playerBuffs;


    public BuffManager() {
        playerBuffs = new HashMap<>();
    }

    public void addBuff(UUID playerId, Buff buff) {
        List<Buff> buffs = playerBuffs.computeIfAbsent(playerId, k -> new ArrayList<>());
        buffs.add(buff);
    }

    public void removeBuff(UUID playerId, Buff buff) {
        List<Buff> buffs = playerBuffs.get(playerId);
        if (buffs != null) {
            buffs.remove(buff);
            if (buffs.isEmpty()) {
                playerBuffs.remove(playerId);
            }
        }
    }
    public void removeBuff(UUID playerId, String buffName) {
        List<Buff> buffs = playerBuffs.get(playerId);
        if (buffs != null) {
            for (Buff buff : buffs) {
                if (buff.getName().equalsIgnoreCase(buffName)) {
                    buffs.remove(buff);
                    if (buffs.isEmpty()) {
                        playerBuffs.remove(playerId);
                    }
                    break;
                }
            }
        }
    }
    public void clear(Player player){
        if(playerBuffs.containsKey(player.getUniqueId())){
            playerBuffs.remove(player.getUniqueId());
        }
    }
    public void increase(Player player,String buffName,int time){
        Buff buff=getBuff(player,buffName);
        buff.increaseDuration(time);
        setBuff(player,buffName,buff);
    }
    public void reduce(Player player,String buffName,int time){
        Buff buff=getBuff(player,buffName);
        buff.decreaseDuration(time);
        setBuff(player,buffName,buff);
    }
    public void setBuff(Player player,String buffName,Buff buff){
        if (buffName==null){
            return;
        }
        if (buff==null){
            return;
        }
        Buff b=getBuff(player,buffName);
        if (b==null){
            return;
        }
        removeBuff(player.getUniqueId(),b);
        addBuff(player.getUniqueId(),buff);
    }

    public Buff getBuff(Player player,String buffName){
        for (Buff playerBuff : getPlayerBuffs(player)) {
            if (playerBuff.getName().equalsIgnoreCase(buffName)){
                return playerBuff;
            }
        }
        return null;
    }
    public boolean hasBuff(Player player,String buffName){
        for (Buff playerBuff : getPlayerBuffs(player)) {
            if (playerBuff.getName().equalsIgnoreCase(buffName)){
                return true;
            }
        }
        return false;
    }
    public List<Buff> getPlayerBuffs(UUID playerId) {
        return playerBuffs.getOrDefault(playerId, new ArrayList<>());
    }
    public List<Buff> getPlayerBuffs(Player player) {
        return playerBuffs.getOrDefault(player.getUniqueId(), new ArrayList<>());
    }
    public List<Buff> getPlayerBuffs(String playerName) {
        Player player= Bukkit.getPlayer(playerName);
        if (player==null){
            return null;
        }
        return playerBuffs.getOrDefault(player.getUniqueId(), new ArrayList<>());
    }
}

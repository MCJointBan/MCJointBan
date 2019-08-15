package org.lintx.mcjointban.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.lintx.mcjointban.MCJointBanInterface;
import org.lintx.mcjointban.config.Config;
import org.lintx.plugins.modules.configure.Configure;

import java.io.File;
import java.util.UUID;

public class MCJointBan extends JavaPlugin implements MCJointBanInterface {
    private org.lintx.mcjointban.MCJointBan jointBan;
    private Task task;

    @Override
    public void onEnable() {
        Config config = Config.getInstance();
        Configure.bukkitLoad(this, config);
        File file = new File(this.getDataFolder(),"config.yml");
        if (!file.exists()){
            Configure.bukkitSave(this,config);
        }

        jointBan = new org.lintx.mcjointban.MCJointBan(this.getDataFolder(),getLogger(),this);
        task = new Task(jointBan);
        if (config.getCheckConfig().isOnOpen()){
            getServer().getScheduler().runTaskAsynchronously(this, () -> jointBan.check());
        }
        if (config.getCheckConfig().getOnTimer()>0){
            long time = config.getCheckConfig().getOnTimer() * 20;
            task.runTaskTimerAsynchronously(this,time,time);
        }

        getCommand("jointban").setExecutor(new Commands(this));
        getServer().getPluginManager().registerEvents(new Listeners(this),this);
    }

    @Override
    public void onDisable() {
        task.cancel();
    }

    public void reloadConfig(){
        task.cancel();
        Config config = Config.getInstance();
        Configure.bukkitLoad(this, config);
        if (config.getCheckConfig().getOnTimer()>0){
            long time = config.getCheckConfig().getOnTimer() * 20;
            task.runTaskTimerAsynchronously(this,time,time);
        }
    }

    org.lintx.mcjointban.MCJointBan getJointBan() {
        return jointBan;
    }

    @Override
    public void kickPlayer(UUID uuid, String reason) {
        Player player = getServer().getPlayer(uuid);
        if (player!=null && player.isOnline()){
            player.kickPlayer(reason);
        }
    }
}

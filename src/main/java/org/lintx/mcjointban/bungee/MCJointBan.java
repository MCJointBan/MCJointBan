package org.lintx.mcjointban.bungee;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.lintx.mcjointban.MCJointBanInterface;
import org.lintx.mcjointban.config.Config;
import org.lintx.plugins.modules.configure.Configure;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MCJointBan extends Plugin implements MCJointBanInterface {
    private org.lintx.mcjointban.MCJointBan jointBan;
    private Task task;
    private ScheduledTask scheduledTask;

    @Override
    public void onEnable() {
        Config config = Config.getInstance();
        Configure.bungeeLoad(this, config);
        File file = new File(this.getDataFolder(),"config.yml");
        if (!file.exists()){
            Configure.bungeeSave(this,config);
        }

        jointBan = new org.lintx.mcjointban.MCJointBan(this.getDataFolder(),getLogger(),this);
        task = new Task(jointBan);
        if (config.getCheckConfig().isOnOpen()){
            jointBan.check();
        }
        if (config.getCheckConfig().getOnTimer()>0){
            long time = config.getCheckConfig().getOnTimer();
            scheduledTask = getProxy().getScheduler().schedule(this,task,time,time, TimeUnit.SECONDS);
        }

        getProxy().getPluginManager().registerCommand(this,new Commands(this,"jointban","jointban","jban"));
        getProxy().getPluginManager().registerListener(this,new Listeners(this));
    }

    @Override
    public void onDisable() {
        if (scheduledTask!=null){
            scheduledTask.cancel();
        }
    }

    public void reloadConfig(){
        if (scheduledTask!=null){
            scheduledTask.cancel();
        }
        Config config = Config.getInstance();
        Configure.bungeeLoad(this, config);
        if (config.getCheckConfig().getOnTimer()>0){
            long time = config.getCheckConfig().getOnTimer();
            scheduledTask = getProxy().getScheduler().schedule(this,task,time,time, TimeUnit.SECONDS);
        }
    }

    org.lintx.mcjointban.MCJointBan getJointBan() {
        return jointBan;
    }

    @Override
    public void kickPlayer(UUID uuid, String reason) {
        ProxiedPlayer player = getProxy().getPlayer(uuid);
        if (player!=null){
            player.disconnect(new TextComponent(reason));
        }
    }
}

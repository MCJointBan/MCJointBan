package org.lintx.mcjointban.bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.lintx.mcjointban.models.BansModel;

public class Listeners implements Listener {
    private MCJointBan jointBan;
    Listeners(MCJointBan jointBan){
        this.jointBan = jointBan;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event){
        if (event.getResult()!= PlayerLoginEvent.Result.ALLOWED){
            return;
        }
        BansModel model = jointBan.getJointBan().getBanModel(event.getPlayer().getUniqueId());
        if (model.isBan()){
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED,model.getKickReason());
        }
    }
}

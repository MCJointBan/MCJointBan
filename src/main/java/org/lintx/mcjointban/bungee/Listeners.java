package org.lintx.mcjointban.bungee;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.lintx.mcjointban.models.BansModel;


public class Listeners implements Listener {
    private MCJointBan jointBan;
    Listeners(MCJointBan jointBan){
        this.jointBan = jointBan;
    }

    @EventHandler
    public void onLogin(LoginEvent event){
        if (event.isCancelled()){
            return;
        }
        BansModel model = jointBan.getJointBan().getBanModel(event.getConnection().getUniqueId());
        if (model.isBan()){
            event.setCancelReason(new TextComponent(model.getKickReason()));
            event.setCancelled(true);
        }
    }
}

package org.lintx.mcjointban.bukkit;

import org.bukkit.scheduler.BukkitRunnable;
import org.lintx.mcjointban.MCJointBan;

public class Task extends BukkitRunnable {
    private org.lintx.mcjointban.MCJointBan jointBan;
    Task(MCJointBan jointBan){
        this.jointBan = jointBan;
    }

    @Override
    public void run() {
        jointBan.check();
    }
}

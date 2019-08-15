package org.lintx.mcjointban.bungee;

import org.lintx.mcjointban.MCJointBan;

public class Task implements Runnable {
    private MCJointBan jointBan;
    Task(MCJointBan jointBan){
        this.jointBan = jointBan;
    }

    @Override
    public void run() {
        jointBan.check();
    }
}

package org.lintx.mcjointban.config;

import org.lintx.plugins.modules.configure.YamlConfig;

public class CheckConfig {
    public boolean isOnOpen() {
        return onOpen;
    }

    public int getOnTimer() {
        return onTimer;
    }

    @YamlConfig
    private boolean onOpen = true;
    @YamlConfig
    private int onTimer = 3600;
}

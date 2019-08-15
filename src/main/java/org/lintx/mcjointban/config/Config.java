package org.lintx.mcjointban.config;

import org.lintx.plugins.modules.configure.YamlConfig;

@YamlConfig
public class Config {
    private static Config instance = new Config();

    public static Config getInstance(){
        if (instance==null) instance = new Config();
        return instance;
    }

    public GitHubConfig getGitHubConfig() {
        return gitHubConfig;
    }

    public CheckConfig getCheckConfig() {
        return checkConfig;
    }

    public String getBanMessage() {
        return banMessage;
    }

    @YamlConfig
    private GitHubConfig gitHubConfig = new GitHubConfig();
    @YamlConfig
    private CheckConfig checkConfig = new CheckConfig();
    @YamlConfig
    private String banMessage = "§c你在 §6{server} §r§c服务器因为 §6{reason} §c被封禁，本服已§6联合封禁§c。";
}

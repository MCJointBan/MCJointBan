package org.lintx.mcjointban.config;

import org.lintx.plugins.modules.configure.YamlConfig;

public class GitHubConfig {
    public String getUsername() {
        return username;
    }

    public String getRepository() {
        return repository;
    }

    @YamlConfig
    private String username = "MCJointBan";
    @YamlConfig
    private String repository = "BanList";
}

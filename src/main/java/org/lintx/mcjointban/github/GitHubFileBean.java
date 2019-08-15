package org.lintx.mcjointban.github;

import com.google.gson.annotations.SerializedName;

public class GitHubFileBean {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("name")
    private String name;
    @SerializedName("path")
    private String path;
    @SerializedName("sha")
    private String sha;
    @SerializedName("download_url")
    private String url;
    @SerializedName("type")
    private String type;
}

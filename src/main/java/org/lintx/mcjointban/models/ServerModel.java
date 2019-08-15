package org.lintx.mcjointban.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServerModel {
    private int id;
    private String folder;
    private String name;
    private String sha;
    private String display_name;

    private int status;

    public ServerModel(){

    }

    public ServerModel(ResultSet rs){
        try {
            setId(rs.getInt("id"));
            setFolder(rs.getString("folder"));
            setName(rs.getString("name"));
            setSha(rs.getString("sha"));
            setDisplay_name(rs.getString("display_name"));
            setStatus(rs.getInt("status"));
        } catch (SQLException ignored) {

        }
    }

    public String getDisplay_name() {
        if (display_name.equals("")){
            return name;
        }
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isEnable(){
        return this.status==1;
    }

    public void setEnable(boolean isEnable){
        this.status = isEnable?1:0;
    }
}

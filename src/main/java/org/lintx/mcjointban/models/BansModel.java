package org.lintx.mcjointban.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BansModel {
    private UUID uuid;
    private String reason;
    private int server_id;
    private int status;

    private String kickReason;


    public BansModel(){

    }

    public BansModel(ResultSet rs){
        try {
            setUuid(UUID.fromString(rs.getString("uuid")));
            setReason(rs.getString("reason"));
            setServer_id(rs.getInt("server_id"));
            setStatus(rs.getInt("status"));
        } catch (SQLException ignored) {

        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isBan(){
        return status==1;
    }

    public String getKickReason() {
        return kickReason;
    }

    public void setKickReason(String kickReason) {
        this.kickReason = kickReason;
    }

    public boolean isEnable(){
        return this.status==1;
    }

    public void setEnable(boolean isEnable){
        this.status = isEnable?1:0;
    }
}

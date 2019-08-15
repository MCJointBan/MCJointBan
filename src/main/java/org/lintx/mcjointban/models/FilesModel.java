package org.lintx.mcjointban.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilesModel {
    private int id;
    private int server_id;
    private String name;
    private String sha;
    private int read_line;

    public FilesModel(){

    }

    public FilesModel(ResultSet rs){
        try {
            setId(rs.getInt("id"));
            setServer_id(rs.getInt("server_id"));
            setName(rs.getString("name"));
            setSha(rs.getString("sha"));
            setRead_line(rs.getInt("read_line"));
        } catch (SQLException ignored) {

        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
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

    public int getRead_line() {
        return read_line;
    }

    public void setRead_line(int read_line) {
        this.read_line = read_line;
    }
}

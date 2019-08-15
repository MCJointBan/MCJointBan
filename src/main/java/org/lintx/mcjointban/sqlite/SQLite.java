package org.lintx.mcjointban.sqlite;

import org.lintx.mcjointban.models.BansModel;
import org.lintx.mcjointban.models.FilesModel;
import org.lintx.mcjointban.models.ServerModel;

import java.io.File;
import java.sql.*;
import java.util.*;

public class SQLite{
    private Connection connection;
    private String dbname;
    private File folder;
    public SQLite(File folder,String dbname){
        this.folder = folder;
        this.dbname = dbname;
    }

    private Connection getSQLConnection() {
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }

            File dataFolder = new File(folder, dbname+".db");

            Class.forName("org.sqlite.JDBC");
            Properties properties = new Properties();
            properties.setProperty("characterEncoding", "UTF-8");
            properties.setProperty("encoding", "\"UTF-8\"");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder.toString());
            return connection;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            String SQLiteCreateTable1 = "CREATE TABLE IF NOT EXISTS \"server\" (\"id\" INTEGER PRIMARY KEY,\"folder\" varchar(64) NOT NULL,\"name\" varchar(64) NOT NULL,\"sha\" varchar(64) NOT NULL,\"display_name\" varchar(20) NOT NULL,\"status\" INTEGER NOT NULL);";
            s.executeUpdate(SQLiteCreateTable1);
            String SQLiteCreateTable2 = "CREATE TABLE IF NOT EXISTS \"files\" (\"id\" INTEGER PRIMARY KEY,\"server_id\" INTEGER NOT NULL,\"name\" varchar(64) NOT NULL,\"sha\" varchar(64) NOT NULL,\"read_line\" INTEGER NOT NULL);";
            s.executeUpdate(SQLiteCreateTable2);
            String SQLiteCreateTable3 = "CREATE TABLE IF NOT EXISTS bans (\"uuid\" varchar(64) PRIMARY KEY NOT NULL,\"reason\" varchar(64) NOT NULL,\"server_id\" INTEGER NOT NULL,\"status\" INTEGER NOT NULL);";
            s.executeUpdate(SQLiteCreateTable3);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ServerModel getServer(int id){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM server WHERE id=? LIMIT 1");
            ps.setInt(1,id);

            rs = ps.executeQuery();
            while(rs.next()){
                return new ServerModel(rs);
            }
        } catch (SQLException ignored) {

        } finally {
            release(conn,ps,rs);
        }
        return null;
    }

    public ServerModel getServer(String name){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM server WHERE name=? LIMIT 1");
            ps.setString(1,name);

            rs = ps.executeQuery();
            while(rs.next()){
                return new ServerModel(rs);
            }
        } catch (SQLException ignored) {

        } finally {
            release(conn,ps,rs);
        }
        return null;
    }

    public List<ServerModel> getServerList(int page){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM server order by id LIMIT 10 offset ?");
            ps.setInt(1,(page-1)*10);

            rs = ps.executeQuery();
            List<ServerModel> list = new ArrayList<>();
            while(rs.next()){
                ServerModel model = new ServerModel(rs);
                list.add(model);
            }
            return list;
        } catch (SQLException ignored) {

        } finally {
            release(conn,ps,rs);
        }
        return new ArrayList<>();
    }

    public boolean updateServer(ServerModel model){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("update server set sha = ?,display_name=?,status=? where id=?");
            ps.setString(1,model.getSha());
            ps.setString(2,model.getDisplay_name());
            ps.setInt(3,model.getStatus());
            ps.setInt(4,model.getId());

            int r =  ps.executeUpdate();
            return r>0;
        } catch (SQLException ignored) {

        } finally {
            release(conn,ps,null);
        }
        return false;
    }

    public boolean insertServer(ServerModel model){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("insert into server (folder,name,sha,display_name,status) values (?,?,?,?,1)");
            ps.setString(1,model.getFolder());
            ps.setString(2,model.getName());
            ps.setString(3,model.getSha());
            ps.setString(4,model.getDisplay_name());

            int r =  ps.executeUpdate();
            return r>0;
        } catch (SQLException ignored) {

        } finally {
            release(conn,ps,null);
        }
        return false;
    }

    public FilesModel getFile(int server_id,String name){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM files WHERE server_id=? and name=? LIMIT 1");
            ps.setInt(1,server_id);
            ps.setString(2,name);

            rs = ps.executeQuery();
            while(rs.next()){
                return new FilesModel(rs);
            }
        } catch (SQLException ignored) {

        } finally {
            release(conn,ps,rs);
        }
        return null;
    }

    public boolean updateFile(FilesModel model){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("update files set sha = ?,read_line = ? where id=?");
            ps.setString(1,model.getSha());
            ps.setInt(2,model.getRead_line());
            ps.setInt(3,model.getId());

            int r =  ps.executeUpdate();
            return r>0;
        } catch (SQLException ignored) {

        } finally {
            release(conn,ps,null);
        }
        return false;
    }

    public boolean insertFile(FilesModel model){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("insert into files (server_id,name,sha,read_line) values (?,?,?,0)");
            ps.setInt(1,model.getServer_id());
            ps.setString(2,model.getName());
            ps.setString(3,model.getSha());

            int r =  ps.executeUpdate();
            return r>0;
        } catch (SQLException ignored) {

        } finally {
            release(conn,ps,null);
        }
        return false;
    }

    public BansModel getBan(UUID uuid){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM bans WHERE uuid=? LIMIT 1");
            ps.setString(1,uuid.toString().toLowerCase(Locale.ROOT));

            rs = ps.executeQuery();
            while(rs.next()){
                return new BansModel(rs);
            }
        } catch (SQLException ignored) {

        } finally {
            release(conn,ps,rs);
        }
        return null;
    }

    public boolean updateBan(BansModel model){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("update bans set status = ? where uuid=?");
            ps.setInt(1,model.getStatus());
            ps.setString(2,model.getUuid().toString().toLowerCase(Locale.ROOT));

            int r =  ps.executeUpdate();
            return r>0;
        } catch (SQLException ignored) {

        } finally {
            release(conn,ps,null);
        }
        return false;
    }

    public boolean insertBan(BansModel model){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("insert into bans (uuid,reason,server_id,status) values (?,?,?,1)");
            ps.setString(1,model.getUuid().toString().toLowerCase(Locale.ROOT));
            ps.setString(2,model.getReason());
            ps.setInt(3,model.getServer_id());

            int r =  ps.executeUpdate();
            return r>0;
        } catch (SQLException ignored) {

        } finally {
            release(conn,ps,null);
        }
        return false;
    }

    private void release(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception ignored) {

            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (Exception ignored) {

            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception ignored) {

            }
        }
    }
}
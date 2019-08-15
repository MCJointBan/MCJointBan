package org.lintx.mcjointban;

import org.lintx.mcjointban.config.Config;
import org.lintx.mcjointban.github.GitHubApi;
import org.lintx.mcjointban.github.GitHubFileBean;
import org.lintx.mcjointban.github.InputStreamInterface;
import org.lintx.mcjointban.models.BansModel;
import org.lintx.mcjointban.models.FilesModel;
import org.lintx.mcjointban.models.ServerModel;
import org.lintx.mcjointban.sqlite.SQLite;

import java.io.*;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class MCJointBan {
    private static SQLite sqLite;
    private static GitHubApi gitHubApi;
    private Logger logger;
    private MCJointBanInterface banInterface;

    public MCJointBan(File folder, Logger logger,MCJointBanInterface banInterface){
        this.banInterface = banInterface;
        this.logger = logger;
        sqLite = new SQLite(folder,"database");
        sqLite.load();
        gitHubApi = new GitHubApi(Config.getInstance().getGitHubConfig().getUsername(),Config.getInstance().getGitHubConfig().getRepository());
    }

    public void check(){
        check("all",false);
    }

    public void check(String server){
        check(server,true);
    }

    public ServerModel getServer(String name){
        return sqLite.getServer(name);
    }

    public List<ServerModel> getServerList(int page){
        return sqLite.getServerList(page);
    }

    public void saveServer(ServerModel model){
        sqLite.updateServer(model);
    }

    public void savePlayer(BansModel model){
        sqLite.updateBan(model);
    }

    public BansModel getBanModel(UUID uuid){
        BansModel model = sqLite.getBan(uuid);
        if (model==null){
            model = new BansModel();
            model.setStatus(-1);
        }
        if (model.isBan()){
            ServerModel serverModel = sqLite.getServer(model.getServer_id());
            if (!serverModel.isEnable()){
                model.setStatus(-1);
                return model;
            }
            String reason = Config.getInstance().getBanMessage();
            String server = serverModel.getDisplay_name();
            reason = reason.replaceAll("\\{server\\}",server);
            reason = reason.replaceAll("\\{reason\\}",model.getReason());
            model.setKickReason(reason);
        }
        return model;
    }

    private void check(String server_name,boolean force){
        logger.info(Messages.startCheck);
        List<GitHubFileBean> list = gitHubApi.getServers();
        for (GitHubFileBean server : list){
            if (!server_name.equalsIgnoreCase("all") && !server_name.equalsIgnoreCase(server.getName())){
                continue;
            }
            checkServer(server,force);
        }
        logger.info(Messages.endCheck);
    }

    private void checkServer(GitHubFileBean server,boolean force){
        ServerModel model = sqLite.getServer(server.getName());
        if (model==null){
            model = new ServerModel();
            model.setSha("");
            model.setDisplay_name(gitHubApi.getServerName(server.getName()));
            model.setFolder(server.getPath());
            model.setName(server.getName());
            sqLite.insertServer(model);
            model = sqLite.getServer(server.getName());
            if (model==null){
                return;
            }
            logger.info(Messages.serverMessage(Messages.newServer,model));
            force = true;
        }
        if (force || !model.getSha().equalsIgnoreCase(server.getSha())){
            String display_name = gitHubApi.getServerName(server.getName());
            if (!display_name.equals("")){
                model.setDisplay_name(display_name);
            }
            model.setSha(server.getSha());

            List<GitHubFileBean> list = gitHubApi.getFileList(server.getName());
            boolean all_ok = true;
            for (GitHubFileBean file : list){
                boolean is_ok = checkFile(file,model,force);
                if (!is_ok){
                    all_ok = false;
                }
            }
            if (all_ok){
                sqLite.updateServer(model);
            }
        }
    }

    private boolean checkFile(GitHubFileBean file,ServerModel serverModel,boolean force){
        FilesModel model = sqLite.getFile(serverModel.getId(),file.getName());
        if (model==null){
            model = new FilesModel();
            model.setServer_id(serverModel.getId());
            model.setName(file.getName());
            model.setSha("");
            sqLite.insertFile(model);
            model = sqLite.getFile(serverModel.getId(),file.getName());
            if (model==null){
                return false;
            }
            force = true;
        }
        final boolean[] all_ok = {true};
        if (force || !model.getSha().equalsIgnoreCase(file.getSha())){
            final int[] i = {0};
            try {
                boolean finalForce = force;
                FilesModel finalModel = model;
                gitHubApi.sendGet(file.getUrl(), new InputStreamInterface() {
                    @Override
                    public void readLine(String line) {
                        if (!finalForce && i[0] < finalModel.getRead_line()){
                            return;
                        }
                        boolean is_ok = addBan(line,serverModel);
                        if (!is_ok){
                            all_ok[0] = false;
                        }
                        i[0] +=1;
                    }
                });
            } catch (Exception ignored) {

            }
            if (all_ok[0]){
                model.setRead_line(i[0]);
                model.setSha(file.getSha());
                sqLite.updateFile(model);
            }
        }
        return all_ok[0];
    }

    private boolean addBan(String line,ServerModel serverModel){
        String[] arr = line.split("\\s+");
        if (arr.length<2){
            return true;
        }
        String uuid_str = arr[0];
        String reason = arr[1];
        try {
            UUID uuid = UUID.fromString(uuid_str);
            BansModel old = sqLite.getBan(uuid);
            if (old!=null){
                return true;
            }
            BansModel model = new BansModel();
            model.setUuid(uuid);
            model.setReason(reason);
            model.setServer_id(serverModel.getId());
            boolean result = sqLite.insertBan(model);

            if (result){
                logger.info(Messages.newPlayerMessage(serverModel,line));
                model = getBanModel(uuid);
                banInterface.kickPlayer(uuid,model.getKickReason());
            }
            return result;
        }catch (IllegalArgumentException ignore){

        }
        return false;
    }
}

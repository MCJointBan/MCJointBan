package org.lintx.mcjointban.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.lintx.mcjointban.Messages;
import org.lintx.mcjointban.models.BansModel;
import org.lintx.mcjointban.models.ServerModel;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commands implements CommandExecutor {
    private MCJointBan jointBan;
    Commands(MCJointBan jointBan){
        this.jointBan = jointBan;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.hasPermission("jointban")){
            commandSender.sendMessage(Messages.noPermission);
            return true;
        }
        if (args.length==0 || args[0].equalsIgnoreCase("help")){
            sendHelp(commandSender);
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")){
            jointBan.reloadConfig();
            commandSender.sendMessage(Messages.reloadConfig);
        }else if (args[0].equalsIgnoreCase("check")){
            jointBan.getJointBan().check();
        }else if (args[0].equalsIgnoreCase("forced")){
            String server = "all";
            if (args.length>1){
                server = args[1];
                jointBan.getJointBan().check(server);
            }
        }else if (args[0].equalsIgnoreCase("server")){
            if (args.length>2){
                String server = args[1];
                String action = args[2];
                ServerModel model = jointBan.getJointBan().getServer(server);
                if (model==null){
                    commandSender.sendMessage(Messages.serverNotFind);
                }else {
                    if (action.equalsIgnoreCase("enable")){
                        model.setEnable(true);
                        commandSender.sendMessage(Messages.serverMessage(Messages.enableServer,model));
                    }else if (action.equalsIgnoreCase("disable")){
                        model.setEnable(false);
                        commandSender.sendMessage(Messages.serverMessage(Messages.disableServer,model));
                    }else {
                        sendHelp(commandSender);
                        return true;
                    }
                    jointBan.getJointBan().saveServer(model);
                }
            }
        }else if (args[0].equalsIgnoreCase("player")){
            if (args.length>2){
                String player = args[1];
                String action = args[2];
                UUID uuid = UUID.fromString(player);
                BansModel model = jointBan.getJointBan().getBanModel(uuid);
                if (model==null){
                    commandSender.sendMessage(Messages.playerNotFind);
                }else {
                    if (action.equalsIgnoreCase("enable")){
                        model.setEnable(true);
                        commandSender.sendMessage(Messages.playerMessage(Messages.enablePlayer,model));
                    }else if (action.equalsIgnoreCase("disable")){
                        model.setEnable(false);
                        commandSender.sendMessage(Messages.playerMessage(Messages.disablePlayer,model));
                    }else {
                        sendHelp(commandSender);
                        return true;
                    }
                    jointBan.getJointBan().savePlayer(model);
                }
            }
        }else if (args[0].equalsIgnoreCase("serverlist")){
            int page = 1;
            if (args.length>1){
                String p = args[1];
                Pattern pattern = Pattern.compile("\\d+");
                Matcher isNum = pattern.matcher(p);
                if (isNum.matches()){
                    page = Integer.parseInt(p);
                }
            }
            page = Math.max(0,page);
            List<ServerModel> serverModels = jointBan.getJointBan().getServerList(page);
            List<String> messages = Messages.serverListMessage(page,serverModels);
            for (String message:messages){
                commandSender.sendMessage(message);
            }
        }else {
            sendHelp(commandSender);
        }
        return true;
    }

    private void sendHelp(CommandSender commandSender){
        for (String help:Messages.helps){
            commandSender.sendMessage(help);
        }
    }
}

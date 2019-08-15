package org.lintx.mcjointban.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import org.lintx.mcjointban.Messages;
import org.lintx.mcjointban.models.BansModel;
import org.lintx.mcjointban.models.ServerModel;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commands extends Command {
    private MCJointBan jointBan;

    public Commands(MCJointBan jointBan,String name, String permission, String... aliases) {
        super(name, permission, aliases);
        this.jointBan = jointBan;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("jointban")){
            sender.sendMessage(new TextComponent(Messages.noPermission));
            return;
        }
        if (args.length==0 || args[0].equalsIgnoreCase("help")){
            for (String help:Messages.helps){
                sender.sendMessage(new TextComponent(help));
            }
            return;
        }
        if (args[0].equalsIgnoreCase("reload")){
            jointBan.reloadConfig();
            sender.sendMessage(new TextComponent(Messages.reloadConfig));
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
                    sender.sendMessage(new TextComponent(Messages.serverNotFind));
                }else {
                    if (action.equalsIgnoreCase("enable")){
                        model.setEnable(true);
                        sender.sendMessage(new TextComponent(Messages.serverMessage(Messages.enableServer,model)));
                    }else if (action.equalsIgnoreCase("disable")){
                        model.setEnable(false);
                        sender.sendMessage(new TextComponent(Messages.serverMessage(Messages.disableServer,model)));
                    }else {
                        sendHelp(sender);
                        return;
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
                    sender.sendMessage(new TextComponent(Messages.playerNotFind));
                }else {
                    if (action.equalsIgnoreCase("enable")){
                        model.setEnable(true);
                        sender.sendMessage(new TextComponent(Messages.playerMessage(Messages.enablePlayer,model)));
                    }else if (action.equalsIgnoreCase("disable")){
                        model.setEnable(false);
                        sender.sendMessage(new TextComponent(Messages.playerMessage(Messages.disablePlayer,model)));
                    }else {
                        sendHelp(sender);
                        return;
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
                sender.sendMessage(new TextComponent(message));
            }
        }else {
            sendHelp(sender);
        }
    }

    private void sendHelp(CommandSender commandSender){
        for (String help:Messages.helps){
            commandSender.sendMessage(new TextComponent(help));
        }
    }
}

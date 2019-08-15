package org.lintx.mcjointban;

import org.lintx.mcjointban.models.BansModel;
import org.lintx.mcjointban.models.ServerModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Messages {
    public static String noPermission = "§c权限不足";
    public static String reloadConfig = "§e配置已经重新加载";
    static String startCheck = "§e开始检查封禁列表更新";
    static String endCheck = "§e检查完毕";
    static String newServer = "§e检查到新服务器{server_name}({server})";
    public static String enableServer = "§e服务器{server_name}({server})提交的封禁记录已启用";
    public static String disableServer = "§e服务器{server_name}({server})提交的封禁记录已禁用";
    public static String serverNotFind = "§c指定的服务器不存在";
    public static String enablePlayer = "§e玩家{name}的封禁记录已启用";
    public static String disablePlayer = "§e玩家{name}的封禁记录已禁用";
    public static String playerNotFind = "§c指定的玩家没有被联合封禁";
    private static String newPlayer = "§e检查到服务器{server_name}({server})提交的新记录：{message}";
    private static String serverListHeader = "§e参与的服务器列表  第 {page} 页";
    private static String serverListContent = "§e{server_name}({server}) 是否启用该服记录：{status}";
    private static String serverStatusEnable = "§e启用";
    private static String serverStatusDisable = "§c禁用";

    public static List<String> helps = Arrays.asList(
            "§eMCJointBan(Minecraft联合封禁)帮助",
            "§e以下内容，§b<>§e内的表示必选参数，§b[]§e内的表示可选参数，参数中以§b/§e分隔的表示只能从中选择一项",
            "§e其中，§bserver§e是对应的服务器名（serverlist子命令返回的结果中，括号里面的部分），§bpage§e是页码（数字，大于0），§bplayer_uuid§e是玩家的UUID",
            "§b/jointban help§e 显示本帮助",
            "§b/jointban reload§e 重新加载配置",
            "§b/jointban check§e 手动检查封禁列表更新",
            "§b/jointban forced [all/server]§e 强制检查所有/特定服务器提交的封禁列表（忽略历史数据）",
            "§b/jointban player <player_uuid> <enable/disable>§e 启用/禁用特定玩家的封禁记录",
            "§b/jointban server <server> <enable/disable>§e 启用/禁用特定服务器提交的封禁列表（禁用后依然会更新该服务器封禁列表，但是该服务器提交的封禁列表内的玩家均做放行处理）",
            "§b/jointban serverlist [page]§e 显示参与的服务器列表"
    );

    public static String serverMessage(String message, ServerModel model){
        message = message.replaceAll("\\{server\\}",model.getName());
        message = message.replaceAll("\\{server_name\\}",model.getDisplay_name());
        return message;
    }

    public static String playerMessage(String message, BansModel model){
        message = message.replaceAll("\\{name\\}",model.getUuid().toString());
        return message;
    }

    static String newPlayerMessage(ServerModel model,String reason){
        String message = newPlayer;
        message = serverMessage(message,model);
        message = message.replaceAll("\\{message\\}",reason);
        return message;
    }

    public static List<String> serverListMessage(int page,List<ServerModel> list){
        List<String> messages = new ArrayList<>();
        messages.add(serverListHeader.replaceAll("\\{page\\}","" + page));
        for (ServerModel model : list){
            String message = serverMessage(serverListContent,model);
            message = message.replaceAll("\\{status\\}",model.isEnable()?serverStatusEnable:serverStatusDisable);
            messages.add(message);
        }
        return messages;
    }
}

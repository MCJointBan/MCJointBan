package org.lintx.mcjointban.github;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GitHubApi {
    private final String contents_url;
    private final Gson gson = new Gson();

    public GitHubApi(String name, String repository){
        this.contents_url = "https://api.github.com/repos/" + name + "/" + repository + "/contents";
    }

    public void sendGet(String url,InputStreamInterface streamInterface) throws Exception {
        String result = "";

        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent","MCJointBan");

        int responseCode = conn.getResponseCode();

        if (responseCode==200){
            readLine(conn.getInputStream(),streamInterface);
        }
    }

    private String getSendBody(String url){
        StringBuilder response = new StringBuilder();
        try {
            sendGet(url, response::append);
        } catch (Exception ignored) {

        }
        return response.toString();
    }

    private void readLine(InputStream is,InputStreamInterface streamInterface){
        if (is==null){
            return;
        }
        int i = 0;

        try {
            Reader r = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(r);
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
               streamInterface.readLine(inputLine);
            }
            br.close();
            r.close();
        } catch (IOException ignored) {

        }finally {
            try {
                is.close();
            } catch (IOException ignored) {

            }
        }
    }

    private List<GitHubFileBean> getFileLists(String url){
        try {
            String body = getSendBody(url);

            return gson.fromJson(body,new TypeToken<List<GitHubFileBean>>(){}.getType());
        } catch (Exception ignored) {

        }
        return new  ArrayList<>();
    }

    public List<GitHubFileBean> getServers(){
        List<GitHubFileBean> files = getFileLists(this.contents_url);
        List<GitHubFileBean> result = new ArrayList<>();
        for (GitHubFileBean f : files){
            if (f.getType().equalsIgnoreCase("dir")){
                result.add(f);
            }
        }
        return result;
    }

    public List<GitHubFileBean> getFileList(String server){
        List<GitHubFileBean> files = getFileLists(this.contents_url + "/" + server);
        List<GitHubFileBean> result = new ArrayList<>();
        for (GitHubFileBean f : files){
            if (f.getType().equalsIgnoreCase("file") && !f.getName().equalsIgnoreCase("server.txt") && f.getName().toLowerCase(Locale.ROOT).endsWith(".txt")){
                result.add(f);
            }
        }
        return result;
    }

    public String getServerName(String server){
        try {
            String body = getSendBody(this.contents_url + "/" + server + "/server.txt");
            GitHubFileBean c = gson.fromJson(body,new TypeToken<GitHubFileBean>(){}.getType());
            if (c.getUrl()!=null && !c.getUrl().equals("")){
                body = getSendBody(c.getUrl());
                if (!body.equals("")){
                    int maxServerNameLength = 20;
                    if (body.length()> maxServerNameLength){
                        body = body.substring(0, maxServerNameLength);
                    }
                    return body;
                }
            }
        } catch (Exception ignored) {

        }
        return "";
    }
}

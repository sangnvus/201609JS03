package com.favn.firstaid.commons;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by Kienmt on 11/5/2016.
 */

public class Question {
    private String injury_id;
    private String asker;
    private String asker_email;
    private String title;
    private String content;

    public Question(String injury_id, String asker, String asker_email, String title, String content) {
        this.injury_id = injury_id;
        this.asker = asker;
        this.asker_email = asker_email;
        this.title = title;
        this.content = content;
    }

    public Question() {
        this.injury_id = "";
        this.asker = "";
        this.asker_email = "";
        this.title = "";
        this.content = "";
    }

    public String getInjury_id() {
        return injury_id;
    }

    public void setInjury_id(String injury_id) {
        this.injury_id = injury_id;
    }

    public String getAsker() {
        return asker;
    }

    public void setAsker(String asker) {
        this.asker = asker;
    }

    public String getAsker_email() {
        return asker_email;
    }

    public void setAsker_email(String asker_email) {
        this.asker_email = asker_email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Pack data to send to server
    public String packData() {
        JSONObject jo = new JSONObject();
        StringBuffer sb = new StringBuffer();

        try {
            jo.put("injury_id", injury_id);
            jo.put("asker", asker);
            jo.put("asker_email", asker_email);
            jo.put("title", title);
            jo.put("content", content);

            Boolean isFirstValue = true;
            Iterator it = jo.keys();

            do {
                String key = it.next().toString();
                String value = jo.get(key).toString();

                if(isFirstValue) {
                    isFirstValue = false;
                } else {
                    sb.append("&");
                }

                sb.append(URLEncoder.encode(key, "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } while (it.hasNext());

            return sb.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}

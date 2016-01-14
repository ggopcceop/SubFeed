/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kime.subfeed;

/**
 *
 * @author Kime
 */
public class Feed {

    public String title;
    public String sid;
    public String description;
    public String language;
    public String group;
    public String downloadCount;

    public Feed(String title, String sid, String description, String language, String group, String downloadCount) {
        this.title = title;
        this.sid = sid;
        this.description = description;
        this.language = language;
        this.group = group;
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<HTML><table width='400px'><tr><td width='300px'><div>");
        sb.append(title);
        sb.append("</div></td><td width='100px'><div>");
        sb.append(group);
        sb.append("</div></td></tr><tr><td colspan=2><p>");
        sb.append(description);
        sb.append("</p></td></tr><tr><td><p>");
        sb.append(language);
        sb.append("</p></td></tr></table><br></HTML>");
        return sb.toString();
    }
}

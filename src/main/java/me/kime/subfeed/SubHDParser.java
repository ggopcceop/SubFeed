/* 
 * The MIT License
 *
 * Copyright 2016 Kime.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package me.kime.subfeed;

import java.io.BufferedReader;
import me.kime.subfeed.ui.util.FeedNode;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Kime
 */
public class SubHDParser {

    public static List<FeedNode> parse(String mediaName) {
        Elements entries = SubHDParser.search(mediaName);
        if (entries == null || entries.isEmpty()) {
            LinkedList list = new LinkedList();
            list.add(new FeedNode("Not Found", "", "", "", "", ""));
            return list;
        } else {
            LinkedList list = new LinkedList();
            entries.forEach((entry) -> {
                String title = SubHDParser.parseTitle(entry);
                String sid = SubHDParser.parseSubId(entry);
                String description = SubHDParser.parseDescription(entry);
                String language = SubHDParser.parseLanguage(entry);
                String group = SubHDParser.parseGroup(entry);
                String downloadCount = SubHDParser.parseDownloadCount(entry);
                System.out.println(title + " " + sid + " " + description + " " + language + " " + group + " " + downloadCount);
                list.add(new FeedNode(title, sid, description, language, group, downloadCount));
            });
            return list;
        }
    }

    public static Elements search(String mediaName) {
        try {
            mediaName = StringUtils.replaceEach(mediaName,
                    new String[]{"&", "'", "[", "]"},
                    new String[]{"%26", "%27", "%5B", "%5D"});
            System.out.println("Searching " + mediaName);
            Document doc = Jsoup.connect("http://subhd.com/search/" + mediaName).get();
            Element context = doc.select("div.col-md-9").first();
            return context.select("div.box");
        } catch (IOException ex) {
            Logger.getLogger(SubHDParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String parseSubId(Element e) {
        Element a = e.select("div.pull-left.lb_r a").first();
        if (a != null) {
            return a.attr("href").substring(4);
        }
        return "";
    }

    public static String parseTitle(Element e) {
        Element a = e.select("div.pull-left.lb_r a").first();
        if (a != null) {
            return a.text();
        }
        return "";
    }

    public static String parseGroup(Element e) {
        Element a = e.select("div.pull-left.lb_r div.d_zu a").first();
        if (a != null) {
            return a.text();
        }
        return "";
    }

    public static String parseDescription(Element e) {
        Element a = e.select("div.pull-left.lb_r div.d_title a").first();
        if (a != null) {
            return a.text();
        }
        return "";
    }

    public static String parseDownloadCount(Element e) {
        return "";
    }

    public static String parseLanguage(Element e) {
        Elements a = e.select("div.pull-left.lb_r > span.label");

        return a.text();
    }

    public static String parseDownLink(String sid) {
        try {
            String data = "sub_id=" + sid;
            byte[] postData = data.getBytes(UTF_8);
            int postDataLength = postData.length;

            HttpURLConnection conn = (HttpURLConnection) new URL("http://subhd.com/ajax/down_ajax").openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "UTF-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            conn.getOutputStream().write(postData);

            StringBuilder textBuilder = new StringBuilder();

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            int c;
            while ((c = in.read()) != -1) {
                textBuilder.append((char) c);
            }

            JSONObject json = (JSONObject) JSONValue.parse(textBuilder.toString());
            String downloadURL = (String) json.get("url");

            conn.disconnect();
            return downloadURL;
        } catch (IOException ex) {
            Logger.getLogger(SubFeed.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}

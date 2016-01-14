/*
 * Copyright 2015 Kime.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.kime.subfeed;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Kime
 */
public class SubHDParser {

    public static Elements Search(String mediaName) {
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
            return a.attr("href").substring(3);
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
}

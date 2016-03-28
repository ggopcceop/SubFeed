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

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import net.sf.sevenzipjbinding.util.ByteArrayStream;

/**
 *
 * @author Kime
 */
public class SubFeed {

    public static List<ISimpleInArchiveItem> downloadSub(String url) {
        LinkedList<ISimpleInArchiveItem> list = new LinkedList<>();
        try {
            URL inFile = new URL(url);
            URLConnection conn = inFile.openConnection();
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);
            conn.setRequestProperty("Cache-Control", "no-store,max-age=0,no-cache");
            conn.setRequestProperty("Expires", "0");
            conn.setRequestProperty("Pragma", "no-cache");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);

            IInArchive inArchive;
            try (ByteArrayStream buffer = new ByteArrayStream(1024 * 1024 * 64)) {
                buffer.writeFromInputStream(conn.getInputStream(), true);
                inArchive = SevenZip.openInArchive(null, buffer);
            }

            System.out.println("Count of items in archive: " + inArchive.getNumberOfItems());

            ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();

            for (ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
                if (item.getPath().endsWith(".ass") || item.getPath().endsWith(".srt")) {
                    System.out.println(item.getPath());

                    list.add(item);

                }
            }

        } catch (IOException ex) {
            Logger.getLogger(SubFeed.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static String parseLanguage(String subName, String mediaName) {
        if ("".equals(mediaName)) {
            return subName;
        }
        String[] indexLang = {"chi", "yue", "eng"};
        String[][] tokens
                = {{"zh-s", "chs", "简体"},
                {"zh-t", "cht", "繁体"},
                {"eng", "英文"}};
        ArrayList<Node> list = new ArrayList<>();
        for (int i = 0; i < indexLang.length; i++) {
            int index = indexof(subName, tokens[i]);
            if (index != -1) {
                list.add(new Node(index, indexLang[i]));
            }
        }

        Collections.sort(list);

        String extention = "";

        int pos = subName.lastIndexOf(".");
        if (pos > 0) {
            extention = subName.substring(pos + 1);
        }

        StringBuilder sb = new StringBuilder();

        sb.append(mediaName);
        sb.append(".");

        if (list.isEmpty()) {
            sb.append("unk");
            sb.append(".");
        } else {
            list.forEach((token) -> {
                sb.append(token.code).append(".");
            });
        }

        sb.append(extention);

        return sb.toString();
    }

    private static int indexof(String name, String[] tokens) {
        for (String token : tokens) {
            int i = name.lastIndexOf(token);
            if (i != -1) {
                return i;
            }
        }
        return -1;
    }
}

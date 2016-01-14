
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kime.subfeed;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileOutStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import net.sf.sevenzipjbinding.util.ByteArrayStream;
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
public class SubFeed {


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Missing input parameter");
            return;
        }
        try {
            File mediaFile = new File(args[0]);

            int pos = mediaFile.getName().lastIndexOf(".");
            String fileName = "";
            if (pos > 0) {
                fileName = mediaFile.getName().substring(0, pos);

            }
            
            System.out.println(args[0]);
            System.out.println(mediaFile.getParent());
            System.out.println(mediaFile.getName());

            Document doc = Jsoup.connect("http://subhd.com/search/" + fileName).get();
            Element context = doc.select("div.col-md-9").first();
            Elements entries = context.select("div.box");
            Element entry = entries.first();
            String dlLink = parseDownLink(SubHDParser.parseSubId(entry));
            System.out.println(dlLink);
            List<ISimpleInArchiveItem> list = downloadSub(dlLink);

            for (ISimpleInArchiveItem item : list) {
                String newName = parseLanguage(item.getPath(), fileName);

                File file = new File(mediaFile.getParent() + File.separator + newName);

                System.out.println(file.getAbsoluteFile());

                ExtractOperationResult result = item.extractSlow(new RandomAccessFileOutStream(new RandomAccessFile(file, "rw")));

                System.out.println(result.toString());
            }


        } catch (IOException ex) {
            Logger.getLogger(SubFeed.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            return downloadURL;
        } catch (IOException ex) {
            Logger.getLogger(SubFeed.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static List<ISimpleInArchiveItem> downloadSub(String url){
        LinkedList<ISimpleInArchiveItem> list = new LinkedList<ISimpleInArchiveItem>();
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

            ByteArrayStream buffer = new ByteArrayStream(1024 * 1024 * 64);
            buffer.writeFromInputStream(conn.getInputStream(), true);
            IInArchive inArchive = SevenZip.openInArchive(null, // autodetect archive type
                    buffer);

            System.out.println("Count of items in archive: "
                    + inArchive.getNumberOfItems());

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
        String[] indexLang = {"chi", "yue", "eng"};
        String[][] tokens
                = {{"zh-s", "chs", "简体"},
                {"zh-t", "cht", "繁体"},
                {"eng", "英文"}};
        ArrayList<Node> list = new ArrayList<Node>();
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
            for (Node token : list) {
                sb.append(token.code);
                sb.append(".");
            }
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

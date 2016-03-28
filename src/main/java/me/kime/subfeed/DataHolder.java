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

import java.io.File;

/**
 *
 * @author Kime
 */
public class DataHolder {

    private static String searchText = "";
    private static String path = "";
    private static String mediaName = "";
    private static String downloadId = "";

    public static void parseString(File file) {
        int pos = file.getName().lastIndexOf(".");
        if (pos > 0) {
            mediaName = file.getName().substring(0, pos);
            searchText = mediaName;
        }
        path = file.getParent();
        System.out.println(file.getParent());
        System.out.println(file.getName());
    }

    public static void setSearchText(String text) {
        searchText = text;
    }

    public static String getSearchText() {
        return searchText;
    }

    public static String getMediaName() {
        return mediaName;
    }

    public static String getPath() {
        return path;
    }

    public static String getDownloadId() {
        return downloadId;
    }

    public static void setDownloadId(String id) {
        downloadId = id;
    }
}

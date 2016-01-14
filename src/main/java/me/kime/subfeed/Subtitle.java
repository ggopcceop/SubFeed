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

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;

/**
 *
 * @author Kime
 */
public class Subtitle {

    public String mediaName;
    public String fileName;
    public ISimpleInArchiveItem item;

    public Subtitle(String n, ISimpleInArchiveItem i) {
        mediaName = n;
        item = i;
        
        try {
            
            fileName = i.getPath();
            int index = fileName.lastIndexOf("\\");
            fileName = fileName.substring(index+1);
        } catch (SevenZipException ex) {
            Logger.getLogger(Subtitle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        return "<HTML><p>" + fileName + "</p></HTML>";
    }
}

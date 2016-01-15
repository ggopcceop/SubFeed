/*
 * Copyright 2016 Kime.
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kime
 */
public class ContextkMenu {

    public static void addWindowsContextkMenuMenu() {
        try {
            String javaPath = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";

            String appPath = new File(ContextkMenu.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getAbsolutePath();

            javaPath = javaPath.replace("\\", "\\\\");
            appPath = appPath.replace("\\", "\\\\");

            System.out.println(javaPath + " -jar " + appPath);

            //file to register windows context menu item
            File regFile = File.createTempFile("regimport", ".reg");
            PrintWriter pw = new PrintWriter(regFile);
            pw.println("Windows Registry Editor Version 5.00");
            pw.println();
            pw.println("[HKEY_CLASSES_ROOT\\*\\shell\\Subtitle]");
            pw.println("@=\"Download Subtitle\"");
            pw.println();
            pw.println("[HKEY_CLASSES_ROOT\\*\\shell\\Subtitle\\command]");
            pw.println("@=\"\\\"" + javaPath + "\\\" -jar \\\"" + appPath + "\\\" \\\"%1\\\"\"");
            pw.close();
            
            //@Anders's workround to display the Windows UAC dialog 
            File batFile = File.createTempFile("UAC", ".bat");
            pw = new PrintWriter(batFile);
            pw.println("@if (1==1) @if(1==0) @ELSE");
            pw.println("@echo off&SETLOCAL ENABLEEXTENSIONS");
            pw.println(">nul 2>&1 \"%SYSTEMROOT%\\system32\\cacls.exe\" \"%SYSTEMROOT%\\system32\\config\\system\"||(");
            pw.println("    cscript //E:JScript //nologo \"%~f0\"");
            pw.println("    @goto :EOF");
            pw.println(")");
            pw.println("reg import " +  regFile.getAbsolutePath());
            pw.println("goto :EOF");
            pw.println("@end @ELSE");
            pw.println("ShA=new ActiveXObject(\"Shell.Application\")");
            pw.println("ShA.ShellExecute(\"cmd.exe\",\"/c \\\"\"+WScript.ScriptFullName+\"\\\"\",\"\",\"runas\",5);");
            pw.println("@end");
            pw.close();

            System.out.println(regFile.getAbsolutePath());
            
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", batFile.getAbsolutePath());
            pb.redirectOutput(Redirect.INHERIT);
            pb.redirectError(Redirect.INHERIT);
            pb.start();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ContextkMenu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ContextkMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void removeRightClickMenu() {

    }
}

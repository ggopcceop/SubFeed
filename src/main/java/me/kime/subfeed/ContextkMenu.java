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
import java.io.IOException;
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
            //using java instead of javaw for debuging
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

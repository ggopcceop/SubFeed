/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kime.subfeed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Kime
 */
public class SubWindow extends javax.swing.JFrame {

    public static String mediaName;
    public static String searchText;
    public static String path;

    /**
     * Creates new form SubWindow
     */
    public SubWindow() {
        initComponents();

        setPane(new Select1());
    }

    public static void setPane(JPanel panel) {
        jScrollPane1.setViewportView(panel);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new JScrollPane();
        jTextField1 = new JTextField();
        JButton jButton1 = new JButton();
        JMenuBar jMenuBar1 = new JMenuBar();
        JMenu jMenu1 = new JMenu();
        JMenu jMenu2 = new JMenu();
        JMenuItem jMenuItem1 = new JMenuItem();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jScrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTextField1.setText(searchText);

        jButton1.setText("Search");
        jButton1.setToolTipText("");
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Options");

        jMenuItem1.setText("Register Right Click Menu");
        jMenuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 567, GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 468, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 550, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        searchText = jTextField1.getText();
        Select1.clearItems();
        setPane(new Select1());

        Elements entries = SubHDParser.Search(searchText);

        if (entries == null || entries.size() == 0) {
            Select1.addItem(new Feed("Not Found", "", "", "", "", ""));
        } else {
            for (Element entry : entries) {
                String title = SubHDParser.parseTitle(entry);
                String sid = SubHDParser.parseSubId(entry);
                String description = SubHDParser.parseDescription(entry);
                String language = SubHDParser.parseLanguage(entry);
                String group = SubHDParser.parseGroup(entry);
                String downloadCount = SubHDParser.parseDownloadCount(entry);
                System.out.println(title + " " + sid + " " + description + " " + language + " " + group + " " + downloadCount);
                Select1.addItem(new Feed(title, sid, description, language, group, downloadCount));

            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            /*for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }*/
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SubWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SubWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SubWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SubWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */

        if (args.length < 1) {
            System.out.println("Missing input parameter");
            return;
        }
        File mediaFile = new File(args[0]);

        int pos = mediaFile.getName().lastIndexOf(".");
        if (pos > 0) {
            mediaName = mediaFile.getName().substring(0, pos);
            searchText = mediaName;
        }

        path = mediaFile.getParent();

        System.out.println(args[0]);
        System.out.println(mediaFile.getParent());
        System.out.println(mediaFile.getName());

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SubWindow().setVisible(true);

                new Thread() {
                    @Override
                    public void run() {
                        System.out.println("Test");

                        Elements entries = SubHDParser.Search(searchText);
                        if (entries == null || entries.size() == 0) {
                            Select1.addItem(new Feed("Not Found", "", "", "", "", ""));
                        } else {
                            for (Element entry : entries) {
                                String title = SubHDParser.parseTitle(entry);
                                String sid = SubHDParser.parseSubId(entry);
                                String description = SubHDParser.parseDescription(entry);
                                String language = SubHDParser.parseLanguage(entry);
                                String group = SubHDParser.parseGroup(entry);
                                String downloadCount = SubHDParser.parseDownloadCount(entry);
                                System.out.println(title + " " + sid + " " + description + " " + language + " " + group + " " + downloadCount);
                                Select1.addItem(new Feed(title, sid, description, language, group, downloadCount));
                            }
                        }

                    }
                }.start();
            }

        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    static JScrollPane jScrollPane1;
    JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}

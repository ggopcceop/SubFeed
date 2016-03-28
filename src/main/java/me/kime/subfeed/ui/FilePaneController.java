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
package me.kime.subfeed.ui;

import me.kime.subfeed.ui.util.FileNode;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.DosFileAttributes;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import me.kime.subfeed.DataHolder;
import me.kime.subfeed.Preference;

/**
 * FXML Controller class
 *
 * @author Kime
 */
public class FilePaneController implements Initializable {

    private final DirectoryChooser dirChooser = new DirectoryChooser();

    @FXML
    private TextField dirField;
    @FXML
    private Button dirButton;
    @FXML
    private ListView<FileNode> fileListView;

    private ObservableList fileList;
    private SubWindowController parent;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileList = FXCollections.observableList(new LinkedList());
        fileListView.setItems(fileList);

        fileListView.setCellFactory(e -> {
            ListCell cell = new TextFieldListCell();
            cell.setOnMouseClicked(this::handleDirClick);
            return cell;
        });

        dirField.setOnKeyPressed(this::handleDirField);

        String filePath = DataHolder.getPath();
        if (!"".equals(filePath)) {
            browseFile(new File(filePath));
        }
    }

    @FXML
    private void handleDirButtonAction(ActionEvent event) {
        System.out.println("You clicked Dir!");
        dirChooser.setInitialDirectory(new File(Preference.getLastUsedDir()));
        File Selectedfile = dirChooser.showDialog(dirButton.getScene().getWindow());        
        
        if (Selectedfile != null) {
            System.out.println("Selected file: " + Selectedfile.getAbsolutePath());
            browseFile(Selectedfile);
        }
    }

    private void handleDirClick(MouseEvent event) {
        if (event.getClickCount() == 2 && MouseButton.PRIMARY.equals(event.getButton())) {
            TextFieldListCell source = (TextFieldListCell) event.getSource();
            if (source != null) {
                System.out.println("clicked on " + event.getSource());
                FileNode data = (FileNode) source.getItem();
                if (data != null) {
                    System.out.println("file on " + data.file.getAbsolutePath());
                    browseFile(data.file);
                }
            }

        }
    }

    private void handleDirField(KeyEvent event) {
        if (KeyCode.ENTER.equals(event.getCode())) {
            String pathString = dirField.getText();
            File f = new File(pathString);
            if (f.exists()) {
                browseFile(f);
            }
        }
    }

    private void browseFile(File file) {
        if (file.isDirectory()) {
            fileList.clear();
            Preference.setLastUsedDir(file.toString());
            for (File f : file.listFiles(f -> {
                try {
                    DosFileAttributes attr = Files.readAttributes(f.toPath(), DosFileAttributes.class);
                    return !attr.isHidden() || !attr.isSystem();
                } catch (IOException ex) {
                    return true;
                }
            })) {
                fileList.add(new FileNode(f));
            }

            dirField.setText(file.getAbsolutePath());
        } else {
            DataHolder.parseString(file);
            parent.getSearchPaneControllerController().search();
            parent.siwchPane(1);
        }

    }

    public void setParentController(SubWindowController parent) {
        this.parent = parent;
    }

}

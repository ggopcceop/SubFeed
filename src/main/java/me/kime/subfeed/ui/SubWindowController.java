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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import me.kime.subfeed.DataHolder;

/**
 * FXML Controller class
 *
 * @author Kime
 */
public class SubWindowController implements Initializable {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab fileTab;

    @FXML
    private Tab searchTab;

    @FXML
    private Tab subtitleTab;
    
    @FXML
    private Tab optionTab;

    @FXML
    private ProgressBar porgressBar;

    private SubtitlePaneController subtitlePaneControllerController;
    private SearchPaneController searchPaneControllerController;
    private FilePaneController filePaneControllerController;
    private OptionPaneController optionPaneControllerController;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupFilePane();
        setupSearchPane();
        setupSubtitlePane();
        setupOptionPane();

        if (!"".equals(DataHolder.getSearchText())) {
            siwchPane(1);
        }
    }

    private void setupFilePane() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FilePane.fxml"), ResourceBundle.getBundle("me.kime.subfeed.Bundle"));
            AnchorPane filePane = (AnchorPane) fxmlLoader.load();

            filePaneControllerController = (FilePaneController) fxmlLoader.getController();
            filePaneControllerController.setParentController(this);

            fileTab.setContent(filePane);
        } catch (IOException ex) {
            Logger.getLogger(SubWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setupSearchPane() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SearchPane.fxml"), ResourceBundle.getBundle("me.kime.subfeed.Bundle"));
            AnchorPane searchPane = (AnchorPane) fxmlLoader.load();

            searchPaneControllerController = (SearchPaneController) fxmlLoader.getController();
            searchPaneControllerController.setParentController(this);

            searchTab.setContent(searchPane);
        } catch (IOException ex) {
            Logger.getLogger(SubWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setupSubtitlePane() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SubtitlePane.fxml"), ResourceBundle.getBundle("me.kime.subfeed.Bundle"));
            AnchorPane subtitlePane = (AnchorPane) fxmlLoader.load();

            subtitlePaneControllerController = (SubtitlePaneController) fxmlLoader.getController();
            subtitlePaneControllerController.setParentController(this);

            subtitleTab.setContent(subtitlePane);
        } catch (IOException ex) {
            Logger.getLogger(SubWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setupOptionPane() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OptionPane.fxml"), ResourceBundle.getBundle("me.kime.subfeed.Bundle"));
            AnchorPane optionPane = (AnchorPane) fxmlLoader.load();

            optionPaneControllerController = (OptionPaneController) fxmlLoader.getController();
            optionPaneControllerController.setParentController(this);

            optionTab.setContent(optionPane);
        } catch (IOException ex) {
            Logger.getLogger(SubWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public SubtitlePaneController getSubtitlePaneControllerController() {
        return subtitlePaneControllerController;
    }

    public SearchPaneController getSearchPaneControllerController() {
        return searchPaneControllerController;
    }

    public FilePaneController getFilePaneControllerController() {
        return filePaneControllerController;
    }

    public void siwchPane(int index) {
        tabPane.getSelectionModel().select(index);
    }

    public void setProgress(double progress) {
        porgressBar.setProgress(progress);
    }
}

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

import me.kime.subfeed.ui.util.FeedNode;
import me.kime.subfeed.ui.util.Util;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import me.kime.subfeed.DataHolder;
import me.kime.subfeed.SubHDParser;

/**
 * FXML Controller class
 *
 * @author Kime
 */
public class SearchPaneController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private VBox result;

    private SubWindowController parent;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String searchText = DataHolder.getSearchText();
        if (!"".equals(searchText)) {
            searchField.setText(searchText);
            search();
        }

    }

    @FXML
    private void handleSearchButtonAction(ActionEvent event) {
        System.out.println("You clicked Search!");

        DataHolder.setSearchText(searchField.getText());
        search();
    }

    private void setupPane(List<FeedNode> list) {
        result.getChildren().clear();
        list.forEach(node -> {
            try {
                FXMLLoader loader = new FXMLLoader(this.getClass().getResource("SearchResult.fxml"));
                AnchorPane resultPane = loader.load();
                SearchResultController controller = loader.getController();

                controller.setParentController(parent);
                controller.setNode(node);

                StringBuilder sb = new StringBuilder();
                if (!"".equals(node.group)) {
                    sb.append(node.group).append("|");
                }
                sb.append(node.title);

                TitledPane t = new TitledPane(sb.toString(), resultPane);

                t.setMinWidth(200d);

                System.out.println("Add TitledPane " + node.title + " to Accordion");

                result.getChildren().add(t);

            } catch (IOException ex) {
                Logger.getLogger(SearchPaneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void search() {
        result.getChildren().clear();

        String searchText = DataHolder.getSearchText();
        searchField.setText(searchText);

        Task<List<FeedNode>> task = Util.task(() -> {
            parent.setProgress(-1d);
            return SubHDParser.parse(searchText);
        });

        task.setOnSucceeded(e -> {
            List<FeedNode> list = (List<FeedNode>) e.getSource().getValue();
            setupPane(list);

            parent.setProgress(0d);
        });

        Util.start(task);
    }

    public void setParentController(SubWindowController parent) {
        this.parent = parent;
    }

}

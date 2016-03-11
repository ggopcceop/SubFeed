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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import me.kime.subfeed.DataHolder;

/**
 * FXML Controller class
 *
 * @author Kime
 */
public class SearchResultController implements Initializable {

    @FXML
    private VBox container;

    @FXML
    private TextField title;
    @FXML
    private TextField group;
    @FXML
    private TextField description;
    @FXML
    private TextField language;

    private FeedNode node;
    private SubWindowController parent;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        container.addEventFilter(MouseEvent.MOUSE_CLICKED, this::handleAction);
    }

    public void handleAction(MouseEvent event) {
        if (event.getClickCount() == 2 && MouseButton.PRIMARY.equals(event.getButton())) {
            System.out.println("clicked on " + event.getSource());

            DataHolder.setDownloadId(node.sid);
            parent.getSubtitlePaneControllerController().fetchSubtitle();
            parent.siwchPane(2);
        }
    }

    public void setNode(FeedNode node) {
        this.node = node;
        title.setText(node.title);
        group.setText(node.group);
        description.setText(node.description);
        language.setText(node.language);
    }

    public FeedNode getNode() {
        return node;
    }

    public void setParentController(SubWindowController parent) {
        this.parent = parent;
    }
}

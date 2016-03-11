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

import me.kime.subfeed.ui.util.SubtitleNode;
import me.kime.subfeed.ui.util.Util;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import me.kime.subfeed.DataHolder;
import me.kime.subfeed.SubFeed;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.impl.RandomAccessFileOutStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;

/**
 * FXML Controller class
 *
 * @author Kime
 */
public class SubtitlePaneController implements Initializable {

    private final FileChooser fileChooser = new FileChooser();

    @FXML
    private TextField saveNameField;

    @FXML
    private Button saveButton;

    @FXML
    private ListView<SubtitleNode> subtitleListView;

    private ObservableList<SubtitleNode> subtitleList;
    private SubWindowController parent;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        subtitleList = FXCollections.observableList(new LinkedList());
        subtitleListView.setItems(subtitleList);

        subtitleListView.setCellFactory(e -> {
            ListCell cell = new TextFieldListCell();
            cell.setOnMouseClicked(this::handleSubtitleClick);
            return cell;
        });

        fetchSubtitle();
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        System.out.println("You clicked Save!");
        SubtitleNode node = (SubtitleNode) subtitleListView.getSelectionModel().getSelectedItem();
        saveSubtitle(node.item);
    }

    private void handleSubtitleClick(MouseEvent event) {
        if (MouseButton.PRIMARY.equals(event.getButton())) {
            TextFieldListCell source = (TextFieldListCell) event.getSource();
            SubtitleNode node = (SubtitleNode) source.getItem();
            if (node == null) {
                return;
            }
            if (event.getClickCount() == 1) {
                String subName = SubFeed.parseLanguage(node.fileName, DataHolder.getMediaName());
                saveNameField.setText(subName);
            } else if (event.getClickCount() == 2) {
                System.out.println("clicked on " + subtitleListView.getSelectionModel().getSelectedItems());
                saveSubtitle(node.item);
            }
        }

    }

    public void setParentController(SubWindowController parent) {
        this.parent = parent;
    }

    private void saveSubtitle(ISimpleInArchiveItem item) {
        String saveName = saveNameField.getText();
        String savePath = DataHolder.getPath();
        File outFile;
        if ("".equals(savePath)) {
            fileChooser.setInitialFileName(saveName);
            outFile = fileChooser.showSaveDialog(saveButton.getScene().getWindow());
        } else {
            outFile = new File(savePath + File.separator + saveName);
        }

        Task<Void> task = Util.task(() -> {

            System.out.println(outFile.getAbsoluteFile());

            RandomAccessFileOutStream fileStream = new RandomAccessFileOutStream(new RandomAccessFile(outFile, "rw"));

            //TODO handle extract result
            ExtractOperationResult result = item.extractSlow(fileStream);

            fileStream.close();

            System.out.println(result.toString());

            return null;
        });

        Util.start(task);
    }

    public void fetchSubtitle() {
        subtitleList.clear();
        if (!"".equals(DataHolder.getDownloadId())) {
            Task<List<ISimpleInArchiveItem>> task = Util.task(() -> {
                String dlLink = SubFeed.parseDownLink(DataHolder.getDownloadId());
                return SubFeed.downloadSub(dlLink);
            });

            task.setOnSucceeded(e -> {
                List<ISimpleInArchiveItem> list = (List<ISimpleInArchiveItem>) e.getSource().getValue();
                list.forEach((item) -> {
                    subtitleList.add(new SubtitleNode("", item));
                });
            });

            Util.start(task);
        }
    }
}

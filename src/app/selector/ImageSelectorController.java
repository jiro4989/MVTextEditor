package app.selector;

import java.io.IOException;
import java.nio.file.*;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class ImageSelectorController {

  // fxml component//{{{

  @FXML private SplitPane splitPane;

  @FXML private TitledPane listViewTitledPane;
  @FXML private ListView<String> listView;

  @FXML private TitledPane imageViewTitledPane;
  @FXML private GridPane parentGridPane;
  @FXML private ImageView imageView;
  @FXML private GridPane focusGridPane;

  //}}}

  @FXML private void initialize() {
    // TODO 一時的な設定
    Path path = Paths.get("c:", "rpg", "Project1", "img", "faces");
    FileVisitor<Path> visitor = new MyFileVisitor(this);
    try {
      Files.walkFileTree(path, visitor);
    } catch (IOException e) {
      e.printStackTrace();
    }

    listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
      Path newPath = Paths.get(path.toString(), newVal);
      setImage(newPath.toString());
    });
  }

  void setImage(String path) {//{{{
    Image img = new Image("file:" + path);
    int width  = (int) img.getWidth();
    int height = (int) img.getHeight();

    imageView.setImage(img);
    imageView.setFitWidth(width);
    imageView.setFitHeight(height);
    parentGridPane.setPrefWidth(width);
    parentGridPane.setPrefHeight(height);
  }//}}}

  void setFilePath(String path) {//{{{
    listView.getItems().add(path);
  }//}}}

}


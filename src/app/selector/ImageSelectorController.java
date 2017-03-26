package app.selector;

import static util.Texts.*;

import jiro.javafx.scene.layout.ImageSelectorPane;
import jiro.javafx.scene.layout.DoubleClickInterface;

import java.io.IOException;
import java.nio.file.*;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.input.*;
import javafx.stage.Stage;

public class ImageSelectorController {

  // fxml component//{{{

  @FXML private SplitPane splitPane;

  @FXML private TitledPane listViewTitledPane;
  @FXML private ListView<String> listView;

  @FXML private TitledPane imageViewTitledPane;
  @FXML private ImageSelectorPane imageSelectorPane;

  @FXML private Button okButton;
  @FXML private Button cancelButton;

  //}}}

  // initialize

  @FXML private void initialize() {//{{{
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

    imageSelectorPane.setWidth(WIDTH);
    imageSelectorPane.setHeight(HEIGHT);
    imageSelectorPane.setDoubleClickAction(() -> okButtonOnAction());
  }//}}}

  void setImage(String path) {//{{{
    imageSelectorPane.setImage(path);
  }//}}}

  void setFilePath(String path) {//{{{
    listView.getItems().add(path);
  }//}}}

  // fxml event

  @FXML private void okButtonOnAction() {//{{{
    //baseX = (int) selectedGridPane.getLayoutX();
    //baseY = (int) selectedGridPane.getLayoutY();
    //int col = baseX / WIDTH;
    //int row = baseY / HEIGHT;
    //selectedIndex = col + row * 4;
    int index = imageSelectorPane.getSelectedIndex();
    System.out.println("index: " + index);

    getStage().hide();
  }//}}}

  @FXML private void cancelButtonOnAction() { getStage().hide(); }

  // private methods

  private Stage getStage() { return (Stage) okButton.getScene().getWindow(); }

  // Getter

  public int getSelectedIndex() { return 255; }

}


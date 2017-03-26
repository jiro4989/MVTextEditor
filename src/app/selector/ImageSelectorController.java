package app.selector;

import static util.Texts.*;

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

  private int baseX = 0;
  private int baseY = 0;
  private int selectedIndex = 0;

  // fxml component//{{{

  @FXML private SplitPane splitPane;

  @FXML private TitledPane listViewTitledPane;
  @FXML private ListView<String> listView;

  @FXML private TitledPane imageViewTitledPane;
  @FXML private GridPane parentGridPane;
  @FXML private ImageView imageView;
  @FXML private GridPane focusGridPane;
  @FXML private GridPane selectedGridPane;

  @FXML private Button okButton;
  @FXML private Button cancelButton;

  //}}}

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
  }//}}}

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

  // fxml event

  @FXML private void imageViewOnMouseMoved(MouseEvent e) {//{{{
    int x = (int) e.getX();
    int y = (int) e.getY();
    int gridX = x / WIDTH * WIDTH;
    int gridY = y / HEIGHT * HEIGHT;

    int imgWidth  = (int) imageView.getFitWidth();
    int imgHeight = (int) imageView.getFitHeight();

    if (   (gridX + WIDTH)  <= imgWidth
        && (gridY + HEIGHT) <= imgHeight
       )
    {
      focusGridPane.setLayoutX(gridX);
      focusGridPane.setLayoutY(gridY);
    }
  }//}}}

  @FXML private void focusGridPaneOnMouseClicked(MouseEvent e) {//{{{
    int x = (int) focusGridPane.getLayoutX();
    int y = (int) focusGridPane.getLayoutY();

    selectedGridPane.setLayoutX(x);
    selectedGridPane.setLayoutY(y);
  }//}}}

  @FXML private void selectedGridPaneOnMouseMoved(MouseEvent e) {//{{{
    int x = (int) (e.getX() + selectedGridPane.getLayoutX());
    int y = (int) (e.getY() + selectedGridPane.getLayoutY());
    int gridX = x / WIDTH * WIDTH;
    int gridY = y / HEIGHT * HEIGHT;

    int imgWidth  = (int) imageView.getFitWidth();
    int imgHeight = (int) imageView.getFitHeight();

    if (   (gridX + WIDTH)  <= imgWidth
        && (gridY + HEIGHT) <= imgHeight
       )
    {
      focusGridPane.setLayoutX(gridX);
      focusGridPane.setLayoutY(gridY);
    }
  }//}}}

  @FXML private void selectedGridPaneOnMouseClicked(MouseEvent e) {//{{{
    if (2 <= e.getClickCount()) {
      okButtonOnAction();
    }
  }//}}}

  @FXML private void okButtonOnAction() {//{{{
    baseX = (int) selectedGridPane.getLayoutX();
    baseY = (int) selectedGridPane.getLayoutY();
    int col = baseX / WIDTH;
    int row = baseY / HEIGHT;
    selectedIndex = col + row * 4;
    getStage().hide();
  }//}}}

  @FXML private void cancelButtonOnAction() { getStage().hide(); }

  // private methods

  private Stage getStage() { return (Stage) okButton.getScene().getWindow(); }

  // Getter

  public int getSelectedIndex() { return selectedIndex; }

}


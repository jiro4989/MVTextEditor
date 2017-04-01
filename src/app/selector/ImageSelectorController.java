package app.selector;

import static util.Texts.*;

import jiro.javafx.scene.layout.ImageSelectorPane;
import jiro.javafx.scene.layout.DoubleClickInterface;

import app.MainController;

import java.util.Optional;
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

  private Optional<String> pathOpt = Optional.empty();

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
    // 設定ファイル内にプロジェクトルートパスが設定されていたら
    MainController.preferencesProperties
      .getProperty(KEY_PROJECT).ifPresent(proj -> {
        Path path = Paths.get(proj, IMG_DIR_PATH);
        if (Files.exists(path)) {
          try {
            FileVisitor<Path> visitor = new MyFileVisitor(this);
            Files.walkFileTree(path, visitor);
          } catch (IOException e) {
            e.printStackTrace();
          }

          listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            Path newPath = Paths.get(path.toString(), newVal);
            setImage(newPath.toString());
          });

          listView.getSelectionModel().selectFirst();
        }
      });

    imageSelectorPane.setWidth(WIDTH);
    imageSelectorPane.setHeight(HEIGHT);
    imageSelectorPane.setDoubleClickAction(() -> okButtonOnAction());
  }//}}}

  void setImage(String path) {//{{{
    Path p = Paths.get(path);
    if (Files.exists(p)) {
      imageSelectorPane.setImage(path);
      pathOpt = Optional.ofNullable(path);

      int index = 0;
      String fileName = p.getFileName().toString();
      for (String name : listView.getItems()) {
        if (name.equals(fileName)) {
          listView.getSelectionModel().select(index);
          break;
        }
        index++;
      }
    }
  }//}}}

  void setFilePath(String path) {//{{{
    listView.getItems().add(path);
  }//}}}

  // fxml event

  @FXML private void okButtonOnAction() {//{{{
    getStage().hide();
  }//}}}

  @FXML private void cancelButtonOnAction() { getStage().hide(); }

  // private methods

  private Stage getStage() { return (Stage) okButton.getScene().getWindow(); }

  // Getter

  public Optional<String> getSelectedTileString() {//{{{
    if (pathOpt.isPresent()) {
      int index = imageSelectorPane.getSelectedIndex();
      return pathOpt.map(path -> path + ":" + index);
    }
    return Optional.empty();
  }//}}}

}


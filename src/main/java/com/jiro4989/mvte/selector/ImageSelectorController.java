package com.jiro4989.mvte.selector;

import static com.jiro4989.mvte.util.Texts.*;

import com.jiro4989.mvte.MainController;
import com.jiro4989.mvte.layout.ImageSelectorPane;
import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ImageSelectorController {

  private Optional<String> pathOpt = Optional.empty();
  private boolean isSelected = false;

  // fxml component//{{{

  @FXML private SplitPane splitPane;

  @FXML private TitledPane listViewTitledPane;
  @FXML private ListView<ImgPath> listView;

  @FXML private TitledPane imageViewTitledPane;
  @FXML private ImageSelectorPane imageSelectorPane;

  @FXML private Button okButton;
  @FXML private Button cancelButton;

  // }}}

  // initialize

  @FXML
  private void initialize() { // {{{
    // 設定ファイル内にプロジェクトルートパスが設定されていたら
    MainController.preferencesProperties
        .getProperty(KEY_PROJECT)
        .ifPresent(
            proj -> {
              Path path = Paths.get(proj, IMG_DIR_PATH);
              if (Files.exists(path)) {
                listView
                    .getSelectionModel()
                    .selectedItemProperty()
                    .addListener(
                        (obs, oldVal, newVal) -> {
                          setImage(newVal.absPath);
                        });

                listView.setOnMouseClicked(
                    e -> {
                      if (!listView.getSelectionModel().isEmpty()) {
                        String newVal = listView.getSelectionModel().getSelectedItem().absPath;
                        setImage(newVal);
                      }
                    });

                try {
                  Files.list(path).forEach(this::setFilePath);
                } catch (IOException e) {
                  com.jiro4989.mvte.util.MyLogger.log("画像ファイル読み込みエラー", e);
                } catch (Exception e) {
                  com.jiro4989.mvte.util.MyLogger.log(e);
                }
              }

              listView.getSelectionModel().selectFirst();
            });

    imageSelectorPane.setWidth(WIDTH);
    imageSelectorPane.setHeight(HEIGHT);
    imageSelectorPane.setDoubleClickAction(() -> okButtonOnAction());
  } // }}}

  void setImage(String path) { // {{{
    Path p = Paths.get(path);
    if (Files.exists(p) && !Files.isDirectory(p)) {
      imageSelectorPane.setImage(path);
      pathOpt = Optional.ofNullable(path);

      int index = 0;
      String fileName = p.getFileName().toString();
      for (ImgPath ip : listView.getItems()) {
        if (ip.fileName.equals(fileName)) {
          listView.getSelectionModel().select(index);
          break;
        }
        index++;
      }
      return;
    }
    listView.getSelectionModel().selectFirst();
  } // }}}

  private void setFilePath(Path path) { // {{{
    String p = path.toString();
    if (!Files.isDirectory(path))
      if (p.endsWith(".png") || p.endsWith(".PNG")) listView.getItems().add(new ImgPath(path));
  } // }}}

  // fxml event

  @FXML
  private void okButtonOnAction() { // {{{
    isSelected = true;
    getStage().hide();
  } // }}}

  @FXML
  private void cancelButtonOnAction() {
    getStage().hide();
  }

  // private methods

  private Stage getStage() {
    return (Stage) okButton.getScene().getWindow();
  }

  // Getter

  public Optional<String> getSelectedTileString() { // {{{
    if (pathOpt.isPresent()) {
      int index = imageSelectorPane.getSelectedIndex();
      return pathOpt.map(path -> path + ":" + index);
    }
    return Optional.empty();
  } // }}}

  public boolean isSelected() {
    return isSelected;
  }
}

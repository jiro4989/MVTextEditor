package com.jiro4989.mvte.selector;

import static com.jiro4989.mvte.util.Texts.*;

import com.jiro4989.mvte.util.ResourceBundleWithUtf8;
import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.*;

public class ImageSelector extends Stage {

  private ImageSelectorController controller;

  public ImageSelector(String path) { // {{{
    URL location = getClass().getResource("image_selector.fxml");
    ResourceBundle resources =
        ResourceBundle.getBundle(
            "com.jiro4989.mvte.selector.dict",
            Locale.getDefault(),
            ResourceBundleWithUtf8.UTF8_ENCODING_CONTROL);
    FXMLLoader loader = new FXMLLoader(location, resources);

    try {
      BorderPane root = (BorderPane) loader.load();
      controller = (ImageSelectorController) loader.getController();
      Scene scene = new Scene(root, 800, 500);
      scene.getStylesheets().add(getClass().getResource(BASIC_CSS).toExternalForm());
      setScene(scene);

      initStyle(StageStyle.UTILITY);
      initModality(Modality.APPLICATION_MODAL);
      setTitle(resources.getString("stage.title"));

      controller.setImage(path);
    } catch (IOException e) {
      com.jiro4989.mvte.util.MyLogger.log("レイアウトファイル読み込みに失敗しましたエラー", e);
    } catch (Exception e) {
      com.jiro4989.mvte.util.MyLogger.log(e);
    }
  } // }}}

  public Optional<String> getSelectedTileString() {
    return controller.getSelectedTileString();
  }

  public boolean isSelected() {
    return controller.isSelected();
  }
}

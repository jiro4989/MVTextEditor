package app.config;

import static util.Texts.*;

import util.ResourceBundleWithUtf8;

import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.*;

public class ImportConfigStage extends Stage {

  private ImportConfigController controller;

  public ImportConfigStage() {//{{{
    URL location = getClass().getResource("import_config.fxml");
    ResourceBundle resources = ResourceBundle.getBundle(
        "app.config.dict"
        , Locale.getDefault()
        , ResourceBundleWithUtf8.UTF8_ENCODING_CONTROL
        );
    FXMLLoader loader = new FXMLLoader(location, resources);

    try {
      BorderPane root = (BorderPane) loader.load();
      controller = (ImportConfigController) loader.getController();
      Scene scene = new Scene(root);
      scene.getStylesheets().add(getClass().getResource(BASIC_CSS).toExternalForm());
      setScene(scene);

      initStyle(StageStyle.UTILITY);
      initModality(Modality.APPLICATION_MODAL);
      setTitle(resources.getString("stage.title"));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }//}}}

}

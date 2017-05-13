package app;

import static util.Texts.*;

import jiro.java.util.MyProperties;
import util.ResourceBundleWithUtf8;

import util.InitUtils;
import util.Texts;
import util.Utils;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.*;

public class Main extends Application {

  public static Stage mainStage;
  static MyProperties mainMp;
  private MainController controller;
  public static ResourceBundle resources;

  @Override
  public void start(Stage primaryStage) {//{{{
    mainStage = primaryStage;
    mainMp = new MyProperties(MAIN_PROPERTIES);
    InitUtils.mkPropDirs();

    Path preferences = Paths.get(".", "properties", "mvte", "preferences.xml");
    if (Files.notExists(preferences)) {
      Utils.showSelectProjectDirDialog();

      Texts.resetProjectFolderProperty(MainController.preferencesProperties, primaryStage);
    }

    changeLanguages();

    URL location = getClass().getResource("main.fxml");
    resources = ResourceBundle.getBundle(
        "app.dict"
        , Locale.getDefault()
        , ResourceBundleWithUtf8.UTF8_ENCODING_CONTROL
        );
    FXMLLoader loader = new FXMLLoader(location, resources);

    try {
      VBox root   = (VBox) loader.load();
      controller  = (MainController) loader.getController();
      Scene scene = new Scene(root, 1280, 720);

      String noname = resources.getString("noname");
      primaryStage.setScene(scene);
      primaryStage.setTitle(noname + " - " +TITLE_VERSION);
      primaryStage.getIcons().add(new Image(APP_ICON));
      primaryStage.setMinWidth(80.0);
      primaryStage.setMinHeight(140.0);

      primaryStage.setOnCloseRequest(e -> controller.closeRequest());

      if (mainMp.load())
        mainMp.customStage(primaryStage);

      String backgroundItem = resources.getString("background");
      controller.setBackgroundItem(backgroundItem);

      String positionItem = resources.getString("position");
      controller.setPositionItem(positionItem);

      primaryStage.show();
      controller.changeFontSizes();
      controller.setRecentFiles();
    } catch (IOException e) {
      util.MyLogger.log("FXMLロードエラー", e);
    } catch (Exception e) {
      util.MyLogger.log(e);
    }
  }//}}}

  public static void main(String... args) { launch(args); }

  private void changeLanguages() {//{{{
    MyProperties preferences = new MyProperties(PREFERENCES_PROPERTIES);
    if (preferences.load()) {
      String ja = Locale.JAPAN.getLanguage();
      String langs = preferences.getProperty("langs").orElse(ja);
      if (!langs.equals(ja)) {
        Locale.setDefault(Locale.ENGLISH);
      }
    }
  }//}}}

}

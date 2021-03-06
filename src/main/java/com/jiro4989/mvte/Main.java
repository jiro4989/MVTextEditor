package com.jiro4989.mvte;

import static com.jiro4989.mvte.util.Texts.*;

import com.jiro4989.mvte.util.InitUtils;
import com.jiro4989.mvte.util.MyProperties;
import com.jiro4989.mvte.util.ResourceBundleWithUtf8;
import com.jiro4989.mvte.util.Texts;
import com.jiro4989.mvte.util.Utils;
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
  public void start(Stage primaryStage) { // {{{
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
    resources =
        ResourceBundle.getBundle(
            "com.jiro4989.mvte.dict",
            Locale.getDefault(),
            ResourceBundleWithUtf8.UTF8_ENCODING_CONTROL);
    FXMLLoader loader = new FXMLLoader(location, resources);

    try {
      VBox root = (VBox) loader.load();
      controller = (MainController) loader.getController();
      Scene scene = new Scene(root, 1280, 720);

      String noname = resources.getString("noname");
      primaryStage.setScene(scene);
      primaryStage.setTitle(noname + " - " + TITLE);
      primaryStage.getIcons().add(new Image(APP_ICON));
      primaryStage.setMinWidth(80.0);
      primaryStage.setMinHeight(140.0);

      primaryStage.setOnCloseRequest(e -> controller.closeRequest());

      if (mainMp.load()) mainMp.customStage(primaryStage);

      String backgroundItem = resources.getString("background");
      controller.setBackgroundItem(backgroundItem);

      String positionItem = resources.getString("position");
      controller.setPositionItem(positionItem);

      primaryStage.show();
      controller.changeFontSizes();
      controller.setRecentFiles();
    } catch (IOException e) {
      com.jiro4989.mvte.util.MyLogger.log("FXMLロードエラー", e);
    } catch (Exception e) {
      com.jiro4989.mvte.util.MyLogger.log(e);
    }
  } // }}}

  public static void main(String... args) {
    System.out.println("--------------------------------------------");
    System.out.println("application_name: " + TITLE);
    System.out.println("version: " + Version.version);
    System.out.println("commit_hash: " + Version.commitHash);
    System.out.println("document: README.txt");
    System.out.println("author: 次郎 (jiro)");
    System.out.println("contact: https://twitter.com/jiro_saburomaru");
    System.out.println("--------------------------------------------");
    launch(args);
  }

  private void changeLanguages() { // {{{
    MyProperties preferences = new MyProperties(PREFERENCES_PROPERTIES);
    if (preferences.load()) {
      String ja = Locale.JAPAN.getLanguage();
      String langs = preferences.getProperty("langs").orElse(ja);
      if (!langs.equals(ja)) {
        Locale.setDefault(Locale.ENGLISH);
      }
    }
  } // }}}
}

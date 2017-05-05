package app;

import static util.Texts.*;

import jiro.java.util.MyProperties;
import util.ResourceBundleWithUtf8;

import util.InitUtils;
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
      DirectoryChooser dc = new DirectoryChooser();
      File file = dc.showDialog(primaryStage);
      if (file != null) {
        MainController.preferencesProperties.setProperty("project", file.getAbsolutePath());
        MainController.preferencesProperties.store();
      }
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
      Scene scene = new Scene(root, 800, 600);

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

      /*
       * // TODO 過去のソースの名残//{{{

       * // フォントサイズの変更
       * final MyProperties preferences = new MyProperties(PREFERENCES_FILE);
       * preferences.load();
       * String fontSize = preferences.getProperty(KEY_FONT_SIZE).orElse(DEFAULT_VALUE_FONT_SIZE);
       * controller.setFontSize(fontSize);
       * controller.setFontSizeOfMenuBar(fontSize);

       * // プリセットの変更
       * String walk     = preferences.getProperty(KEY_WALK_PRESET).orElse(WALK_PREST);
       * String sideView = preferences.getProperty(KEY_SIDE_VIEW_PRESET).orElse(SIDE_VIEW_PREST);
       * controller.setWalkStandard(new File(walk));
       * controller.setSideViewStandard(new File(sideView));

       * // 最近開いたファイルを更新
       * controller.setRecentFiles();

       * //}}}
       */

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

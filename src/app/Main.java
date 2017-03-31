package app;

import static util.Texts.*;

import jiro.java.util.MyProperties;

import util.InitUtils;

import java.io.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

  static MyProperties mainMp;
  private MainController controller;

  @Override
  public void start(Stage primaryStage) {//{{{
    mainMp = new MyProperties(MAIN_PROPERTIES);
    InitUtils.mkPropDirs();

    //changeLanguages();
    //PresetsUtils.mkInitDirs();
    //PresetsUtils.mkInitPresets();

    FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));

    try {
      VBox root = (VBox) loader.load();
      controller = (MainController) loader.getController();
      Scene scene = new Scene(root, 800, 600);
      scene.getStylesheets().add(BASIC_CSS);

      primaryStage.setScene(scene);
      primaryStage.setTitle(TITLE_VERSION);
      primaryStage.getIcons().add(new Image(APP_ICON));
      primaryStage.setMinWidth(80.0);
      primaryStage.setMinHeight(140.0);

      primaryStage.setOnCloseRequest(e -> controller.closeRequest());

      if (mainMp.load())
        mainMp.customStage(primaryStage);

      //
      //      // TODO 過去のソースの名残//{{{
      //
      //      if (mainMp.load()) mainMp.customStage(primaryStage);
      //
      //      // 設定ウィンドウの追従リスナー
      //      primaryStage.xProperty      ( ).addListener ( ( obs, o, n) -> controller.resizeConfigStage ( ) ) ;
      //      primaryStage.yProperty      ( ).addListener ( ( obs, o, n) -> controller.resizeConfigStage ( ) ) ;
      //      primaryStage.widthProperty  ( ).addListener ( ( obs, o, n) -> controller.resizeConfigStage ( ) ) ;
      //      primaryStage.heightProperty ( ).addListener ( ( obs, o, n) -> controller.resizeConfigStage ( ) ) ;
      //
      //      primaryStage.setOnCloseRequest(e -> controller.closeRequest());
      //
      //      // マウスドラッグでウィンドウの位置を変更//{{{
      //
      //      final Delta delta = new Delta();
      //
      //      root.setOnMousePressed(e -> {
      //        delta.x = primaryStage.getX() - e.getScreenX();
      //        delta.y = primaryStage.getY() - e.getScreenY();
      //      });
      //
      //      root.setOnMouseDragged(e -> {
      //        primaryStage.setX(e.getScreenX() + delta.x);
      //        primaryStage.setY(e.getScreenY() + delta.y);
      //      });
      //
      //      //}}}
      //
      //      root.setOnScroll(e -> controller.updateZoomRate(e));
      //
      //      controller.setConfigStageInstance();
      //      controller.setInitAlwaysOnTop();
      //
      //      // フォントサイズの変更
      //      final MyProperties preferences = new MyProperties(PREFERENCES_FILE);
      //      preferences.load();
      //      String fontSize = preferences.getProperty(KEY_FONT_SIZE).orElse(DEFAULT_VALUE_FONT_SIZE);
      //      controller.setFontSize(fontSize);
      //      controller.setFontSizeOfMenuBar(fontSize);
      //
      //      // プリセットの変更
      //      String walk     = preferences.getProperty(KEY_WALK_PRESET).orElse(WALK_PREST);
      //      String sideView = preferences.getProperty(KEY_SIDE_VIEW_PRESET).orElse(SIDE_VIEW_PREST);
      //      controller.setWalkStandard(new File(walk));
      //      controller.setSideViewStandard(new File(sideView));
      //
      //      // 最近開いたファイルを更新
      //      controller.setRecentFiles();
      //
      //      //}}}
      //

      primaryStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }//}}}

  public static void main(String... args) {//{{{
    launch(args);
  }//}}}

  //
  //  private void changeLanguages() {//{{{
  //    MyProperties preferences = new MyProperties(PREFERENCES_FILE);
  //    if (preferences.load()) {
  //
  //      String ja = Locale.JAPAN.getLanguage();
  //      String langs = preferences.getProperty(KEY_LANGS).orElse(ja);
  //      if (!langs.equals(ja)) {
  //
  //        Locale.setDefault(Locale.ENGLISH);
  //
  //      }
  //
  //    }
  //  }//}}}
  //
  //  private class Delta {//{{{
  //    double x;
  //    double y;
  //  }//}}}
  //

}

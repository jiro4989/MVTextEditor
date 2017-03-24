package app.menubar;

import static util.Texts.*;

import jiro.java.lang.Brackets;
import jiro.java.lang.FormattableText;
import jiro.javafx.scene.control.DialogUtils;
import jiro.javafx.stage.AboutStage;
import jiro.javafx.stage.MyFileChooser;

import app.MainController;
import util.ResourceBundleWithUtf8;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class MyMenuBar extends VBox {

  private MainController mainController;
  private final MyFileChooser textFileManager;

  // FXMLコンポーネント{{{

  // ファイル
  @FXML private Menu     fileMenu;
  @FXML private MenuItem openTextFileMenuItem;
  @FXML private MenuItem openSideViewMenuItem;
  @FXML private Menu     openWalkRecentMenu;
  @FXML private Menu     openSideViewRecentMenu;
  @FXML private MenuItem closeMenuItem;
  @FXML private MenuItem currentWalkPresetMenuItem;
  @FXML private MenuItem currentSideViewPresetMenuItem;
  @FXML private MenuItem walkPresetMenuItem;
  @FXML private MenuItem sideViewPresetMenuItem;
  @FXML private MenuItem editWalkPresetMenuItem;
  @FXML private MenuItem editSideViewPresetMenuItem;
  @FXML private MenuItem preferencesMenuItem;
  @FXML private MenuItem quitMenuItem;
  @FXML private MenuItem forcedTerminateMenuItem;

  // ヘルプメニュー
  @FXML private Menu     helpMenu;
  @FXML private MenuItem aboutMenuItem;

  //}}}

  // constructor

  public MyMenuBar() {//{{{

    textFileManager  = new MyFileChooser.Builder("Text Files", "*.txt")
      .initDir("./input").build();

    URL location = getClass().getResource("my_menu_bar.fxml");
    ResourceBundle resources = ResourceBundle.getBundle(
        "app.menubar.dict"
        , Locale.getDefault()
        , ResourceBundleWithUtf8.UTF8_ENCODING_CONTROL
        );
    FXMLLoader loader = new FXMLLoader(location, resources);

    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }//}}}

  // fxml event

  // file menu

  @FXML private void openTextFileMenuItemOnAction() {//{{{
    textFileManager.openFile().ifPresent(file -> {
      // TODO 一時変数
      final int RETURN_SIZE = 27 * 2;
      final int INDENT_SIZE = 2;
      final Brackets BRACKETS = Brackets.TYPE1;

      // TODO test code
      try {
        FormattableText ft = new FormattableText.Builder(file)
          .returnOption(true)
          .returnSize(RETURN_SIZE)
          .indentOption(true)
          .indentSize(INDENT_SIZE)
          .bracketsOption(true)
          .brackets(BRACKETS)
          .joiningOption(false)
          .build();
        mainController.setTextList(ft.format().getTextList());
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }//}}}

  @FXML private void openSideViewMenuItemOnAction() {//{{{
  }//}}}

  @FXML private void openWalkRecentMenuItemOnAction() {//{{{
  }//}}}

  @FXML private void openSideViewRecentMenuItemOnAction() {//{{{
  }//}}}

  @FXML private void closeMenuItemOnAction() {//{{{
  }//}}}

  @FXML private void walkPresetMenuItemOnAction() {//{{{
  }//}}}

  @FXML private void sideViewPresetMenuItemOnAction() {//{{{
  }//}}}

  @FXML private void editWalkPresetMenuItemOnAction() {//{{{
  }//}}}

  @FXML private void editSideViewPresetMenuItemOnAction() {//{{{
  }//}}}

  @FXML private void preferencesMenuItemOnAction() {//{{{
  }//}}}

  @FXML private void quitMenuItemOnAction() {//{{{
    mainController.closeRequest();
    Platform.exit();
  }//}}}

  @FXML private void forcedTerminateMenuItemOnAction() {//{{{
    DialogUtils.showForcedTerminationDialog();
  }//}}}

  // help menu

  @FXML private void aboutMenuItemOnAction() {//{{{
    AboutStage about = new AboutStage.Builder(TITLE, VERSION)
      .author("次郎 (Jiro)")
      .blog("次ログ")
      .blogUrl("http://jiroron666.hatenablog.com/")
      .css(BASIC_CSS)
      .appIcon(APP_ICON)
      .build();
    about.showAndWait();
  }//}}}

  // Setter

  public void setMainController(MainController aMain) { mainController = aMain; }

}

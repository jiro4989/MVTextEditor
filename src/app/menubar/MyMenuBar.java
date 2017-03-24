package app.menubar;

import static util.Texts.*;

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
  private final MyFileChooser mfc;

  // FXMLコンポーネント{{{

  // ファイル
  @FXML private Menu     fileMenu;
  @FXML private MenuItem openCharaChipMenuItem;
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

  // コンストラクタ

  public MyMenuBar() {//{{{

    mfc  = new MyFileChooser.Builder("Text Files", "*.txt").build();

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

  // FXMLイベント

  // ファイルメニュー

  @FXML private void openCharaChipMenuItemOnAction() {//{{{
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

  // Setter

  public void setMainController(MainController aMain) { mainController = aMain; }

}

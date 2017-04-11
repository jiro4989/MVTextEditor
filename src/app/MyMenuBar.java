package app;

import jiro.java.lang.Brackets;
import jiro.java.lang.FormattableText;
import jiro.javafx.stage.MyFileChooser;

import java.io.IOException;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javax.xml.parsers.ParserConfigurationException;

class MyMenuBar {
  private final MainController mainController;
  private final MyFileChooser textFileManager;
  private final MyFileChooser xmlManager;

  private final MenuItem newMenuItem;
  private final MenuItem openMenuItem;
  private final Menu     recentMenu;
  private final MenuItem closeMenu;
  private final MenuItem saveMenuItem;
  private final MenuItem saveAsMenuItem;
  private final MenuItem importMenuItem;
  private final MenuItem exportMenuItem;
  private final MenuItem preferencesMenuItem;
  private final MenuItem quitMenuItem;

  MyMenuBar(
      MainController mainController
      , MenuItem newMenuItem
      , MenuItem openMenuItem
      , Menu     recentMenu
      , MenuItem closeMenu
      , MenuItem saveMenuItem
      , MenuItem saveAsMenuItem
      , MenuItem importMenuItem
      , MenuItem exportMenuItem
      , MenuItem preferencesMenuItem
      , MenuItem quitMenuItem
      )
  {//{{{

    textFileManager = new MyFileChooser.Builder("Text Files", "*.txt").build();
    xmlManager      = new MyFileChooser.Builder("Text Files", "*.xml").build();

    this.mainController      = mainController;
    this.newMenuItem         = newMenuItem;
    this.openMenuItem        = openMenuItem;
    this.recentMenu          = recentMenu;
    this.closeMenu           = closeMenu;
    this.saveMenuItem        = saveMenuItem;
    this.saveAsMenuItem      = saveAsMenuItem;
    this.importMenuItem      = importMenuItem;
    this.exportMenuItem      = exportMenuItem;
    this.preferencesMenuItem = preferencesMenuItem;
    this.quitMenuItem        = quitMenuItem;

    saveMenuItem   . setOnAction(e -> saveXml());
    saveAsMenuItem . setOnAction(e -> saveAsXml());
    importMenuItem . setOnAction(e -> importTextFile());

  }//}}}

  private void importTextFile() {//{{{
    textFileManager.openFile().ifPresent(file -> {
      // TODO 一時変数
      final int RETURN_SIZE = 27 * 2;
      final int INDENT_SIZE = 2;
      final Brackets BRACKETS = Brackets.TYPE1;

      // TODO test code
      try {
        FormattableText ft = new FormattableText.Builder(file)
          .actorNameOption(true)
          .returnOption(true)
          .returnSize(RETURN_SIZE)
          .indentOption(true)
          .indentSize(INDENT_SIZE)
          .bracketsOption(true)
          .brackets(BRACKETS)
          .joiningOption(false)
          .build();
        mainController.setTextList(ft.format().getTextList());
      } catch (ParserConfigurationException pce) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText("一時データ保存時のXML変換に失敗しました。");
        alert.setContentText("作者に報告してください。");
        alert.showAndWait();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    });
  }//}}}

  private void saveXml() {//{{{
  }//}}}

  private void saveAsXml() {//{{{
    xmlManager.saveFile().ifPresent(file -> {
      mainController.saveXml(file);
    });
  }//}}}

}

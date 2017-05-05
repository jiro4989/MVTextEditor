package app;

import static util.Texts.len;

import jiro.java.lang.Brackets;
import jiro.java.lang.FormattableText;
import jiro.java.util.MyProperties;
import jiro.javafx.stage.MyFileChooser;

import app.table.SavingData;
import app.table.TextDB;
import util.Texts;
import util.Utils;

import java.io.*;
import java.util.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

class MyMenuBar {

  private Optional<File> saveFileOpt = Optional.empty();
  private final Tooltip tooltip = new Tooltip();

  private final MainController mainController;
  private final MyFileChooser textFileManager;
  private final MyFileChooser xmlManager;
  private final MyFileChooser exportManager;

  // fxml component//{{{

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
  //}}}

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
    exportManager   = new MyFileChooser.Builder("Text Files", "*.json").build();

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

    openMenuItem   . setOnAction(e -> openXml());
    saveMenuItem   . setOnAction(e -> saveXml());
    saveAsMenuItem . setOnAction(e -> saveAsXml());
    importMenuItem . setOnAction(e -> importTextFile());
    exportMenuItem . setOnAction(e -> exportJson());

  }//}}}

  // package methods

  void reloadXml() {//{{{
    xmlManager.getOpenedFile().ifPresent(file -> {
      Alert alert = new Alert(AlertType.CONFIRMATION);

      Locale locale = Locale.getDefault();
      String header = locale.equals(Locale.JAPAN)
        ? "ファイルを開き直します。"
        : "Reopen File.";
      alert.setHeaderText(header);

      String content = locale.equals(Locale.JAPAN)
        ? "編集後のデータを保存していなかった場合、そのデータは失われます。本当によろしいですか？"
        : "Edited datas are lost if you didn't save one. Really continue ?";
      alert.setContentText(content);

      alert.showAndWait()
        .filter(r -> r == ButtonType.OK)
        .ifPresent(r -> {
          try {
            List<TextDB> dbs = SavingData.convertTextDB(file);
            mainController.setTextDB(dbs);
          } catch (SAXException e) {
            util.MyLogger.log(e);
          } catch (ParserConfigurationException e) {
            util.MyLogger.log("XMLパースできませんでしたエラー", e);
          } catch (IOException e) {
            util.MyLogger.log("ファイル読み込みに失敗しましたエラー", e);
          } catch (Exception e) {
            util.MyLogger.log(e);
          }
        });
    });
  }//}}}

  void close() {
    saveFileOpt = Optional.empty();
    saveMenuItem.setDisable(true);
  }

  private void openXml() {//{{{
    xmlManager.openFile().ifPresent(file -> {
      try {
        List<TextDB> dbs = SavingData.convertTextDB(file);
        mainController.setTextDB(dbs);
        Main.mainStage.setTitle(file.getName() + " - " + Texts.TITLE_VERSION);
        saveMenuItem.setDisable(false);
        saveFileOpt = Optional.ofNullable(file);
      } catch (SAXException e) {
        util.MyLogger.log(e);
      } catch (ParserConfigurationException e) {
        util.MyLogger.log("XMLパースできませんでしたエラー", e);
      } catch (IOException e) {
        util.MyLogger.log("ファイル読み込みに失敗しましたエラー", e);
      } catch (Exception e) {
        util.MyLogger.log(e);
      }
    });
  }//}}}

  private void saveXml() {//{{{
    saveFileOpt.ifPresent(file -> {
      save(file);
      Main.mainStage.setTitle(file.getName() + " - " + Texts.TITLE_VERSION);
    });
  }//}}}

  private void saveAsXml() {//{{{
    xmlManager.saveFile().ifPresent(file -> {
      save(file);
      Main.mainStage.setTitle(file.getName() + " - " + Texts.TITLE_VERSION);
    });
  }//}}}

  private void importTextFile() {//{{{
    textFileManager.openFile().ifPresent(file -> {

      MyProperties mp = MainController.formatProperties;
      String textReturnStr     = mp.getProperty("textReturn").get();
      String textReturnSizeStr = mp.getProperty("textReturnSize").get();
      String textIndentStr     = mp.getProperty("textIndent").get();
      String bracketStartStr   = mp.getProperty("bracketStart").get();
      String wrappingStr       = mp.getProperty("wrapping").get();

      boolean textReturn = textReturnStr     == null ? true : Boolean.valueOf(textReturnStr);
      int textReturnSize = textReturnSizeStr == null ? 54   : Integer.parseInt(textReturnSizeStr);
      boolean textIndent = textIndentStr     == null ? true : Boolean.valueOf(textIndentStr);
      int bracketsIndex  = bracketStartStr   == null ? 0    : Integer.parseInt(bracketStartStr);
      boolean wrapping   = wrappingStr       == null ? true : Boolean.valueOf(wrappingStr);
      Brackets brackets  = Brackets.values()[bracketsIndex];
      int indentSize     = len(brackets.START);

      try {
        FormattableText ft = new FormattableText.Builder(file)
          .actorNameOption(true)
          .returnOption(textReturn)
          .returnSize(textReturnSize)
          .indentOption(true)
          .indentSize(indentSize)
          .bracketsOption(wrapping)
          .brackets(brackets)
          .joiningOption(false)
          .build();
        mainController.setTextList(ft.format().getTextList());

        // データの管理はxmlで行うため、
        // importするtxtファイルをタイトルにセットするのは適当ではない？
        //Main.mainStage.setTitle(file.getName() + " - " + Texts.TITLE_VERSION);
      } catch (IOException ioe) {
        util.MyLogger.log("ファイル読み込みに失敗しましたエラー", ioe);
      } catch (Exception e) {
        util.MyLogger.log(e);
      }
    });
  }//}}}

  private void exportJson() {//{{{
    MainController.preferencesProperties.getProperty("project").ifPresent(dir -> {
      String fileName = dir + File.separator + "Map001.json";
      File file = new File(fileName);
      int count = 0;
      while (true) {
        if (!file.exists()) break;
        file = new File(String.format(dir + File.separator + "data" + File.separator + "Map%03d.json", ++count));
      }
      try {
        mainController.exportJson(file);
      } catch (FileNotFoundException e) {
        util.MyLogger.log("ファイルが見つかりませんでしたエラー", e);

        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText("ファイルが見つかりませんでした。");
        alert.setContentText("作者に報告してください。");
        alert.showAndWait();
      } catch (IOException e) {
        util.MyLogger.log("ファイル出力に失敗しましたエラー", e);

        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText("ファイル出力エラー。");
        alert.setContentText("ファイルを生成する権限があるか、あるいはファイル出力先を正常に指定できているか確認してください。");
        alert.showAndWait();
      } catch (Exception e) {
        util.MyLogger.log(e);
      }
    });
  }//}}}

  private void save(File file) {//{{{
    try {
      saveFileOpt = Optional.ofNullable(file);
      mainController.saveXml(file);

      // TODO 表示すると表示され続けてしまう。
      //tooltip.setText("Data is saved !");
      //if (tooltip.isShowing()) {
      //  tooltip.hide();
      //}
      //tooltip.show(mainController.getWindow());
    } catch (ParserConfigurationException pce) {
      util.MyLogger.log("XMLパースできませんでしたエラー", pce);
      showParsingErrorDialog();
    } catch (Exception e) {
      util.MyLogger.log(e);
    }
  }//}}}

  private static final void showParsingErrorDialog() {//{{{
    Alert alert = new Alert(AlertType.ERROR);
    alert.setHeaderText("一時データ保存時のXML変換に失敗しました。");
    alert.setContentText("作者に報告してください。");
    alert.showAndWait();
  }//}}}

}

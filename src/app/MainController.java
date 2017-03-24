package app;

import static util.Texts.*;

import jiro.java.lang.FormattableText;
import jiro.java.lang.Brackets;
import jiro.java.util.MyProperties;
import jiro.javafx.stage.AboutStage;
import jiro.javafx.stage.MyFileChooser;

import app.manager.TextManager;
import app.table.TextTable;
import app.viewer.TextViewer;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import javafx.fxml.FXML;

public class MainController {
  private MyProperties formatProperties;
  private MyProperties preferencesProperties;

  private static final int RETURN_SIZE = 27 * 2;
  private static final int INDENT_SIZE = 2;
  private static final Brackets BRACKETS = Brackets.TYPE1;

  @FXML private TextTable   textTable;
  @FXML private TextViewer  textViewer;
  @FXML private TextManager textManager;

  @FXML private void initialize() {//{{{
    formatProperties = new MyProperties(FORMAT_PROPERTIES);
    formatProperties.load();

    preferencesProperties = new MyProperties(PREFERENCES_PROPERTIES);
    preferencesProperties.load();
    preferencesProperties.changeLanguages();

    // TODO test code
    try {
      FormattableText ft = new FormattableText.Builder(new File("./input/test3.csv"))
        .returnOption(true)
        .returnSize(RETURN_SIZE)
        .indentOption(true)
        .indentSize(INDENT_SIZE)
        .bracketsOption(true)
        .brackets(BRACKETS)
        .joiningOption(false)
        .build();
      ft.format().show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }//}}}

  void closeRequest() {//{{{
    Main.mainMp.setProperty(textTable);
    Main.mainMp.store();
    formatProperties.store();

    // TODO 一時的な設定
    String langs = Locale.getDefault().getLanguage();
    preferencesProperties.setProperty("langs", langs);

    preferencesProperties.store();
  }//}}}

}

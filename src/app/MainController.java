package app;

import static util.Texts.*;

import jiro.java.util.MyProperties;
import jiro.javafx.stage.AboutStage;
import jiro.javafx.stage.MyFileChooser;

import app.manager.TextManager;
import app.menubar.MyMenuBar;
import app.table.TextDB;
import app.table.TextTable;
import app.viewer.TextViewer;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {
  private MyProperties formatProperties;
  private MyProperties preferencesProperties;

  // fxml component

  @FXML private MyMenuBar   myMenuBar;
  @FXML private TextTable   textTable;
  @FXML private TextViewer  textViewer;
  @FXML private TextManager textManager;

  @FXML private void initialize() {//{{{
    myMenuBar.setMainController(this);
    textTable.setMainController(this);

    formatProperties = new MyProperties(FORMAT_PROPERTIES);
    formatProperties.load();

    preferencesProperties = new MyProperties(PREFERENCES_PROPERTIES);
    preferencesProperties.load();
    preferencesProperties.changeLanguages();

  }//}}}

  // public methods

  public void setTextList(List<List<String>> listList) {//{{{
    textTable.setTextList(listList);
  }//}}}

  public void closeRequest() {//{{{
    Main.mainMp.setProperty(textTable);
    Main.mainMp.store();
    formatProperties.store();

    // TODO 一時的な設定
    String langs = Locale.getDefault().getLanguage();
    preferencesProperties.setProperty("langs", langs);

    preferencesProperties.store();
  }//}}}

  public void updateTextViewer(TextDB db) {//{{{
    textViewer.update(db);
  }//}}}

}

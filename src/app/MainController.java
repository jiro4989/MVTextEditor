package app;

import static util.Texts.*;

import jiro.java.util.MyProperties;
import jiro.javafx.stage.AboutStage;
import jiro.javafx.stage.MyFileChooser;

import app.table.TextDB;
import app.table.TextTable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class MainController {

  public static MyProperties formatProperties;
  public static MyProperties preferencesProperties;

  private MyMenuBar myMenubar;
  private TextTable textTable;
  private TextView textView;

  // fxml component//{{{

  // menubar

  @FXML private MenuItem openTextFileMenuItem;

  // table

  @FXML private TableView<TextDB> tableView;
  @FXML private TableColumn<TextDB, String> iconColumn;
  @FXML private TableColumn<TextDB, String> nameColumn;
  @FXML private TableColumn<TextDB, String> textColumn;
  @FXML private TableColumn<TextDB, String> backgroundColumn;
  @FXML private TableColumn<TextDB, String> positionColumn;

  // textview

  @FXML private ImageView faceImageView;
  @FXML private GridPane colorPickerGridPane;
  @FXML private ImageView colorPickerImageView;
  @FXML private TextArea editorTextArea;
  @FXML private ComboBox<String> backgroundComboBox;
  @FXML private ComboBox<String> positionComboBox;

  //}}}

  // initialize

  @FXML private void initialize() {//{{{
    formatProperties = new MyProperties(FORMAT_PROPERTIES);
    formatProperties.load();

    preferencesProperties = new MyProperties(PREFERENCES_PROPERTIES);
    preferencesProperties.load();
    preferencesProperties.changeLanguages();

    myMenubar = new MyMenuBar(this, openTextFileMenuItem);
    textTable = new TextTable(this, tableView, iconColumn, nameColumn, textColumn, backgroundColumn, positionColumn);
    textView  = new TextView(this, faceImageView ,colorPickerGridPane ,colorPickerImageView ,editorTextArea ,backgroundComboBox ,positionComboBox);
    // TODO
    preferencesProperties.getProperty(KEY_PROJECT).ifPresent(proj -> {
      textView.setColorPickerImage(proj + "/" + IMG_WINDOW_PATH);
    });
  }//}}}

  // public methods

  public void closeRequest() {//{{{
    Main.mainMp.setProperty(tableView);
    Main.mainMp.store();
    formatProperties.store();

    // TODO 一時的な設定
    String langs = Locale.getDefault().getLanguage();
    preferencesProperties.setProperty("langs", langs);

    preferencesProperties.store();
  }//}}}

  // fxml event

  public void updateTextView(TextDB db) {
    textView.update(db);
  }

  public void setTextList(List<List<String>> listList) {
    textTable.setTextList(listList);
  }

}

package app;

import static util.Texts.*;

import jiro.java.util.MyProperties;
import jiro.java.util.RecentFilesUtils;
import jiro.javafx.scene.control.DialogUtils;
import jiro.javafx.stage.AboutStage;
import jiro.javafx.stage.MyFileChooser;

import app.config.ImportConfigStage;
import app.manager.ActorDB;
import app.manager.EditManager;
import app.manager.VarDB;
import app.table.MapInfos;
import app.table.TextDB;
import app.table.TextTable;
import util.Utils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import javafx.stage.Stage;
import javax.xml.parsers.ParserConfigurationException;

public class MainController {

  public static MyProperties formatProperties = new MyProperties(FORMAT_PROPERTIES);
  public static MyProperties preferencesProperties = new MyProperties(PREFERENCES_PROPERTIES);
  public static MyProperties logsProperties = new MyProperties(LOGS_PROPERTIES);

  private DirectoryChooser dc;

  private MyMenuBar myMenubar;
  private TextTable textTable;
  private TextView textView;
  private EditManager editManager;

  // fxml component//{{{

  // menubar

  @FXML private MenuItem newMenuItem;
  @FXML private MenuItem openMenuItem;
  @FXML private Menu     recentMenu;
  @FXML private MenuItem closeMenu;
  @FXML private MenuItem saveMenuItem;
  @FXML private MenuItem saveAsMenuItem;
  @FXML private MenuItem importMenuItem;
  @FXML private MenuItem exportMenuItem;
  @FXML private MenuItem importConfigsMenuItem;
  @FXML private MenuItem selectProjectMenuItem;
  @FXML private MenuItem quitMenuItem;
  @FXML private MenuItem forcedTerminateMenuItem;

  @FXML private MenuItem iconIndex1MenuItem;
  @FXML private MenuItem iconIndex2MenuItem;
  @FXML private MenuItem iconIndex3MenuItem;
  @FXML private MenuItem iconIndex4MenuItem;
  @FXML private MenuItem iconIndex5MenuItem;
  @FXML private MenuItem iconIndex6MenuItem;
  @FXML private MenuItem iconIndex7MenuItem;
  @FXML private MenuItem iconIndex8MenuItem;

  @FXML private ToggleGroup   tableFontGroup;
  @FXML private RadioMenuItem tableFontSize8RadioMenuItem;
  @FXML private RadioMenuItem tableFontSize9RadioMenuItem;
  @FXML private RadioMenuItem tableFontSize10RadioMenuItem;
  @FXML private RadioMenuItem tableFontSize11RadioMenuItem;
  @FXML private RadioMenuItem tableFontSize12RadioMenuItem;

  @FXML private ToggleGroup   editorFontGroup;
  @FXML private RadioMenuItem editorFontSize8RadioMenuItem;
  @FXML private RadioMenuItem editorFontSize9RadioMenuItem;
  @FXML private RadioMenuItem editorFontSize10RadioMenuItem;
  @FXML private RadioMenuItem editorFontSize11RadioMenuItem;
  @FXML private RadioMenuItem editorFontSize12RadioMenuItem;

  @FXML private ToggleGroup   langFontGroup;
  @FXML private RadioMenuItem jaRadioMenuItem;
  @FXML private RadioMenuItem enRadioMenuItem;

  // table

  @FXML private TextField tableFilterTextField;
  @FXML private TableView<TextDB> tableView;
  @FXML private TableColumn<TextDB, String> iconColumn;
  @FXML private TableColumn<TextDB, String> nameColumn;
  @FXML private TableColumn<TextDB, String> textColumn;
  @FXML private TableColumn<TextDB, String> backgroundColumn;
  @FXML private TableColumn<TextDB, String> positionColumn;

  // textview

  @FXML private ImageView faceImageView;
  @FXML private TextField actorNameTextField;
  @FXML private GridPane colorPickerGridPane;
  @FXML private ImageView colorPickerImageView;
  @FXML private TextArea editorTextArea;
  @FXML private ComboBox<String> backgroundComboBox;
  @FXML private ComboBox<String> positionComboBox;

  // edit manager

  @FXML private Accordion  accordion;
  @FXML private TitledPane varTitledPane;
  @FXML private TitledPane actorTitledPane;
  @FXML private TitledPane iconsetTitledPane;

  @FXML private TextField                   varSearchTextField;
  @FXML private TableView<  VarDB>          varTableView;
  @FXML private TableColumn<VarDB, Integer> varIdColumn;
  @FXML private TableColumn<VarDB, String>  varNameColumn;

  @FXML private TextField                     actorSearchTextField;
  @FXML private TableView<  ActorDB>          actorTableView;
  @FXML private TableColumn<ActorDB, Integer> actorIdColumn;
  @FXML private TableColumn<ActorDB, String>  actorNameColumn;

  @FXML private GridPane  iconGridPane;
  @FXML private ImageView iconImageView;
  @FXML private Label     iconFocusLabel;
  @FXML private Label     iconSelectedLabel;

  // Button Bar

  @FXML private Button partyButton;
  @FXML private Button backslashButton;

  @FXML private Button goldButton;
  @FXML private Button showGoldButton;

  @FXML private Button fontUpButton;
  @FXML private Button fontDownButton;

  @FXML private Button wait1_4Button;
  @FXML private Button wait1Button;

  @FXML private Button showAllButton;
  @FXML private Button showStopButton;

  @FXML private Button waitInputButton;
  @FXML private Button nonWaitButton;

  //}}}

  // initialize

  @FXML private void initialize() {//{{{
    Locale locale = Locale.getDefault();
    if (!Locale.JAPAN.equals(locale)) {
      enRadioMenuItem.setSelected(true);
    }

    dc = new DirectoryChooser();
    dc.setInitialDirectory(new File("."));

    formatProperties      . load();
    preferencesProperties . load();
    logsProperties        . load();

    preferencesProperties.changeLanguages();

    tableFontSize8RadioMenuItem   . setOnAction(e -> changeTableViewFontSize("8" ));
    tableFontSize9RadioMenuItem   . setOnAction(e -> changeTableViewFontSize("9" ));
    tableFontSize10RadioMenuItem  . setOnAction(e -> changeTableViewFontSize("10"));
    tableFontSize11RadioMenuItem  . setOnAction(e -> changeTableViewFontSize("11"));
    tableFontSize12RadioMenuItem  . setOnAction(e -> changeTableViewFontSize("12"));

    editorFontSize8RadioMenuItem  . setOnAction(e -> changeEditorFontSize("8" ));
    editorFontSize9RadioMenuItem  . setOnAction(e -> changeEditorFontSize("9" ));
    editorFontSize10RadioMenuItem . setOnAction(e -> changeEditorFontSize("10"));
    editorFontSize11RadioMenuItem . setOnAction(e -> changeEditorFontSize("11"));
    editorFontSize12RadioMenuItem . setOnAction(e -> changeEditorFontSize("12"));

    myMenubar = new MyMenuBar(
        this
        , newMenuItem
        , openMenuItem
        , recentMenu
        , closeMenu
        , saveMenuItem
        , saveAsMenuItem
        , importMenuItem
        , exportMenuItem
        , importConfigsMenuItem
        , quitMenuItem
        );
    textTable = new TextTable(this
        , tableFilterTextField
        , tableView        , iconColumn       , nameColumn , textColumn
        , backgroundColumn , positionColumn);
    textView  = new TextView(this
        , faceImageView   , actorNameTextField , colorPickerGridPane , colorPickerImageView
        , editorTextArea  , backgroundComboBox , positionComboBox
        , partyButton     , backslashButton
        , goldButton      , showGoldButton
        , fontUpButton    , fontDownButton
        , wait1_4Button   , wait1Button
        , showAllButton   , showStopButton
        , waitInputButton , nonWaitButton
        );
    editManager = new EditManager(
        this
        , varSearchTextField   , varTableView   , varIdColumn    , varNameColumn
        , actorSearchTextField , actorTableView , actorIdColumn  , actorNameColumn
        , iconGridPane         , iconImageView  , iconFocusLabel , iconSelectedLabel
        );

    iconIndex1MenuItem.setOnAction(e -> textTable.changeIconIndex(0));
    iconIndex2MenuItem.setOnAction(e -> textTable.changeIconIndex(1));
    iconIndex3MenuItem.setOnAction(e -> textTable.changeIconIndex(2));
    iconIndex4MenuItem.setOnAction(e -> textTable.changeIconIndex(3));
    iconIndex5MenuItem.setOnAction(e -> textTable.changeIconIndex(4));
    iconIndex6MenuItem.setOnAction(e -> textTable.changeIconIndex(5));
    iconIndex7MenuItem.setOnAction(e -> textTable.changeIconIndex(6));
    iconIndex8MenuItem.setOnAction(e -> textTable.changeIconIndex(7));

    loadPreference();

  }//}}}

  // fxml event

  @FXML private void reloadMenuItemOnAction() { myMenubar.reloadXml(); }

  @FXML private void cutRecordsMenuItemOnAction()    { textTable.cutRecords(); }
  @FXML private void copyRecordsMenuItemOnAction()   { textTable.copyRecords(); }
  @FXML private void pasteRecordsMenuItemOnAction()  { textTable.pasteRecords(); }
  @FXML private void deleteRecordsMenuItemOnAction() { textTable.deleteRecords(); }

  @FXML private void selectPreviousMenuItemOnAction() { textTable.selectPrevious(); }
  @FXML private void selectNextMenuItemOnAction()     { textTable.selectNext(); }

  @FXML private void updateSelectedRecordsMenuItemOnAction() { textTable.updateSelectedRecords(); }
  @FXML private void addNewRecordMenuItemOnAction() { textTable.addNewRecord(); }
  @FXML private void formatMenuItemOnAction() { textTable.format(); }

  @FXML private void selectProjectMenuItemOnAction() {//{{{
    File dir = dc.showDialog(getWindow());
    if (dir != null) {
      dc.setInitialDirectory(dir.getParentFile());
      loadPreference();
    }
  }//}}}

  @FXML private void focusVarPane() {//{{{
    accordion.setExpandedPane(accordion.getPanes().get(0));
    editManager.focusVarPane();
  }//}}}

  @FXML private void focusActorPane() {//{{{
    accordion.setExpandedPane(accordion.getPanes().get(1));
    editManager.focusActorPane();
  }//}}}

  @FXML private void focusIconsetPane() {//{{{
    accordion.setExpandedPane(accordion.getPanes().get(2));
    editManager.focusIconsetPane();
  }//}}}

  @FXML private void updateActorNames() {//{{{
    String actorName = textView.getActorName();
    textTable.setActorNames(actorName);
  }//}}}

  @FXML private void updateTexts() {//{{{
    String text = textView.getText();
    textTable.setTexts(text);
  }//}}}

  @FXML private void importConfigsMenuItemOnAction() {//{{{
    ImportConfigStage ics = new ImportConfigStage();
    ics.showAndWait();
    formatProperties.load();
  }//}}}

  @FXML private void jpRadioMenuItemOnAction() {//{{{
    DialogUtils.showLanguageDialog();
    String langs = Locale.JAPAN.getLanguage();
    setLanguages(langs);
  }//}}}

  @FXML private void enRadioMenuItemOnAction() {//{{{
    DialogUtils.showLanguageDialog();
    String langs = Locale.ENGLISH.getLanguage();
    setLanguages(langs);
  }//}}}

  @FXML private void closeMenuItemOnAction() { //{{{
    myMenubar.close();
    textTable.clear();
  }//}}}

  @FXML private void quitMenuItemOnAction() { closeRequest(); }

  @FXML private void forcedTerminateMenuItemOnAction() {//{{{
    DialogUtils.showForcedTerminationDialog();
  }//}}}

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

  @FXML private void newMenuItemOnAction() {//{{{
    myMenubar.showAcceptDialog().ifPresent(r -> {
      String noname = Main.resources.getString("noname");
      Stage stg = (Stage) getWindow();
      stg.setTitle(noname + " - " +TITLE_VERSION);
      textTable.addInitRecord();
      myMenubar.initOpenedXml();
    });
  }//}}}

  // drag and drop

  @FXML private void rootOnDragOver(DragEvent e) {//{{{
    Dragboard board = e.getDragboard();
    if (board.hasFiles()) {
      e.acceptTransferModes(TransferMode.COPY);
    }
    e.consume();
  }//}}}

  @FXML private void rootOnDragDropped(DragEvent e) {//{{{
    Dragboard board = e.getDragboard();
    if (board.hasFiles()) {
      // ディレクトリが渡された時、対象プロジェクトを更新する。
      Optional<File> dirOpt = board.getFiles().stream().filter(f -> f.isDirectory()).findFirst();
      if (dirOpt.isPresent()) {
        File dir = dirOpt.get();
        String s = dir.getAbsolutePath() + File.separator + "Game.rpgproject";
        if (new File(s).exists()) {
          dc.setInitialDirectory(dir.getParentFile());
          preferencesProperties.setProperty(KEY_PROJECT, dir.getAbsolutePath());
          preferencesProperties.store();
          loadPreference();
        } else {
          Utils.showErrorDialog();
        }
        e.setDropCompleted(true);
        e.consume();
        return;
      }

      // テキストファイルが渡された場合、インポートする。
      Optional<File> txtOpt = board.getFiles().stream().filter(f -> f.isFile()).findFirst();
      if (txtOpt.isPresent()) {
        Pattern p = Pattern.compile("^.*\\.((?i)txt)");
        txtOpt.filter(f -> p.matcher(f.getName()).matches())
          .ifPresent(file -> {
            myMenubar.importFile(file);
          });

        e.setDropCompleted(true);
        e.consume();
        return;
      }

      // XMLファイルが渡された場合、開く
      Optional<File> xmlOpt = board.getFiles().stream().filter(f -> f.isFile()).findFirst();
      if (xmlOpt.isPresent()) {
        Pattern p = Pattern.compile("^.*\\.((?i)xml)");
        xmlOpt.filter(f -> p.matcher(f.getName()).matches())
          .ifPresent(file -> {
            myMenubar.openXml(file);
            myMenubar.setRecentFile(file);
          });

        e.setDropCompleted(true);
        e.consume();
        return;
      }
    }

    e.setDropCompleted(false);
    e.consume();
  }//}}}

  // public methods

  public void closeRequest() {//{{{
    Main.mainMp.setProperty(tableView);
    Main.mainMp.store();
    formatProperties.store();

    for (int i=0; i<recentMenu.getItems().size(); i++) {
      logsProperties.setProperty("log" + i, recentMenu.getItems().get(i).getText());
    }

    preferencesProperties.store();
    logsProperties       .store();

    Platform.exit();
  }//}}}

  public void updateActorNameOfTable(  String value) { textTable.setActorName(value);  }
  public void updateTextOfTable(       String value) { textTable.setText(value);       }
  public void updateBackgroundOfTable( String value) { textTable.setBackground(value); }
  public void updatePositionOfTable(   String value) { textTable.setPosition(value);   }

  public void updateTextView(TextDB db) { textView.update(db); }
  public void saveXml(File file) throws ParserConfigurationException { textTable.saveXml(file); }

  public void setTextList(List<List<String>> listList) { textTable.setTextList(listList); }
  public void setTextDB(List<TextDB> dbs) { textTable.setTextDB(dbs); }

  public void insertVarId(     int id) { textView.insertVarId(     id) ; }
  public void insertActorId(   int id) { textView.insertActorId(   id) ; }
  public void insertIconSetId( int id) { textView.insertIconSetId( id) ; }

  public void exportJson(File file) throws FileNotFoundException, IOException {
    textTable.exportJson(file);
  }

  void changeFontSizes() {//{{{
    preferencesProperties.getProperty(TABLE_VIEW_FONT_SIZE).map(s -> Integer.parseInt(s) - 8)
      .ifPresent(index -> {
        tableFontGroup.getToggles().get(index).setSelected(true);
        changeTableViewFontSize("" + (index+8));
      });

    preferencesProperties.getProperty(EDITOR_FONT_SIZE).map(s -> Integer.parseInt(s) - 8)
      .ifPresent(index -> {
        editorFontGroup.getToggles().get(index).setSelected(true);
        changeEditorFontSize("" + (index+8));
      });
  }//}}}

  public void focusTextView() { textView.focusEditor(); }

  // getter

  public Window getWindow() { return tableView.getScene().getWindow(); }

  // setter

  void setBackgroundItem(String item) {//{{{
    String[] array = item.split(",");
    backgroundComboBox.getItems().addAll(array);
  }//}}}

  void setPositionItem(String item) {//{{{
    String[] array = item.split(",");
    positionComboBox.getItems().addAll(array);
  }//}}}

  void setRecentFiles() {//{{{
    for (int i=1; i<=RecentFilesUtils.MAX; i++) {
      logsProperties.getProperty("log" + i).ifPresent(path -> {
        if (RecentFilesUtils.EMPTY.equals(path)) return;
        File file = new File(path);
        myMenubar.setRecentFile(file);
      });
    }
  }//}}}

  // private methods

  private void loadPreference() {//{{{
    preferencesProperties.getProperty(KEY_PROJECT).ifPresent(proj -> {
      textView.setColorPickerImage(proj + "/" + IMG_WINDOW_PATH);
      editManager.setVariables(proj + "/" + VAR_FILE_PATH);
      editManager.setActors(proj + "/" + ACTORS_FILE_PATH);
      editManager.setIconset(proj + "/" + IMG_ICONSET_PATH);
    });
  }//}}}

  private void setLanguages(String langs) {//{{{
    preferencesProperties.setProperty(TABLE_VIEW_FONT_SIZE, langs);
  }//}}}

  private void changeTableViewFontSize(String fontSize) {//{{{
    TableView target = (TableView) tableView.getScene().lookup("#tableView");
    target.setStyle("-fx-font-size:" + fontSize + "pt;");
    preferencesProperties.setProperty(TABLE_VIEW_FONT_SIZE, fontSize);
  }//}}}

  private void changeEditorFontSize(String fontSize) {//{{{
    TextArea target = (TextArea) tableView.getScene().lookup("#editorTextArea");
    target.setStyle("-fx-font-size:" + fontSize + "pt;");
    preferencesProperties.setProperty(EDITOR_FONT_SIZE, fontSize);
  }//}}}

}

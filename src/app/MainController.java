package app;

import static util.Texts.*;

import jiro.java.util.MyProperties;
import jiro.javafx.stage.AboutStage;
import jiro.javafx.stage.MyFileChooser;

import app.manager.ActorDB;
import app.manager.VarDB;
import app.manager.EditManager;
import app.table.TextDB;
import app.table.TextTable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javax.xml.parsers.ParserConfigurationException;

public class MainController {

  public static MyProperties formatProperties;
  public static MyProperties preferencesProperties;

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
  @FXML private MenuItem preferencesMenuItem;
  @FXML private MenuItem quitMenuItem;

  // table

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
    formatProperties = new MyProperties(FORMAT_PROPERTIES);
    formatProperties.load();

    preferencesProperties = new MyProperties(PREFERENCES_PROPERTIES);
    preferencesProperties.load();
    preferencesProperties.changeLanguages();

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
        , preferencesMenuItem
        , quitMenuItem
        );
    textTable = new TextTable(this
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

    // TODO
    preferencesProperties.getProperty(KEY_PROJECT).ifPresent(proj -> {
      // ColorPickerのシステム画像を一時的に読み込み
      textView.setColorPickerImage(proj + "/" + IMG_WINDOW_PATH);
      // 変数一覧の一時読み込み
      editManager.setVariables(proj + "/" + VAR_FILE_PATH);
      // アクター一覧の一時読み込み
      editManager.setActors(proj + "/" + ACTORS_FILE_PATH);
      // アイコンセット一覧の一時読み込み
      editManager.setIconset(proj + "/" + IMG_ICONSET_PATH);
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

  public void updateTextView(TextDB db) { textView.update(db); }
  public void saveXml(File file) { textTable.saveXml(file); }

  public void setTextList(List<List<String>> listList) throws ParserConfigurationException {//{{{
    textTable.setTextList(listList);
  }//}}}

  public void insertVarId(     int id) { textView.insertVarId(     id) ; }
  public void insertActorId(   int id) { textView.insertActorId(   id) ; }
  public void insertIconSetId( int id) { textView.insertIconSetId( id) ; }

}

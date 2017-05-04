package app.table;

import static util.Texts.*;

import app.selector.ImageSelector;
import jiro.java.util.MyProperties;

import app.Main;
import app.MainController;
import util.JsonUtils;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javax.xml.parsers.ParserConfigurationException;

public class TextTable {

  private final MainController mainController;
  private Optional<List<TextDB>> copyTextDBs = Optional.empty();

  private static final String CR = System.lineSeparator();

  // fxml component {{{
  private final TextField tableFilterTextField;
  private final TableView<TextDB>           tableView;
  private final TableColumn<TextDB, String> iconColumn;
  private final TableColumn<TextDB, String> actorNameColumn;
  private final TableColumn<TextDB, String> textColumn;
  private final TableColumn<TextDB, String> backgroundColumn;
  private final TableColumn<TextDB, String> positionColumn;
  //}}}

  // 変数パネルの検索される元のデータベース
  private final ObservableList<TextDB> masterData;

  public TextTable(
      MainController mc
      , TextField tftf
      , TableView<TextDB> tv
      , TableColumn<TextDB, String> ic
      , TableColumn<TextDB, String> anc
      , TableColumn<TextDB, String> tc
      , TableColumn<TextDB, String> bgc
      , TableColumn<TextDB, String> pc
      )
  {//{{{

    mainController       = mc;
    tableFilterTextField = tftf;
    tableView            = tv;
    iconColumn           = ic;
    actorNameColumn      = anc;
    textColumn           = tc;
    backgroundColumn     = bgc;
    positionColumn       = pc;
    masterData = FXCollections.observableArrayList();

    iconColumn       . setCellValueFactory(new PropertyValueFactory<TextDB, String>("icon"));
    actorNameColumn  . setCellValueFactory(new PropertyValueFactory<TextDB, String>("actorName"));
    textColumn       . setCellValueFactory(new PropertyValueFactory<TextDB, String>("text"));
    backgroundColumn . setCellValueFactory(new PropertyValueFactory<TextDB, String>("background"));
    positionColumn   . setCellValueFactory(new PropertyValueFactory<TextDB, String>("position"));

    iconColumn.setCellFactory(col -> new ImageTableCell());

    tableView.getFocusModel().focusedCellProperty().addListener((obs, oldVal, newVal) -> {
      updateTextView();
    });

    tableView.setOnMouseClicked(e -> {
      if (e.getClickCount() == 2) {
        openImageSelector();
      }
    });

    tableView.setOnKeyPressed(e -> {
      if (KeyCode.J == e.getCode() && !e.isControlDown()) {
        selectNext();
      } else if (KeyCode.K == e.getCode() && !e.isControlDown()) {
        selectPrevious();
      } else if (KeyCode.ENTER == e.getCode()) {
        openImageSelector();
      }
    });

    tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    //tableView.getSelectionModel().setCellSelectionEnabled(true);

    masterData.add(createInitTextDB());

    // テーブルのフィルタリング
    FilteredList<TextDB> filteredData = new FilteredList<>(masterData, p -> true);
    tableFilterTextField.textProperty().addListener((obs, oldVal, newVal) -> {
      filteredData.setPredicate(db -> existsMatchedText(db, newVal));
    });
    tableView.setItems(filteredData);

    tableView.getSelectionModel().selectFirst();
  }//}}}

  // public methods

  public void saveXml(File file) throws ParserConfigurationException {//{{{
    SavingData data = new SavingData(tableView.getItems());
    data.saveXml(file);
  }//}}}

  public void exportJson(File file) throws FileNotFoundException, IOException {//{{{
    JsonMap data = new JsonMap(1, new ArrayList<>(masterData));
    JsonUtils.writeValue(file, data);

    MainController.preferencesProperties.getProperty("project").ifPresent(dir -> {
      try {
        final String SP = File.separator;
        JsonUtils.updateMapInfos(new File(dir + SP + "data" + SP + "MapInfos.json"), file.getName());
      } catch (IOException e) {
        util.MyLogger.log("ファイル出力に失敗しましたエラー", e);
      } catch (Exception e) {
        util.MyLogger.log(e);
      }
    });
  }//}}}

  public void changeIconIndex(int index) {//{{{
    getSelectedItems().ifPresent(selectedItems -> {
      selectedItems.stream().forEach(selectedItem -> {
        selectedItem.setIconIndex(index);
      });
    });
    updateTextView();
  }//}}}

  public void cutRecords() {//{{{
    copyRecords();
    deleteRecords();
  }//}}}

  public void copyRecords() {//{{{
    getSelectedItems().ifPresent(selectedItems -> {
      copyTextDBs = Optional.ofNullable(new ArrayList<>(selectedItems));
    });
  }//}}}

  public void pasteRecords() {//{{{
    getSelectedItems().ifPresent(i -> {
      copyTextDBs.map(l -> l.stream()
          .map(TextDB::new)
          .collect(Collectors.toList())
          )
        .ifPresent(records -> {
          int index = tableView.getSelectionModel().getSelectedIndices().get(0);
          masterData.addAll(index+1, records);
        });
    });
  }//}}}

  public void deleteRecords() {//{{{
    int index = tableView.getSelectionModel().getSelectedIndices().get(0);
    getSelectedItems().ifPresent(selectedItems -> {
      masterData.removeAll(selectedItems);
    });
    if (0 < index) selectNext();
    if (masterData.size() < 1) {
      masterData.add(createInitTextDB());
      tableView.getSelectionModel().selectFirst();
    }
  }//}}}

  public void selectPrevious() {//{{{
    int index = tableView.getSelectionModel().getSelectedIndex();
    index = Math.max(0, --index);
    tableView.getSelectionModel().clearAndSelect(index);
    tableView.scrollTo(index);
  }//}}}

  public void selectNext() {//{{{
    int index = tableView.getSelectionModel().getSelectedIndex();
    int max = tableView.getItems().size();
    index = Math.min(++index, max);
    tableView.getSelectionModel().clearAndSelect(index);
    tableView.scrollTo(index);
  }//}}}

  public void updateSelectedRecords() {//{{{
    getSelectedItems().ifPresent(items -> {
      getSelectedItem().ifPresent(si -> {
        TextDB db = si;
        items.stream().forEach(item -> {
          TextDB newDb = new TextDB(db);
          item . setIcon(newDb       . iconProperty()       . get());
          item . setActorName(newDb  . actorNameProperty()  . get());
          item . setText(newDb       . textProperty()       . get());
          item . setBackground(newDb . backgroundProperty() . get());
          item . setPosition(newDb   . positionProperty()   . get());
        });
      });
    });
  }//}}}

  public void addNewRecord() {//{{{
    getSelectedItem().ifPresent(si -> {
      int index = tableView.getSelectionModel().getSelectedIndex();
      masterData.add(index+1, new TextDB());
    });
  }//}}}

  private String mkReturnedString(int size, String text, boolean bra, String indent, boolean facePathExists) {//{{{
    int len = len(text);
    if (len <= size) {
      return text;
    }

    // FIXME
    // 文章の折り返しが正常に行われない。
    // 画像がない段階で折り返されていた時、
    // 後から画像を追加してフォーマットしようとしても
    // 折り返しが実行されない
    // 後から文章を追加して上限を超えるようにすると
    // 上限が減少して整形が実行される。
    if (facePathExists) size -= FACE_FONT_SIZE;

    int cnt = 0;
    StringBuilder sb = new StringBuilder();
    for (String s : text.split("")) {
      int a = len(s);
      cnt += a;
      sb.append(s);
      if (size < cnt) {
        sb.append(CR);
        cnt = 0;

        if (bra) {
          sb.append(indent);
          cnt += len(indent);
        }
      }
    }
    return sb.toString();
  }//}}}

  public void format() {//{{{
    getSelectedItems().ifPresent(items -> {
      MyProperties mp = MainController.formatProperties;
      mp.getProperty("textReturnSize").ifPresent(sizeStr -> {
        int size = Integer.parseInt(sizeStr);

        String textIndentStr = mp.getProperty("textIndent").get();
        String braS          = mp.getProperty("textBracket").get();
        String braStart      = mp.getProperty("bracketStart").get();

        boolean textIndent = textIndentStr == null ? true  : Boolean.valueOf(textIndentStr);
        boolean bra        = braS          == null ? false : Boolean.valueOf(braS);
        int braLen         = braStart      == null ? 0     : len(braStart);

        StringBuilder indentSb = new StringBuilder();
        if (textIndent) {
          for (int i=0; i<braLen; i++) {
            indentSb.append(" ");
          }
        }
        String indent = indentSb.toString();

        items.stream().forEach(item -> {
          String icon = item.iconProperty().get().replaceAll("\\s", "");
          boolean facePathExists = !Objects.equals(icon, "");
          String text = item.textProperty().get();

          BufferedReader br = new BufferedReader(new StringReader(text));
          List<String> lines = br.lines()
            .map(l -> mkReturnedString(size, l, bra, indent, facePathExists))
            .collect(Collectors.toList());

          String formattedText = String.join(CR, lines);
          item.setText(formattedText);
          updateTextView();
        });
      });
    });
  }//}}}

  // private methods

  private Optional<TextDB> getSelectedItem() {//{{{
    SelectionModel<TextDB> model = tableView.getSelectionModel();
    if (!model.isEmpty()) {
      return Optional.ofNullable(model.getSelectedItem());
    }
    return Optional.empty();
  }//}}}

  private Optional<ObservableList<TextDB>> getSelectedItems() {//{{{
    MultipleSelectionModel<TextDB> model = tableView.getSelectionModel();
    if (!model.isEmpty()) {
      List<TextDB> list = model.getSelectedItems().stream().collect(Collectors.toList());
      ObservableList<TextDB> selectedItems = FXCollections.observableArrayList(list);
      return Optional.ofNullable(selectedItems);
    }
    return Optional.empty();
  }//}}}

  private void updateTextView() {//{{{
    getSelectedItem().ifPresent(item -> {
      mainController.updateTextView(item);
    });
  }//}}}

  // setter

  public void setTextDB(List<TextDB> dbs) {//{{{
    tableView.getSelectionModel().clearSelection();
    masterData.clear();
    dbs.stream().forEach(db -> {
      masterData.add(db);
    });
  }//}}}

  public void setTextList(List<List<String>> listList) {//{{{
    tableView.getSelectionModel().clearSelection();
    masterData.clear();
    listList.stream().forEach(list -> {
      String bg  = getBackgroundInitText();
      String pos = getPositionInitText();
      masterData.add(new TextDB("", list, bg, pos));
    });
  }//}}}

  // データベースのsetter

  public void setActorName(String value) {//{{{
    getSelectedItem().ifPresent(selectedItem -> {
      selectedItem.setActorName(value);
    });
  }//}}}

  public void setText(String value) {//{{{
    getSelectedItem().ifPresent(selectedItem -> {
      selectedItem.setText(value);
    });
  }//}}}

  public void setBackground(String value) {//{{{
    getSelectedItem().ifPresent(selectedItem -> {
      selectedItem.setBackground(value);
    });
  }//}}}

  public void setPosition(String value) {//{{{
    getSelectedItem().ifPresent(selectedItem -> {
      selectedItem.setPosition(value);
    });
  }//}}}

  public void setActorNames(String value) {//{{{
    getSelectedItems().ifPresent(items -> {
      items.stream().forEach(item -> {
        item.setActorName(value);
      });
    });
  }//}}}

  public void setTexts(String value) {//{{{
    getSelectedItems().ifPresent(items -> {
      items.stream().forEach(item -> {
        item.setText(value);
      });
    });
  }//}}}

  // private methods

  private boolean existsMatchedText(TextDB db, String newVal) {//{{{
    if (newVal == null || newVal.isEmpty()) {
      return true;
    }

    String lowerCaseFilter = newVal.toLowerCase();

    String actorName = db . actorNameProperty()  . get();
    String text      = db . textProperty()       . get();
    String bg        = db . backgroundProperty() . get();
    String pos       = db . positionProperty()   . get();

    if (
        actorName. toLowerCase() . contains(lowerCaseFilter)
        || text  . toLowerCase() . contains(lowerCaseFilter)
        || bg    . toLowerCase() . contains(lowerCaseFilter)
        || pos   . toLowerCase() . contains(lowerCaseFilter)
       ) {
      return true;
       }
    return false;
  }//}}}

  private TextDB createInitTextDB() {//{{{
    String bg  = getBackgroundInitText();
    String pos = getPositionInitText();
    return new TextDB("", "", "", bg, pos);
  }//}}}

  private void openImageSelector() {//{{{
    getSelectedItems().ifPresent(items -> {
      TextDB db = items.get(0);
      String icon = db.iconProperty().get();
      if (icon != null || icon.length() == 0) {
        String path = createFilePath(icon.split(":"));
        ImageSelector selector = new ImageSelector(path);
        selector.showAndWait();
        boolean selected = selector.isSelected();

        if (selected) {
          selector.getSelectedTileString().ifPresent(s -> {
            items.stream().forEach(item -> {
              item.setIcon(s);
              updateTextView();
            });
          });
        }
      }
    });
  }//}}}

}


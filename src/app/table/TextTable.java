package app.table;

import static util.Texts.*;

import app.selector.ImageSelector;

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
import javax.xml.parsers.ParserConfigurationException;

public class TextTable {

  private final MainController mainController;
  private Optional<List<TextDB>> copyTextDBs = Optional.empty();

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
        getSelectedItems().ifPresent(items -> {
          TextDB db = items.get(0);
          String icon = db.iconProperty().get();
          if (icon != null || icon.length() == 0) {
            String path = createFilePath(icon.split(":"));
            ImageSelector selector = new ImageSelector(path);
            selector.showAndWait();

            selector.getSelectedTileString().ifPresent(s -> {
              items.stream().forEach(item -> {
                item.setIcon(s);
                updateTextView();
              });
            });
          }
        });
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

  }//}}}

  public void saveXml(File file) throws ParserConfigurationException {//{{{
    SavingData data = new SavingData(tableView.getItems());
    data.saveXml(file);
  }//}}}

  public void exportJson(File file) throws FileNotFoundException, IOException {//{{{
    JsonMap data = new JsonMap(1, 5, 5, new ArrayList<>(masterData));
    JsonUtils.writeValue(file, data);
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
          masterData.addAll(index, records);
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
    }
  }//}}}

  public void selectPrevious() {//{{{
    int index = tableView.getSelectionModel().getSelectedIndex();
    index = Math.max(0, --index);
    tableView.getSelectionModel().clearAndSelect(index);
  }//}}}

  public void selectNext() {//{{{
    int index = tableView.getSelectionModel().getSelectedIndex();
    int max = tableView.getItems().size();
    index = Math.min(++index, max);
    tableView.getSelectionModel().clearAndSelect(index);
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
      masterData.add(index, new TextDB());
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

}


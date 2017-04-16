package app.table;

import static util.Texts.*;

import app.selector.ImageSelector;

import app.Main;
import app.MainController;

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

  private final TextField tableFilterTextField;
  private final TableView<TextDB>           tableView;
  private final TableColumn<TextDB, String> iconColumn;
  private final TableColumn<TextDB, String> actorNameColumn;
  private final TableColumn<TextDB, String> textColumn;
  private final TableColumn<TextDB, String> backgroundColumn;
  private final TableColumn<TextDB, String> positionColumn;

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
    tableView.getSelectionModel().setCellSelectionEnabled(true);

    String bg  = getBackgroundInitText();
    String pos = getPositionInitText();
    masterData.add(new TextDB("", "", "", bg, pos));

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

  public void setActorName(String actorName) {//{{{
    getSelectedItem().ifPresent(selectedItem -> {
      selectedItem.setActorName(actorName);
    });
  }//}}}

  public void setText(String text) {//{{{
    getSelectedItem().ifPresent(selectedItem -> {
      selectedItem.setText(text);
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

}


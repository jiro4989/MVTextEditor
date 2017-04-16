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
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javax.xml.parsers.ParserConfigurationException;

public class TextTable {

  private final MainController mainController;

  private final TableView<TextDB>           tableView;
  private final TableColumn<TextDB, String> iconColumn;
  private final TableColumn<TextDB, String> actorNameColumn;
  private final TableColumn<TextDB, String> textColumn;
  private final TableColumn<TextDB, String> backgroundColumn;
  private final TableColumn<TextDB, String> positionColumn;

  public TextTable(
      MainController mc
      , TableView<TextDB> tv
      , TableColumn<TextDB, String> ic
      , TableColumn<TextDB, String> anc
      , TableColumn<TextDB, String> tc
      , TableColumn<TextDB, String> bgc
      , TableColumn<TextDB, String> pc
      )
  {//{{{

    mainController   = mc;
    tableView        = tv;
    iconColumn       = ic;
    actorNameColumn  = anc;
    textColumn       = tc;
    backgroundColumn = bgc;
    positionColumn   = pc;

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

    String background = Main.resources.getString("background");
    background = background.split(",")[0];
    String position = Main.resources.getString("position");
    position = position.split(",")[0];
    tableView.getItems().add(new TextDB("", "", "", background, position));

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
    tableView.getItems().clear();
    dbs.stream().forEach(db -> {
      tableView.getItems().add(db);
    });
  }//}}}

  public void setTextList(List<List<String>> listList) {//{{{
    listList.stream().forEach(list -> {
      tableView.getItems().add(new TextDB("", list, "ウィンドウ", "下"));
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

}


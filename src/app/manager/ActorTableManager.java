package app.manager;

import static util.Texts.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.MainController;
import app.manager.ActorDB;

import java.io.*;
import java.util.stream.*;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

class ActorTableManager {

  private final MainController mainController;

  private final TextField actorSearchTextField;
  private final TableView<  ActorDB>          actorTableView;
  private final TableColumn<ActorDB, Integer> actorIdColumn;
  private final TableColumn<ActorDB, String>  actorNameColumn;

  // 変数パネルの検索される元のデータベース
  private final ObservableList<ActorDB> masterActorDBs = FXCollections.observableArrayList();

  ActorTableManager(
      MainController mc
      , TextField tf
      , TableView<ActorDB> tv
      , TableColumn<ActorDB, Integer> aic
      , TableColumn<ActorDB, String> anc
      )
  {//{{{
    mainController     = mc;
    actorSearchTextField = tf;
    actorTableView       = tv;
    actorIdColumn        = aic;
    actorNameColumn      = anc;

    actorIdColumn   . setCellValueFactory(new PropertyValueFactory<ActorDB, Integer>("id"));
    actorNameColumn . setCellValueFactory(new PropertyValueFactory<ActorDB, String>("name"));

    // テーブルのフィルタリング
    FilteredList<ActorDB> filteredData = new FilteredList<>(masterActorDBs, p -> true);
    actorSearchTextField.textProperty().addListener((obs, oldVal, newVal) -> {
      filteredData.setPredicate(actorDb -> existsMatchedText(actorDb, newVal));
    });
    actorTableView.setItems(filteredData);

    // ダブルクリックでTextViewに文字列を挿入
    actorTableView.setOnMouseClicked(e -> {
      if (e.getClickCount() == 2) {
        insertActorId();
      }
    });

    actorTableView.setOnKeyPressed(e -> {
      if (KeyCode.J == e.getCode()
          && !e.isControlDown()
          && !e.isShiftDown()
         )
      {
        actorTableView.getSelectionModel().selectNext();
      } else if (KeyCode.K == e.getCode()
          && !e.isControlDown()
          && !e.isShiftDown()
          )
      {
        actorTableView.getSelectionModel().selectPrevious();
      } else if (KeyCode.ENTER == e.getCode()) {
        insertActorId();
      }
    });
  }//}}}

  void focus() {//{{{
    actorTableView.requestFocus();
    if (actorTableView.getSelectionModel().isEmpty()) {
      actorTableView.getSelectionModel().selectFirst();
    }
  }//}}}

  private boolean existsMatchedText(ActorDB actorDb, String newVal) {//{{{
    if (newVal == null || newVal.isEmpty()) {
      return true;
    }

    String lowerCaseFilter = newVal.toLowerCase();
    int vi      = actorDb.getId();
    String id   = String.valueOf(vi);
    String name = actorDb.getName();

    if (id.contains(lowerCaseFilter)) {
      return true;
    } else if (name.toLowerCase().contains(lowerCaseFilter)) {
      return true;
    }
    return false;
  }//}}}

  private void insertActorId() {//{{{
    SelectionModel<ActorDB> model = actorTableView.getSelectionModel();
    if (!model.isEmpty()) {
      ActorDB selectedItem = model.getSelectedItem();
      int actorId = selectedItem.getId();
      mainController.insertActorId(actorId);
    }
  }//}}}

  // setter

  public void setActors(String path) {//{{{
    try {
      File file           = new File(path);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root       = mapper.readTree(file);
      int size = root.size();

      IntStream.range(1, size).forEach(i -> {
        JsonNode child = root.get(i);
        int id      = child.get("id")  .asInt();
        String name = child.get("name").asText();
        masterActorDBs.add(new ActorDB(i, name));
      });
    } catch (IOException e) {
      util.MyLogger.log(e);
    }
  }//}}}

}

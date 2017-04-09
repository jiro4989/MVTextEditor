package app.manager;

import static util.Texts.*;
import static java.util.stream.IntStream.range;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.MainController;

import java.io.*;
import java.util.*;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class EditManager {

  private final MainController mainController;

  // 変数
  private final TextField varSearchTextField;
  private final TableView<  VarDB>          varTableView;
  private final TableColumn<VarDB, Integer> varIdColumn;
  private final TableColumn<VarDB, String>  varNameColumn;

  // アクター
  private final TextField actorSearchTextField;
  private final TableView<  ActorDB>          actorTableView;
  private final TableColumn<ActorDB, Integer> actorIdColumn;
  private final TableColumn<ActorDB, String>  actorNameColumn;

  private final IconSetManager iconSetManager;

  // 変数パネルの検索される元のデータベース
  private final ObservableList<VarDB> masterVarDBs = FXCollections.observableArrayList();

  // 変数パネルの検索される元のデータベース
  private final ObservableList<ActorDB> masterActorDBs = FXCollections.observableArrayList();

  public EditManager(
      MainController mainController

      , TextField                   varSearchTextField
      , TableView<  VarDB>          varTableView
      , TableColumn<VarDB, Integer> varIdColumn
      , TableColumn<VarDB, String>  varNameColumn

      , TextField                     actorSearchTextField
      , TableView<  ActorDB>          actorTableView
      , TableColumn<ActorDB, Integer> actorIdColumn
      , TableColumn<ActorDB, String>  actorNameColumn

      , GridPane  iconGridPane
      , ImageView iconImageView
      , Label     iconFocusLabel
      , Label     iconSelectedLabel

      )
  {//{{{

    this.mainController = mainController;

    this.varSearchTextField = varSearchTextField;
    this.varTableView       = varTableView;
    this.varIdColumn        = varIdColumn;
    this.varNameColumn      = varNameColumn;

    this.actorSearchTextField = actorSearchTextField;
    this.actorTableView       = actorTableView;
    this.actorIdColumn        = actorIdColumn;
    this.actorNameColumn      = actorNameColumn;

    iconSetManager = new IconSetManager(iconGridPane, iconImageView, iconFocusLabel, iconSelectedLabel);

    this . varIdColumn     . setCellValueFactory(new PropertyValueFactory<VarDB, Integer>("id"));
    this . varNameColumn   . setCellValueFactory(new PropertyValueFactory<VarDB, String>("name"));
    this . actorIdColumn   . setCellValueFactory(new PropertyValueFactory<ActorDB, Integer>("id"));
    this . actorNameColumn . setCellValueFactory(new PropertyValueFactory<ActorDB, String>("name"));

    // 変数テーブルに検索フィルタを実装 //{{{

    {
      FilteredList<VarDB> filteredData = new FilteredList<>(masterVarDBs, p -> true);
      this.varSearchTextField.textProperty().addListener((obs, oldVal, newVal) -> {
        filteredData.setPredicate(varDb -> existsMatchedText(varDb, newVal));
      });

      varTableView.setOnMouseClicked(e -> {
        if (e.getClickCount() == 2) {
          insertVarId();
        }
      });

      varTableView.setItems(filteredData);
    }

    //}}}

    // アクターテーブルに検索フィルタを実装 //{{{

    {
      FilteredList<ActorDB> filteredData = new FilteredList<>(masterActorDBs, p -> true);
      this.actorSearchTextField.textProperty().addListener((obs, oldVal, newVal) -> {
        filteredData.setPredicate(actorDb -> existsMatchedText(actorDb, newVal));
      });

      actorTableView.setOnMouseClicked(e -> {
        if (e.getClickCount() == 2) {
          insertActorId();
        }
      });

      actorTableView.setItems(filteredData);
    }

    //}}}

  }//}}}

  private boolean existsMatchedText(VarDB varDb, String newVal) {//{{{
    if (newVal == null || newVal.isEmpty()) {
      return true;
    }

    String lowerCaseFilter = newVal.toLowerCase();
    int vi         = varDb.idProperty().get();
    String varId   = String.valueOf(vi);
    String varName = varDb.nameProperty().get();

    if (varId.contains(lowerCaseFilter)) {
      return true;
    } else if (varName.toLowerCase().contains(lowerCaseFilter)) {
      return true;
    }
    return false;
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

  private void insertVarId() {//{{{
    SelectionModel<VarDB> model = varTableView.getSelectionModel();
    if (!model.isEmpty()) {
      VarDB selectedItem = model.getSelectedItem();
      int varId = selectedItem.idProperty().get();
      mainController.insertVarId(varId);
    }
  }//}}}

  private void insertActorId() {//{{{
    SelectionModel<ActorDB> model = actorTableView.getSelectionModel();
    if (!model.isEmpty()) {
      ActorDB selectedItem = model.getSelectedItem();
      int varId = selectedItem.getId();
      mainController.insertActorId(varId);
    }
  }//}}}

  // setter

  public void setVariables(String path) {//{{{
    try {
      File file           = new File(path);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root       = mapper.readTree(file);
      JsonNode child      = root.get(KEY_VAR);
      int size = child.size();

      range(1, size).forEach(i -> {
        String name = child.get(i).asText();
        masterVarDBs.add(new VarDB(i, name));
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }//}}}

  public void setActors(String path) {//{{{
    try {
      File file           = new File(path);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root       = mapper.readTree(file);
      int size = root.size();

      range(1, size).forEach(i -> {
        JsonNode child = root.get(i);
        int id      = child.get("id")  .asInt();
        String name = child.get("name").asText();
        masterActorDBs.add(new ActorDB(i, name));
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }//}}}

  public void setIconset(String path) { iconSetManager.setImage(path); }

}

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
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class EditManager {

  private final MainController mainController;

  private final TextField searchTextField;
  private final TableView<VarDB>            varTableView;
  private final TableColumn<VarDB, Integer> varIdColumn;
  private final TableColumn<VarDB, String>  varNameColumn;

  // 検索される元のデータベース
  private final ObservableList<VarDB> masterData = FXCollections.observableArrayList();

  public EditManager(
      MainController mainController
      , TextField searchTextField
      , TableView<VarDB> varTableView
      , TableColumn<VarDB, Integer> varIdColumn
      , TableColumn<VarDB, String> varNameColumn
      )
  {//{{{
    this.mainController  = mainController;
    this.searchTextField = searchTextField;
    this.varTableView    = varTableView;
    this.varIdColumn     = varIdColumn;
    this.varNameColumn   = varNameColumn;

    this.varIdColumn  .setCellValueFactory(new PropertyValueFactory<VarDB, Integer>("id"));
    this.varNameColumn.setCellValueFactory(new PropertyValueFactory<VarDB, String>("name"));

    // フィルタリング
    FilteredList<VarDB> filteredData = new FilteredList<>(masterData, p -> true);
    this.searchTextField.textProperty().addListener((obs, oldVal, newVal) -> {
      filteredData.setPredicate(varDb -> existsMatchedText(varDb, newVal));
    });

    varTableView.setOnMouseClicked(e -> {
      if (e.getClickCount() == 2) {
        insertVarId();
      }
    });

    varTableView.setItems(filteredData);
  }//}}}

  /**
   * System.jsonファイルから変数のデータを読み取る。
   * @param path System.jsonのパス
   */
  public void setVariables(String path) {//{{{
    try {
      File file = new File(path);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(file);
      JsonNode child = root.get(KEY_VAR);
      int size = child.size();

      List<String> list = new ArrayList<>(size);
      range(1, size).forEach(i -> {
        String name = child.get(i).asText();
        masterData.add(new VarDB(i, name));
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }//}}}

  /**
   * newValがvarDbの中から検索し、マッチするものが有るかどうかの結果を返却する。
   * @param varDb 対象データベース
   * @param newVal 検索文字列
   * @return マッチしたか否か
   */
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

  private void insertVarId() {//{{{
    SelectionModel<VarDB> model = varTableView.getSelectionModel();
    if (!model.isEmpty()) {
      VarDB selectedItem = model.getSelectedItem();
      int varId = selectedItem.idProperty().get();
      mainController.insertVarId(varId);
    }
  }//}}}

}

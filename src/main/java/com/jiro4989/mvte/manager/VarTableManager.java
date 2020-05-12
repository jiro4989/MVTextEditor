package com.jiro4989.mvte.manager;

import static util.Texts.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiro4989.mvte.MainController;
import java.io.*;
import java.util.stream.*;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

class VarTableManager {

  private final MainController mainController;

  private final TextField varSearchTextField;
  private final TableView<VarDB> varTableView;
  private final TableColumn<VarDB, Integer> varIdColumn;
  private final TableColumn<VarDB, String> varNameColumn;

  // 変数パネルの検索される元のデータベース
  private final ObservableList<VarDB> masterVarDBs = FXCollections.observableArrayList();

  VarTableManager(
      MainController mc,
      TextField tf,
      TableView<VarDB> tv,
      TableColumn<VarDB, Integer> vic,
      TableColumn<VarDB, String> vnc) { // {{{
    mainController = mc;
    varSearchTextField = tf;
    varTableView = tv;
    varIdColumn = vic;
    varNameColumn = vnc;

    varIdColumn.setCellValueFactory(new PropertyValueFactory<VarDB, Integer>("id"));
    varNameColumn.setCellValueFactory(new PropertyValueFactory<VarDB, String>("name"));

    // テーブルのフィルタリング
    FilteredList<VarDB> filteredData = new FilteredList<>(masterVarDBs, p -> true);
    varSearchTextField
        .textProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              filteredData.setPredicate(varDb -> existsMatchedText(varDb, newVal));
            });
    varTableView.setItems(filteredData);

    // ダブルクリックでTextViewに文字列を挿入
    varTableView.setOnMouseClicked(
        e -> {
          if (e.getClickCount() == 2) {
            insertVarId();
          }
        });

    varTableView.setOnKeyPressed(
        e -> {
          if (KeyCode.J == e.getCode() && !e.isControlDown() && !e.isShiftDown()) {
            varTableView.getSelectionModel().selectNext();
          } else if (KeyCode.K == e.getCode() && !e.isControlDown() && !e.isShiftDown()) {
            varTableView.getSelectionModel().selectPrevious();
          } else if (KeyCode.ENTER == e.getCode()) {
            insertVarId();
          }
        });
  } // }}}

  void focus() { // {{{
    varTableView.requestFocus();
    if (varTableView.getSelectionModel().isEmpty()) {
      varTableView.getSelectionModel().selectFirst();
    }
  } // }}}

  private boolean existsMatchedText(VarDB varDb, String newVal) { // {{{
    if (newVal == null || newVal.isEmpty()) {
      return true;
    }

    String lowerCaseFilter = newVal.toLowerCase();
    int vi = varDb.idProperty().get();
    String varId = String.valueOf(vi);
    String varName = varDb.nameProperty().get();

    if (varId.contains(lowerCaseFilter)) {
      return true;
    } else if (varName.toLowerCase().contains(lowerCaseFilter)) {
      return true;
    }
    return false;
  } // }}}

  private void insertVarId() { // {{{
    SelectionModel<VarDB> model = varTableView.getSelectionModel();
    if (!model.isEmpty()) {
      VarDB selectedItem = model.getSelectedItem();
      int varId = selectedItem.idProperty().get();
      mainController.insertVarId(varId);
    }
  } // }}}

  // setter

  public void setVariables(String path) { // {{{
    try {
      masterVarDBs.clear();
      File file = new File(path);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(file);
      JsonNode child = root.get(KEY_VAR);
      int size = child.size();

      IntStream.range(1, size)
          .forEach(
              i -> {
                String name = child.get(i).asText();
                masterVarDBs.add(new VarDB(i, name));
              });
    } catch (IOException e) {
      util.MyLogger.log(e);
    }
  } // }}}
}

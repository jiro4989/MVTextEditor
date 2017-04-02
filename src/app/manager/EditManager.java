package app.manager;

import static util.Texts.*;
import static java.util.stream.IntStream.range;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.MainController;

import java.io.*;
import java.util.*;
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
        varTableView.getItems().add(new VarDB(i, name));
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }//}}}

}

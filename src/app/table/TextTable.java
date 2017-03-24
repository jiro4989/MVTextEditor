package app.table;

import java.io.IOException;
import java.util.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;

public class TextTable extends AnchorPane {

  @FXML private TableView<TextDB> tableView;

  @FXML private TableColumn<TextDB, String> iconColumn;
  @FXML private TableColumn<TextDB, String> actorNameColumn;
  @FXML private TableColumn<TextDB, String> textColumn;
  @FXML private TableColumn<TextDB, Boolean> backgroundColumn;
  @FXML private TableColumn<TextDB, Boolean> positionColumn;

  public TextTable() {//{{{
    FXMLLoader loader = new FXMLLoader(getClass().getResource("text_table.fxml"));
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();

      iconColumn.setCellValueFactory(new PropertyValueFactory<TextDB, String>("icon"));
      actorNameColumn.setCellValueFactory(new PropertyValueFactory<TextDB, String>("actorName"));
      textColumn.setCellValueFactory(new PropertyValueFactory<TextDB, String>("text"));
      backgroundColumn.setCellValueFactory(new PropertyValueFactory<TextDB, Boolean>("background"));
      positionColumn.setCellValueFactory(new PropertyValueFactory<TextDB, Boolean>("position"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }//}}}

  public void setTextList(List<List<String>> listList) {//{{{
    listList.stream().forEach(list -> {
      tableView.getItems().add(new TextDB("actor01:0", list, false, false));
    });
  }//}}}

}

package app.table;

import app.MainController;

import java.io.IOException;
import java.util.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;

public class TextTable extends AnchorPane {

  private Optional<MainController> mainControllerOpt = Optional.empty();

  @FXML private TableView<TextDB> tableView;
  @FXML private TableColumn<TextDB, String> iconColumn;
  @FXML private TableColumn<TextDB, String> actorNameColumn;
  @FXML private TableColumn<TextDB, String> textColumn;
  @FXML private TableColumn<TextDB, String> backgroundColumn;
  @FXML private TableColumn<TextDB, String> positionColumn;

  public TextTable() {//{{{
    FXMLLoader loader = new FXMLLoader(getClass().getResource("text_table.fxml"));
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();

      iconColumn       . setCellValueFactory(new PropertyValueFactory<TextDB, String>("icon"));
      actorNameColumn  . setCellValueFactory(new PropertyValueFactory<TextDB, String>("actorName"));
      textColumn       . setCellValueFactory(new PropertyValueFactory<TextDB, String>("text"));
      backgroundColumn . setCellValueFactory(new PropertyValueFactory<TextDB, String>("background"));
      positionColumn   . setCellValueFactory(new PropertyValueFactory<TextDB, String>("position"));

      iconColumn.setCellFactory(col -> new ImageTableCell());

      tableView.getFocusModel().focusedCellProperty().addListener((obs, oldVal, newVal) -> {
        mainControllerOpt.ifPresent(mc -> {
          SelectionModel<TextDB> model = tableView.getSelectionModel();
          if (!model.isEmpty()) {
            TextDB db = model.getSelectedItem();
            mc.updateTextViewer(db);
          }
        });
      });
      tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      tableView.getSelectionModel().setCellSelectionEnabled(true);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }//}}}

  public void setTextList(List<List<String>> listList) {//{{{
    listList.stream().forEach(list -> {
      tableView.getItems().add(new TextDB("C:/RPG/Project1/img/faces/Actor1.png:0", list, "ウィンドウ", "下"));
    });
  }//}}}

  public void setMainController(MainController mc) {//{{{
    mainControllerOpt = Optional.ofNullable(mc);
  }//}}}

}

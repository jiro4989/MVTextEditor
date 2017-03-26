package app.table;

import static util.Texts.*;

import util.ResourceBundleWithUtf8;

import app.MainController;
import app.selector.ImageSelector;

import java.io.IOException;
import java.util.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
          getSelectedItem().ifPresent(item -> {
            mc.updateTextViewer(item);
          });
        });
      });

      tableView.setOnMouseClicked(e -> {
        if (e.getClickCount() == 2) {
          getSelectedItem().ifPresent(item -> {
            String icon = item.iconProperty().get();
            String path = createFilePath(icon.split(":"));

            ImageSelector selector = new ImageSelector(path);
            selector.showAndWait();
          });
        }
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

  private Optional<TextDB> getSelectedItem() {//{{{
    SelectionModel<TextDB> model = tableView.getSelectionModel();
    if (!model.isEmpty()) {
      return Optional.ofNullable(model.getSelectedItem());
    }
    return Optional.empty();
  }//}}}

}

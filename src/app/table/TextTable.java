package app.table;

import static util.Texts.*;

import app.selector.ImageSelector;

import app.MainController;

import java.util.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

public class TextTable {

  private Optional<MainController> mainControllerOpt = Optional.empty();

  private final TableView<TextDB>           tableView;
  private final TableColumn<TextDB, String> iconColumn;
  private final TableColumn<TextDB, String> actorNameColumn;
  private final TableColumn<TextDB, String> textColumn;
  private final TableColumn<TextDB, String> backgroundColumn;
  private final TableColumn<TextDB, String> positionColumn;

  public TextTable(
      TableView<TextDB> tv
      , TableColumn<TextDB, String> ic
      , TableColumn<TextDB, String> anc
      , TableColumn<TextDB, String> tc
      , TableColumn<TextDB, String> bgc
      , TableColumn<TextDB, String> pc
      )
  {//{{{

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
          int index = selector.getSelectedIndex();
          System.out.println(index);
        });
      }
    });

    tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    tableView.getSelectionModel().setCellSelectionEnabled(true);

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

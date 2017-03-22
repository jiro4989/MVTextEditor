package app.table;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TextTable extends AnchorPane {

  public TextTable() {//{{{
    FXMLLoader loader = new FXMLLoader(getClass().getResource("text_table.fxml"));
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }//}}}

}

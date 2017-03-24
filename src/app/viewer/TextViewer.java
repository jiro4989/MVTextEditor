package app.viewer;

import app.table.TextDB;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TextViewer extends VBox {

  @FXML private TextArea textArea;
  @FXML private ComboBox<String> windowComboBox;
  @FXML private ComboBox<String> positionComboBox;

  public TextViewer() {//{{{
    FXMLLoader loader = new FXMLLoader(getClass().getResource("text_viewer.fxml"));
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }//}}}

  public void update(TextDB db) {//{{{
    textArea.setText(db.textProperty().get());
  }//}}}

}

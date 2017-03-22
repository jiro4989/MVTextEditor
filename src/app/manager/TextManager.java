package app.manager;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TextManager extends AnchorPane {

  public TextManager() {//{{{
    FXMLLoader loader = new FXMLLoader(getClass().getResource("text_manager.fxml"));
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }//}}}

}

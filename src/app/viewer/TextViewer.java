package app.viewer;

import static util.Texts.*;

import app.table.TextDB;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class TextViewer extends VBox {
  @FXML private ImageView imageView;
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
    String path = db.iconProperty().get();
    String[] strs = path.split(":");
    path = createFilePath(strs);

    int index = Integer.parseInt(strs[strs.length - 1]);
    Image img = new Image("file:" + path);
    int x = index % 4 * WIDTH;
    int y = index / 4 * HEIGHT;

    PixelReader reader = img.getPixelReader();
    Image newImage = new WritableImage(reader, x, y, WIDTH, HEIGHT);
    imageView.setImage(newImage);
  }//}}}

}

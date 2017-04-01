package app;

import static util.Texts.*;

import app.MainController;
import app.table.TextDB;

import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

class TextView {

  private final MainController mainController;
  private final ImageView faceImageView;
  private final GridPane colorPickerGridPane;
  private final ImageView colorPickerImageView;
  private final TextArea editorTextArea;
  private final ComboBox<String> backgroundComboBox;
  private final ComboBox<String> positionComboBox;

  TextView(
      MainController mainController
      , ImageView faceImageView
      , GridPane colorPickerGridPane
      , ImageView colorPickerImageView
      , TextArea editorTextArea
      , ComboBox<String> backgroundComboBox
      , ComboBox<String> positionComboBox
      )
  {//{{{
    this.mainController       = mainController;
    this.faceImageView        = faceImageView;
    this.colorPickerGridPane  = colorPickerGridPane;
    this.colorPickerImageView = colorPickerImageView;
    this.editorTextArea       = editorTextArea;
    this.backgroundComboBox   = backgroundComboBox;
    this.positionComboBox     = positionComboBox;
  }//}}}

  void update(TextDB db) {//{{{
    String icon = db.iconProperty().get();
    String text = db.textProperty().get();
    String bg   = db.backgroundProperty().get();
    String pos  = db.positionProperty().get();

    String[] array      = icon.split(":");
    String path         = createFilePath(array);
    Image originalImage = new Image("file:" + path);
    int index           = Integer.parseInt(array[array.length - 1]);
    Image newImage      = createTrimmedImage(originalImage, index);

    faceImageView.setImage(newImage);
    editorTextArea.setText(text);
    backgroundComboBox.setValue(bg);
    positionComboBox.setValue(pos);
  }//}}}

  /**
   * 元になる画像から番号でトリミングした位置の画像を返す。
   */
  private Image createTrimmedImage(Image originalImage, int index) {//{{{
    PixelReader reader = originalImage.getPixelReader();
    int x = index % COLUMN * WIDTH;
    int y = index / COLUMN * HEIGHT;
    Image newImage = new WritableImage(reader, x, y, WIDTH, HEIGHT);
    return newImage;
  }//}}}

}

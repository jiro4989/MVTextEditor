package app;

import static util.Texts.*;
import static java.util.stream.IntStream.range;

import app.MainController;
import app.table.TextDB;

import java.nio.file.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

class TextView {

  private final MainController mainController;
  private final ImageView faceImageView;
  private final TextField actorNameTextField;
  private final GridPane colorPickerGridPane;
  private final ImageView colorPickerImageView;
  private final TextArea editorTextArea;
  private final ComboBox<String> backgroundComboBox;
  private final ComboBox<String> positionComboBox;

  TextView(
      MainController mainController
      , ImageView faceImageView
      , TextField actorNameTextField
      , GridPane colorPickerGridPane
      , ImageView colorPickerImageView
      , TextArea editorTextArea
      , ComboBox<String> backgroundComboBox
      , ComboBox<String> positionComboBox
      )
  {//{{{
    this.mainController       = mainController;
    this.faceImageView        = faceImageView;
    this.actorNameTextField   = actorNameTextField;
    this.colorPickerGridPane  = colorPickerGridPane;
    this.colorPickerImageView = colorPickerImageView;
    this.editorTextArea       = editorTextArea;
    this.backgroundComboBox   = backgroundComboBox;
    this.positionComboBox     = positionComboBox;

    // カラーピッカーをダブルクリックして選択範囲を色文字列でくくる
    colorPickerImageView.setOnMouseClicked(e -> {
      final String FORM = "\\c[%d]";
      if (e.getClickCount() == 2) {
        IndexRange range = editorTextArea.getSelection();
        int start = range.getStart();
        int end   = range.getEnd();

        int colorIndex = calcColorIndex(e);
        if (start != end) {
          editorTextArea.insertText(end, DEFAULT_COLOR);
          editorTextArea.insertText(start, String.format(FORM, colorIndex));
          return;
        }
        editorTextArea.insertText(start, String.format(FORM, colorIndex));
      }
    });
  }//}}}

  private int calcColorIndex(MouseEvent e) {//{{{
    int x  = (int) e.getX();
    int y  = (int) e.getY();
    int xx = x / COLOR_TILE_SIZE;
    int yy = y / COLOR_TILE_SIZE;
    return xx + yy * COLOR_PICKER_COLUMN_SIZE;
  }//}}}

  void update(TextDB db) {//{{{
    String icon      = db.iconProperty().get();
    String actorName = db.actorNameProperty().get();
    String text      = db.textProperty().get();
    String bg        = db.backgroundProperty().get();
    String pos       = db.positionProperty().get();

    actorNameTextField.setText(actorName);
    editorTextArea.setText(text);
    backgroundComboBox.setValue(bg);
    positionComboBox.setValue(pos);

    if (icon != null && icon.length() != 0) {
      String[] array      = icon.split(":");
      String path         = createFilePath(array);

      if (Files.exists(Paths.get(path))) {
        Image originalImage = new Image("file:" + path);
        int index           = Integer.parseInt(array[array.length - 1]);
        Image newImage      = createTrimmedImage(originalImage, index);
        faceImageView.setImage(newImage);
        return;
      }
    }
  }//}}}

  void setColorPickerImage(String path) {//{{{
    Image src = new Image("file:" + path);
    PixelReader r = src.getPixelReader();
    int std = (int) src.getWidth();
    int x = std / 2;
    int y = std - std / 4;
    int w = std / 2;
    int h = std / 4;
    Image trimmedImg = new WritableImage(r, x, y, w, h);

    int w2 = w * 2;
    int h2 = h / 2;
    WritableImage wImage = new WritableImage(w2, h2);
    PixelWriter writer = wImage.getPixelWriter();

    final int H = std / 16;
    range(0, 4).forEach(i -> {
      int y2 = i * H;
      int[] pixels = getTrimmedPixels(trimmedImg, 0, y2, w, H);
      int tmpX = i % 2 * w;
      int tmpY = i / 2 * H;
      writer.setPixels(tmpX, tmpY, w, H, FORMAT, pixels, 0, w);
    });

    colorPickerImageView.setImage(wImage);
  }//}}}

  private int[] getTrimmedPixels(Image src, int x, int y , int w, int h) {//{{{
    PixelReader r = src.getPixelReader();
    Image newImg = new WritableImage(r, x, y, w, h);

    int[] pixels = new int[w * h];
    PixelReader rr = newImg.getPixelReader();
    rr.getPixels(0, 0, w, h , FORMAT, pixels, 0, w);
    return pixels;
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

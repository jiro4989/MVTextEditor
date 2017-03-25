package app.table;

import static util.Texts.*;

import java.util.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

class ImageTableCell extends TableCell<TextDB, String> {
  private final VBox vBox;
  private final ImageView imageView;
  //private final Label label;
  private static Map<String, Image[]> imageMap;

  ImageTableCell() {//{{{
    super();
    imageView = new ImageView();
    //label     = new Label();
    vBox      = new VBox();
    imageMap  = new HashMap<>();

    double s = 60;
    imageView.setFitWidth(s);
    imageView.setFitHeight(s);
    vBox.setSpacing(10);
    vBox.getChildren().add(imageView);
    //vBox.getChildren().add(label);
  }//}}}

  @Override
  protected void updateItem(String item, boolean empty) {//{{{
    super.updateItem(item, empty);
    if (item != null) {
      Image[] imgs = imageMap.get(item);
      if (imgs == null) {
        imgs = createTrimmedImages(item);
        imageMap.put(item, imgs);
      }

      int index = 0;
      Image img = imgs[index];

      imageView.setImage(img);
      //label.setText(item);
      setGraphic(vBox);
      return;
    }
    setGraphic(null);
  }//}}}

  private Image[] createTrimmedImages(String path) {//{{{
    Image img = new Image("file:" + path);
    int imgHeight = (int) img.getHeight();
    int rowMax = imgHeight / HEIGHT;
    int max = rowMax * 4;

    Image[] imgs = new Image[max];
    for (int i=0; i<max; i++) {
      int x = i % 4 * WIDTH;
      int y = i / 4 * HEIGHT;
      PixelReader reader = img.getPixelReader();
      imgs[i] = new WritableImage(reader, x, y, WIDTH, HEIGHT);
    }

    return imgs;
  }//}}}

  void clear() {//{{{
    imageMap.clear();
  }//}}}

}

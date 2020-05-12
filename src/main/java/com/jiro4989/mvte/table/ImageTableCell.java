package com.jiro4989.mvte.table;

import static com.jiro4989.mvte.util.Texts.*;

import java.util.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

class ImageTableCell extends TableCell<TextDB, String> {
  private final VBox vBox;
  private final ImageView imageView;
  private static Map<String, Image[]> imageMap;

  ImageTableCell() { // {{{
    super();
    imageView = new ImageView();
    vBox = new VBox();
    imageMap = new HashMap<>();

    double s = 60;
    imageView.setFitWidth(s);
    imageView.setFitHeight(s);
    vBox.setSpacing(10);
    vBox.getChildren().add(imageView);
  } // }}}

  @Override
  protected void updateItem(String item, boolean empty) { // {{{
    super.updateItem(item, empty);
    if (item != null && item.length() != 0) {
      Image[] imgs = imageMap.get(item);
      if (imgs == null) {
        String[] strs = item.split(":");
        String path = createFilePath(strs);
        imgs = createTrimmedImages(path);
        imageMap.put(item, imgs);
      }

      String[] strs = item.split(":");
      int index = Integer.parseInt(strs[strs.length - 1]);
      Image img = imgs[index];

      imageView.setImage(img);
      setGraphic(vBox);
      return;
    }
    setGraphic(null);
  } // }}}

  private Image[] createTrimmedImages(String path) { // {{{
    Image img = new Image("file:" + path);
    int imgHeight = (int) img.getHeight();
    int rowMax = imgHeight / HEIGHT;
    int max = rowMax * 4;

    Image[] imgs = new Image[max];
    for (int i = 0; i < max; i++) {
      int x = i % 4 * WIDTH;
      int y = i / 4 * HEIGHT;
      PixelReader reader = img.getPixelReader();
      imgs[i] = new WritableImage(reader, x, y, WIDTH, HEIGHT);
    }

    return imgs;
  } // }}}

  void clear() { // {{{
    imageMap.clear();
  } // }}}
}

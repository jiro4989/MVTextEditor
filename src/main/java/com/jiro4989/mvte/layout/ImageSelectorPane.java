package jiro.javafx.scene.layout;

import java.io.IOException;
import javafx.fxml.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

public class ImageSelectorPane extends GridPane {

  private int baseX = 0;
  private int baseY = 0;
  private int width = 0;
  private int height = 0;

  private DoubleClickInterface dci = null;

  @FXML private ImageView imageView;
  @FXML private GridPane focusGridPane;
  @FXML private GridPane selectedGridPane;

  // constructor

  public ImageSelectorPane() { // {{{
    FXMLLoader loader = new FXMLLoader(getClass().getResource("image_selector.fxml"));
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  } // }}}

  // fxml event

  @FXML
  private void imageViewOnMouseMoved(MouseEvent e) { // {{{
    int x = (int) e.getX();
    int y = (int) e.getY();
    int gridX = x / width * width;
    int gridY = y / height * height;

    int imgWidth = (int) imageView.getFitWidth();
    int imgHeight = (int) imageView.getFitHeight();

    if ((gridX + width) <= imgWidth && (gridY + height) <= imgHeight) {
      focusGridPane.setLayoutX(gridX);
      focusGridPane.setLayoutY(gridY);
    }
  } // }}}

  @FXML
  private void focusGridPaneOnMouseClicked(MouseEvent e) { // {{{
    int x = (int) focusGridPane.getLayoutX();
    int y = (int) focusGridPane.getLayoutY();

    selectedGridPane.setLayoutX(x);
    selectedGridPane.setLayoutY(y);
  } // }}}

  @FXML
  private void selectedGridPaneOnMouseMoved(MouseEvent e) { // {{{
    int x = (int) (e.getX() + selectedGridPane.getLayoutX());
    int y = (int) (e.getY() + selectedGridPane.getLayoutY());
    int gridX = x / width * width;
    int gridY = y / height * height;

    int imgWidth = (int) imageView.getFitWidth();
    int imgHeight = (int) imageView.getFitHeight();

    if ((gridX + width) <= imgWidth && (gridY + height) <= imgHeight) {
      focusGridPane.setLayoutX(gridX);
      focusGridPane.setLayoutY(gridY);
    }
  } // }}}

  @FXML
  private void selectedGridPaneOnMouseClicked(MouseEvent e) { // {{{
    if (e.getClickCount() == 2) dci.apply();
  } // }}}

  // Getter

  public int getSelectedIndex() { // {{{
    baseX = (int) selectedGridPane.getLayoutX();
    baseY = (int) selectedGridPane.getLayoutY();
    int col = baseX / width;
    int row = baseY / height;
    return col + row * 4;
  } // }}}

  // Setter

  public void setImage(String path) {
    setImage(new Image("file:" + path));
  }

  public void setImage(Image img) { // {{{
    int width = (int) img.getWidth();
    int height = (int) img.getHeight();

    imageView.setImage(img);
    imageView.setFitWidth(width);
    imageView.setFitHeight(height);
    setPrefWidth(width);
    setPrefHeight(height);
  } // }}}

  public void setWidth(int width) {
    this.width = width;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public void setDoubleClickAction(DoubleClickInterface dci) {
    this.dci = dci;
  }
}

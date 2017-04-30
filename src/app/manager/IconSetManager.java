package app.manager;

import static util.Texts.*;

import app.MainController;

import java.util.Optional;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

class IconSetManager {

  private final MainController mainController;

  // component
  private final GridPane  iconGridPane;
  private final ImageView iconImageView;
  private final Label     iconFocusLabel;
  private final Label     iconSelectedLabel;

  // IconSet image
  private Optional<Image>   iconImageOpt  = Optional.empty();
  private Optional<Integer> iconWidthOpt  = Optional.empty();
  private Optional<Integer> iconHeightOpt = Optional.empty();

  IconSetManager(MainController mc, GridPane igp, ImageView iiv, Label ifl, Label isl) {//{{{
    mainController    = mc;
    iconGridPane      = igp;
    iconImageView     = iiv;
    iconFocusLabel    = ifl;;
    iconSelectedLabel = isl;

    iconImageView.setOnMouseMoved       ( e -> { iconImageViewOnMouseMoved       ( e ) ; } ) ;
    iconFocusLabel.setOnMouseClicked    ( e -> { iconFocusLabelOnMouseClicked    ( e ) ; } ) ;
    iconSelectedLabel.setOnMouseClicked ( e -> { iconSelectedLabelOnMouseClicked ( e ) ; } ) ;

  }//}}}

  void focus() { iconGridPane.requestFocus(); }

  // private methods

  private void iconImageViewOnMouseMoved(MouseEvent e) {//{{{
    iconWidthOpt.ifPresent(iconWidth -> {
      iconHeightOpt.ifPresent(iconHeight -> {
        int x = (int) e.getX();
        int y = (int) e.getY();
        int gridX = x / ICONSET_SIZE * ICONSET_SIZE;
        int gridY = y / ICONSET_SIZE * ICONSET_SIZE;

        if (   gridX  <= iconWidth
            && gridY  <= iconHeight
           )
        {
          iconFocusLabel.setLayoutX(gridX);
          iconFocusLabel.setLayoutY(gridY);
        }
      });
    });
  }//}}}

  private void iconFocusLabelOnMouseClicked(MouseEvent e) {//{{{
    int x = (int) iconFocusLabel.getLayoutX();
    int y = (int) iconFocusLabel.getLayoutY();
    iconSelectedLabel.setLayoutX(x);
    iconSelectedLabel.setLayoutY(y);

    if (e.getClickCount() == 2) {
      int column = x / ICONSET_SIZE;
      int row    = y / ICONSET_SIZE;
      int index  = column + row * ICONSET_COLUMN;
      mainController.insertIconSetId(index);
    }
  }//}}}

  private void iconSelectedLabelOnMouseClicked(MouseEvent e) {//{{{
    iconWidthOpt.ifPresent(width -> {
      iconHeightOpt.ifPresent(height -> {
        int x = (int) (e.getX() + iconSelectedLabel.getLayoutX());
        int y = (int) (e.getY() + iconSelectedLabel.getLayoutY());
        int gridX = x / width * width;
        int gridY = y / height * height;

        int imgWidth  = (int) iconImageView.getFitWidth();
        int imgHeight = (int) iconImageView.getFitHeight();

        if (   (gridX + width)  <= imgWidth
            && (gridY + height) <= imgHeight
           )
        {
          iconFocusLabel.setLayoutX(gridX);
          iconFocusLabel.setLayoutY(gridY);
        }
      });
    });
  }//}}}

  // setter

  void setImage(String path) {//{{{
    Image src  = new Image("file:" + path);
    int width  = (int) src.getWidth();
    int height = (int) src.getHeight();

    iconGridPane.setPrefWidth(width);
    iconGridPane.setPrefHeight(height);
    iconImageView.setImage(src);
    iconImageView.setFitWidth(width);
    iconImageView.setFitHeight(height);

    iconImageOpt  = Optional.ofNullable(new Image("file:" + path));
    iconWidthOpt  = Optional.ofNullable(width);
    iconHeightOpt = Optional.ofNullable(height);
  }//}}}

}

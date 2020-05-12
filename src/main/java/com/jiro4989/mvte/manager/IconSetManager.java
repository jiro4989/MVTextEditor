package com.jiro4989.mvte.manager;

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

  private int colCount = 0;
  private int rowCount = 0;

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

    iconGridPane.setOnKeyPressed(e -> {
      if (KeyCode.H == e.getCode()
          && !e.isControlDown()
          && !e.isShiftDown()
         )
      {
        moveFocusGridPane(0, -1);
      } else if (KeyCode.J == e.getCode()
          && !e.isControlDown()
          && !e.isShiftDown()
          )
      {
        moveFocusGridPane(1, 0);
      } else if (KeyCode.K == e.getCode()
          && !e.isControlDown()
          && !e.isShiftDown()
          )
      {
        moveFocusGridPane(-1, 0);
      } else if (KeyCode.L == e.getCode()
          && !e.isControlDown()
          && !e.isShiftDown()
          )
      {
        moveFocusGridPane(0, 1);
      } else if (KeyCode.ENTER == e.getCode()) {
        int x = (int) iconFocusLabel.getLayoutX();
        int y = (int) iconFocusLabel.getLayoutY();
        int column = x / ICONSET_SIZE;
        int row    = y / ICONSET_SIZE;
        int index  = column + row * ICONSET_COLUMN;
        mainController.insertIconSetId(index);
      }
    });

  }//}}}

  void focus() { iconGridPane.requestFocus(); }

  // private methods

  private void moveFocusGridPane(int prow, int pcol) {//{{{
    int x = (int) iconFocusLabel.getLayoutX();
    int y = (int) iconFocusLabel.getLayoutY();
    x += pcol * ICONSET_SIZE;
    y += prow * ICONSET_SIZE;
    x = Math.max(0, x);
    y = Math.max(0, y);

    int w = (int) iconImageView.getFitWidth();
    int h = (int) iconImageView.getFitHeight();
    x = Math.min((colCount - 1) * ICONSET_SIZE, x);
    y = Math.min((rowCount - 1) * ICONSET_SIZE, y);

    iconSelectedLabel.setLayoutX(x);
    iconSelectedLabel.setLayoutY(y);
    iconFocusLabel.setLayoutX(x);
    iconFocusLabel.setLayoutY(y);
  }//}}}

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
    iconImageView.setImage(null);
    iconSelectedLabel.setLayoutX(0);
    iconSelectedLabel.setLayoutY(0);
    iconFocusLabel.setLayoutX(0);
    iconFocusLabel.setLayoutY(0);

    Image src  = new Image("file:" + path);
    int width  = (int) src.getWidth();
    int height = (int) src.getHeight();

    colCount = width / ICONSET_SIZE;
    rowCount = height / ICONSET_SIZE;

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

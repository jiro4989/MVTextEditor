package app.selector;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class ImageSelectorController {

  // fxml component//{{{

  @FXML private SplitPane splitPane;

  @FXML private TitledPane listViewTitledPane;
  @FXML private ListView<String> listView;

  @FXML private TitledPane imageViewTitledPane;
  @FXML private GridPane parentGridPane;
  @FXML private ImageView imageView;
  @FXML private GridPane focusGridPane;

  //}}}

  @FXML private void initialize() {

  }

}


package app.config;

import jiro.javafx.scene.control.JavaFXCustomizeUtils;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ImportConfigController {

  @FXML private CheckBox crCheckBox;
  @FXML private TextField fontCountTextField;
  @FXML private CheckBox wrappingCheckBox;
  @FXML private ComboBox<String> bracketsComboBox;
  @FXML private CheckBox indentCheckBox;

  @FXML private Button okButton;
  @FXML private Button cancelButton;

  @FXML
  private void initialize() {
    JavaFXCustomizeUtils.setIntegerOnlyOption(fontCountTextField);
  }

  @FXML private void okButtonOnAction() {
    //propWrite();
    okButton.getScene().getWindow().hide();
  }

  @FXML private void cancelButtonOnAction() {
    okButton.getScene().getWindow().hide();
  }

}

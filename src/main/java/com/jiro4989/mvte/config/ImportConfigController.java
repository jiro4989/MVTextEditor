package com.jiro4989.mvte.config;

import jiro.javafx.scene.control.JavaFXCustomizeUtils;

import jiro.java.util.MyProperties;

import com.jiro4989.mvte.MainController;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ImportConfigController {

  @FXML private CheckBox crCheckBox;

  @FXML private Label returnFontLabel;
  @FXML private TextField fontCountTextField;

  @FXML private CheckBox wrappingCheckBox;

  @FXML private Label bracketsLabel;
  @FXML private ComboBox<String> bracketsComboBox;

  @FXML private CheckBox indentCheckBox;

  @FXML private Button okButton;
  @FXML private Button cancelButton;

  @FXML
  private void initialize() {//{{{
    JavaFXCustomizeUtils.setIntegerOnlyOption(fontCountTextField);
    MyProperties mp = MainController.formatProperties;
    mp.getProperty("textReturn").map(Boolean::valueOf).ifPresent(this::setCrCheckBox);
    mp.getProperty("wrapping")  .map(Boolean::valueOf).ifPresent(this::setWrappingCheckBox);
    mp.getProperty("textIndent").map(Boolean::valueOf).ifPresent(indentCheckBox::setSelected);
    mp.getProperty("textReturnSize").map(Integer::parseInt).ifPresent(f -> fontCountTextField.setText("" + f));
    mp.getProperty("bracketStart").map(Integer::parseInt).ifPresent(i -> bracketsComboBox.getSelectionModel().select(i));
  }//}}}

  @FXML private void crCheckBoxOnAction()       { setCrCheckBox(crCheckBox.isSelected()); }
  @FXML private void wrappingCheckBoxOnAction() { setWrappingCheckBox(wrappingCheckBox.isSelected()); }

  private void setCrCheckBox(boolean b) {//{{{
    crCheckBox.setSelected(b);
    returnFontLabel.setDisable(!b);
    fontCountTextField.setDisable(!b);

    boolean disable = !(crCheckBox.isSelected() && wrappingCheckBox.isSelected());
    indentCheckBox.setDisable(disable);
  }//}}}

  private void setWrappingCheckBox(boolean b) {//{{{
    wrappingCheckBox.setSelected(b);
    bracketsLabel.setDisable(!b);
    bracketsComboBox.setDisable(!b);

    boolean disable = !(crCheckBox.isSelected() && wrappingCheckBox.isSelected());
    indentCheckBox.setDisable(disable);
  }//}}}

  @FXML private void okButtonOnAction() {
    propWrite();
    okButton.getScene().getWindow().hide();
  }

  @FXML private void cancelButtonOnAction() {
    okButton.getScene().getWindow().hide();
  }

  private void propWrite() {//{{{
    MyProperties mp = MainController.formatProperties;
    mp . setProperty("textReturn" , "" + crCheckBox       . isSelected());
    mp . setProperty("wrapping"   , "" + wrappingCheckBox . isSelected());
    mp . setProperty("textIndent" , "" + indentCheckBox   . isSelected());
    mp . setProperty("textReturnSize" , "" + fontCountTextField.getText());

    int selectedIndex = bracketsComboBox.getSelectionModel().getSelectedIndex();
    mp . setProperty("bracketStart" , "" + selectedIndex);

    mp.store();
  }//}}}

}

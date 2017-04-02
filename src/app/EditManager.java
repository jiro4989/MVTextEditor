package app;

import app.MainController;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

class EditManager {

  private final MainController mainController;

  private final TableView<VarDB>            varTable;
  private final TableColumn<VarDB, Integer> varIdColumn;
  private final TableColumn<VarDB, String> varNameColumn;

  EditManager(
        MainController mainController
      , TableView<VarDB> varTable
      , TableColumn<VarDB, Integer> varIdColumn
      , TableColumn<VarDB, String> varNameColumn
      )
  {//{{{
    this.mainController = mainController;
    this.varTable       = varTable;
    this.varIdColumn    = varIdColumn;
    this.varNameColumn  = varNameColumn;
  }//}}}

}

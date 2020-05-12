package util;

import static util.Texts.*;

import java.io.*;
import java.nio.file.*;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;

public final class InitUtils {
  private InitUtils() {}

  /**
   * 初期設定フォルダを生成する。<br>
   * 生成した場合はtrueを、すでに存在していた場合はfalseを返す。
   *
   * @return 生成した or not
   */
  public static boolean mkPropDirs() { // {{{
    try {
      Path path = Paths.get(".", "properties", JAR_NAME);
      if (Files.notExists(path)) {
        Files.createDirectories(path);
        return true;
      }

      Path errdir = Paths.get(".", "errors", JAR_NAME);
      if (Files.notExists(errdir)) {
        Files.createDirectories(errdir);
        return true;
      }

      return false;
    } catch (IOException e) {
      showExceptionDialog(e);
      Platform.exit();
    }
    return true;
  } // }}}

  public static final void showExceptionDialog(Exception ex) { // {{{
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Exception Dialog");
    alert.setHeaderText("Look, an Exception Dialog");
    alert.setContentText("Could not find file blabla.txt!");

    // Create expandable Exception.
    // StringWriter sw = new StringWriter();
    // PrintWriter pw = new PrintWriter(sw);
    // ex.printStackTrace(pw);
    // String exceptionText = sw.toString();
    String exceptionText = ex.getLocalizedMessage();

    Label label = new Label("The exception stacktrace was:");

    TextArea textArea = new TextArea(exceptionText);
    textArea.setEditable(false);
    textArea.setWrapText(true);

    textArea.setMaxWidth(Double.MAX_VALUE);
    textArea.setMaxHeight(Double.MAX_VALUE);
    GridPane.setVgrow(textArea, Priority.ALWAYS);
    GridPane.setHgrow(textArea, Priority.ALWAYS);

    GridPane expContent = new GridPane();
    expContent.setMaxWidth(Double.MAX_VALUE);
    expContent.add(label, 0, 0);
    expContent.add(textArea, 0, 1);

    // Set expandable Exception into the dialog pane.
    alert.getDialogPane().setExpandableContent(expContent);

    alert.showAndWait();
    Platform.exit();
  } // }}}
}

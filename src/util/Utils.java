package util;

import java.util.Locale;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public final class Utils {
  private Utils() {}

  // TODO
  public static void showSelectProjectDirDialog() {
    Alert alert = new Alert(AlertType.CONFIRMATION);

    Locale locale = Locale.getDefault();
    String header = locale.equals(Locale.JAPAN)
      ? "【初回設定】ツクールMVのプロジェクトフォルダを指定してください。"
      : "Select Project Folder of RPG Maker MV";
    alert.setHeaderText(header);
    alert.setContentText("プロジェクトフォルダ ＝ Game.rpgproject の存在するフォルダ");

    alert.showAndWait();
  }
}

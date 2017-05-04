package util;

import java.util.Locale;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public final class Utils {
  private Utils() {}

  public static void showSelectProjectDirDialog() {
    Alert alert = new Alert(AlertType.CONFIRMATION);

    Locale locale = Locale.getDefault();
    String header = locale.equals(Locale.JAPAN)
      ? "【初回設定】ツクールMVのプロジェクトフォルダを指定してください。"
      : "[Initial Setting] Select Project Folder of RPG Maker MV";
    alert.setHeaderText(header);

    String content = locale.equals(Locale.JAPAN)
      ? "プロジェクトフォルダ ＝ Game.rpgproject の存在するフォルダ"
      : "Project Folder = Folder that \"Game.rpgproject\" exists.";
    alert.setContentText(content);

    alert.showAndWait();
  }

}

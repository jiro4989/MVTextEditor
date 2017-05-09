package util;

import java.util.Locale;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public final class Utils {
  private Utils() {}

  public static void showSelectProjectDirDialog() {//{{{
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
  }//}}}

  public static void showErrorDialog() {//{{{
    Alert alert = new Alert(AlertType.ERROR);

    Locale locale = Locale.getDefault();
    String header = locale.equals(Locale.JAPAN)
      ? "フォルダはプロジェクトフォルダではありません。"
      : "Folder is not Project Folder.";
    alert.setHeaderText(header);

    alert.showAndWait();
  }//}}}

  public static void showSuccessDialog() {//{{{
    Alert alert = new Alert(AlertType.INFORMATION);
    Locale locale = Locale.getDefault();

    String header = locale.equals(Locale.JAPAN)
      ? "正常に出力できました。"
      : "Success Export MapXXX.json";
    alert.setHeaderText(header);

    if (locale.equals(Locale.JAPAN))
      alert.setContentText("お疲れ様です。");
    alert.showAndWait();
  }//}}}

  public static final void showParsingErrorDialog() {//{{{
    Alert alert = new Alert(AlertType.ERROR);

    Locale locale = Locale.getDefault();
    String header = locale.equals(Locale.JAPAN)
      ? "一時データ保存時のXML変換に失敗しました。"
      : "Failed saving XML data.";
    alert.setHeaderText(header);

    alert.showAndWait();
  }//}}}

}

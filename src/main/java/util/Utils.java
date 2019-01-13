package util;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;

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

  public static void showFailedToLoadProjectDialog() {//{{{
    Alert alert = new Alert(AlertType.CONFIRMATION);

    Locale locale = Locale.getDefault();
    String header = locale.equals(Locale.JAPAN)
      ? "ツクールMVのプロジェクトフォルダではありません。"
      : "Folder is not Project Folder of RPG Maker MV.";
    alert.setHeaderText(header);

    String content = locale.equals(Locale.JAPAN)
      ? "必要データの読み込みに失敗しました。"
      : "Failed to load datas.";
    alert.setContentText(content);

    alert.showAndWait();
  }//}}}

  public static void showResetProjectDirDialog() {//{{{
    Alert alert = new Alert(AlertType.CONFIRMATION);

    Locale locale = Locale.getDefault();
    String header = locale.equals(Locale.JAPAN)
      ? "ツクールMVのプロジェクトフォルダが存在しません。"
      : "Project Folder of RPG Maker MV doesn't exist.";
    alert.setHeaderText(header);

    String content = locale.equals(Locale.JAPAN)
      ? "お手数ですが、再設定してください。"
      : "Please reset one.";
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

  public static void showFileNotFoundDialog() {//{{{
    Alert alert = new Alert(AlertType.ERROR);

    Locale locale = Locale.getDefault();
    String header = locale.equals(Locale.JAPAN)
      ? "ファイルが見つかりませんでした。"
      : "File is not found.";
    alert.setHeaderText(header);
    if (locale.equals(Locale.JAPAN))
      alert.setContentText("データを保存できませんでした。別名保存を実行してください。");
    else
      alert.setContentText("Please save as other file name.");

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

  public static void showFileEncodingDialog() {//{{{
    Alert alert = new Alert(AlertType.INFORMATION);
    Locale locale = Locale.getDefault();

    String header = locale.equals(Locale.JAPAN)
      ? "文字コードが対応していません。"
      : "UnsupportedEncodingException.";
    alert.setHeaderText(header);

    if (locale.equals(Locale.JAPAN)) {
      alert.setContentText("ファイルの文字コードにUTF-8を指定してください。");
    } else {
      alert.setContentText("Please set Encoding UTF-8 of Text File.");
    }
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

  public static final boolean r(String target, String regex) {//{{{
    try {
      Pattern p = Pattern.compile(regex);
      Matcher m = p.matcher(target);
      return m.find();
    } catch (PatternSyntaxException ignore) {
    }
    return false;
  }//}}}

}

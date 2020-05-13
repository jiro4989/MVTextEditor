package com.jiro4989.mvte.util;

import java.util.*;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

/**
 * 頻繁に利用するダイアログクラス。
 *
 * @version 1.0.0
 * @author Jiro
 */
public class DialogUtils {

  /** 言語変更時に表示するダイアログ。 */
  public static final void showLanguageDialog() { // {{{
    Locale locale = Locale.getDefault();

    Alert alert = new Alert(AlertType.INFORMATION);
    String header =
        locale.equals(Locale.JAPAN)
            ? "言語の変更は次回起動時に適用されます"
            : "Apply changing Languages when next Starting";
    alert.setHeaderText(header);

    alert.showAndWait();
  } // }}}

  /** 強制終了の可否を問うダイアログ。 */
  public static final void showForcedTerminationDialog() { // {{{
    Locale locale = Locale.getDefault();

    Alert alert = new Alert(AlertType.CONFIRMATION);

    String header =
        locale.equals(Locale.JAPAN)
            ? "通常の方法で本ソフトを終了できない場合以外は使用しないでください。"
            : "Should do this operation when you couldn't stop this application";
    alert.setHeaderText(header);

    String content = locale.equals(Locale.JAPAN) ? "本当に終了しますか？" : "Really execute ?";
    alert.setContentText(content);

    alert.showAndWait().filter(r -> r == ButtonType.OK).ifPresent(r -> Platform.exit());
  } // }}}
}

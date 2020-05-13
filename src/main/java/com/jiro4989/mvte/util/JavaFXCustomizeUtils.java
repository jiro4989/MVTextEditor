package com.jiro4989.mvte.util;

import javafx.scene.control.*;
import javafx.scene.input.*;

/** JavaFXのコンポーネントをカスタマイズしたものに変更するstaticメソッドをまとめ たユーティリティクラス。 */
public class JavaFXCustomizeUtils {

  /**
   * テキストフィールドを整数値の入力のみを可能にする。
   *
   * @param textField カスタム対象
   */
  public static void setIntegerOnlyOption(TextField textField) { // {{{
    textField
        .textProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              setNumberOnly(textField, oldVal, newVal);
            });
  } // }}}

  /**
   * newValに数値が渡された場合のみ、セットする。
   *
   * @param textField 対象テキストフィールド
   * @param oldVal 編集前のテキスト
   * @param newVal 編集後のテキスト
   */
  public static void setNumberOnly(TextField textField, String oldVal, String newVal) { // {{{
    if (!newVal.matches("[-]?[0-9]*")) {
      textField.setText(oldVal);
    }
  } // }}}

  /**
   * テキストフィールドの入力可能数値桁数上限をセットする。
   *
   * @param textField カスタム対象
   * @param min 最小値
   * @param max 最大値
   */
  public static void setMaxDigitOption(TextField textField, int min, int max) { // {{{
    textField
        .lengthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              if (oldVal.intValue() < newVal.intValue()) {
                int minLength = String.valueOf(min).length();
                int maxLength = String.valueOf(max).length();
                int digit = Math.max(minLength, maxLength);

                // 乳力されたテキストの桁がオーバーしたら変更前に戻す。
                String text = textField.getText();
                if (digit <= text.length()) {
                  textField.setText(text.substring(0, digit));
                }

                // 乳力された数値が上限値、あるいは下限値を超えていたら、修正する。
                int number = Integer.parseInt(text);
                number = Math.max(number, min);
                number = Math.min(number, max);
                textField.setText("" + number);
              }
            });
  } // }}}

  /**
   * マウススクロールとCtrl, Shiftを入力しながらでの数値の変動量を設定する。
   *
   * @param textField カスタム対象
   * @param ctrlVal Ctrlキーを押しながらでの数値の変動量
   * @param shiftVal Shiftキーを押しながらでの数値の変動量
   */
  public static void setScrollValueOption(TextField textField, int ctrlVal, int shiftVal) { // {{{
    textField.setOnScroll(
        e -> {
          String text = textField.getText();
          int value = getDefaultValueIfEmpty(text, 1);
          value = getValueWithScroll(e, value, ctrlVal, shiftVal);
          textField.setText("" + value);
        });
  } // }}}

  /**
   * マウススクロールで保持する数値をインクリメント・デクリメントする。 Ctrlキーを押しながらとShiftキーを押しながらの操作で数値の上昇量を操作できる 。
   *
   * @param e ScrollEvent
   * @param oldVal 数値変更前の値
   * @param sVal Ctrlを押しながらで変動する量
   * @param lVal Shiftを押しながらで変動する量
   * @return スクロールで変動した新たな数値
   */
  private static final int getValueWithScroll(
      ScrollEvent e, int oldVal, int sVal, int lVal) { // {{{
    int newVal =
        e.isControlDown()
            ? 0 < e.getDeltaY() ? oldVal + sVal : oldVal - sVal
            : e.isShiftDown()
                ? 0 < e.getDeltaX() ? oldVal + lVal : oldVal - lVal
                : 0 < e.getDeltaY() ? oldVal + 1 : oldVal - 1;

    return newVal;
  } // }}}

  /**
   * 渡したテキストが空かnullだった場合に、デフォルト値に修正した数値として返す 。
   *
   * @param text 検査対象のテキスト
   * @param defaultVal テキストが不正だった場合に返却される数値
   */
  private static int getDefaultValueIfEmpty(String text, int defaultVal) { // {{{
    if (text.equals("") || text == null) {
      return defaultVal;
    }

    return Integer.parseInt(text);
  } // }}}
}

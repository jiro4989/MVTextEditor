package jiro.java.util;

import java.io.*;
import java.util.*;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * プロパティファイルの操作を簡単にするためのラッパークラス。 単純にPropertiesクラスのラッパークラスであるが、以下の点で異なる。
 *
 * <ul>
 *   <li>{@link MyProperties#store()}メソッドは<b>XML形式</b>で出力する。<br>
 *       拡張子をpropertiesにする方法を用意していない。
 *   <li>getProperty()は{@code null}を返すことがないように{@link java.util.Optional}で ラップしている。
 *   <li>{@link javafx.stage.Stage}や{@link javafx.scene.Node}を渡すと、Propertiesに {@link
 *       javafx.stage.Stage}のx,y,width,height,maximized値を自動でセットするメ ソッドを持つ。<br>
 *       その際のキーはそれぞれx,y,widht,height,isMaximizedである。このキー文字列を変 更することはできない。
 *   <li>引数に{@link javafx.stage.Stage}を渡すと{@link javafx.stage.Stage}の
 *       x,y,width,height,maximizeプロパティを変更するメソッドを持つ。<br>
 *       ただし渡されたプロパティファイルにそれらの値がなかった場合はセットされない。
 * </ul>
 *
 * @version 1.2.0
 * @author Jiro
 */
public final class MyProperties {

  private final Properties properties;
  private final File file;

  /**
   * ファイルパスのプロパティを生成する。
   *
   * @param path 生成するプロパティファイルのパス
   */
  public MyProperties(String path) {
    this(new File(path));
  }

  /**
   * プロパティファイルを生成する。
   *
   * @param aFile 生成するプロパティファイル
   */
  public MyProperties(File aFile) { // {{{
    properties = new Properties();
    file = aFile;
  } // }}}

  /**
   * プロパティファイルをロードし、成功失敗を返す。
   *
   * @return 成功の可否
   */
  public boolean load() { // {{{
    if (file.exists()) {
      try (InputStream in = new FileInputStream(file)) {
        properties.loadFromXML(in);
        return true;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return false;
  } // }}}

  /**
   * Stsgrの座標と幅の設定を行う。 <b>事前に{@link MyProperties#load()}を成功({@code true})させていないといけ ない。</b>
   *
   * @param stage 座標、幅データを変更したいStageインスタンス
   */
  public void customStage(Stage stage) { // {{{
    String strX = properties.getProperty("x");
    String strY = properties.getProperty("y");
    String strW = properties.getProperty("width");
    String strH = properties.getProperty("height");

    if (strX != null) stage.setX(Double.parseDouble(strX));
    if (strY != null) stage.setY(Double.parseDouble(strY));
    if (strW != null) stage.setWidth(Double.parseDouble(strW));
    if (strH != null) stage.setHeight(Double.parseDouble(strH));

    String strM = properties.getProperty("isMaximized");
    if (strM != null) stage.setMaximized(Boolean.valueOf(strM));
  } // }}}

  /**
   * XML形式でプロパティファイルを出力する。
   *
   * @param comment コメント
   * @return 成功の可否
   */
  public boolean store(String comment) { // {{{
    try (FileOutputStream out = new FileOutputStream(file)) {
      properties.storeToXML(out, comment);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return false;
  } // }}}

  /**
   * XML形式でプロパティファイルを出力する。
   *
   * @return 成功の可否
   */
  public boolean store() {
    return store(null);
  }

  /**
   * ファイルの有無を返す。
   *
   * @return 存在するかしないか
   */
  public boolean exists() {
    return file.exists();
  }

  /**
   * キーから値を取得する。<br>
   * <b>戻り値はStringではなくOptionalである。</b>
   *
   * @param key キー
   * @return 値
   */
  public Optional<String> getProperty(String key) { // {{{
    return Optional.ofNullable(properties.getProperty(key));
  } // }}}

  /**
   * 整数型でプロパティを取得する。
   *
   * @param key キー
   * @param defVal デフォルト値
   * @return 整数
   */
  public int getIntegerValue(String key, String defVal) { // {{{
    String valStr = properties.getProperty(key);
    if (valStr == null) valStr = defVal;
    return Integer.parseInt(valStr);
  } // }}}

  /**
   * 小数型でプロパティを取得する。
   *
   * @param key キー
   * @param defVal デフォルト値
   * @return 小数
   */
  public double getDoubleValue(String key, String defVal) { // {{{
    String valStr = properties.getProperty(key);
    if (valStr == null) valStr = defVal;
    return Double.parseDouble(valStr);
  } // }}}

  /**
   * 真偽値型でプロパティを取得する。
   *
   * @param key キー
   * @param defVal デフォルト値
   * @return 真偽値
   */
  public boolean getBooleanValue(String key, String defVal) { // {{{
    String valStr = properties.getProperty(key);
    if (valStr == null) valStr = defVal;
    return Boolean.valueOf(valStr);
  } // }}}

  /**
   * キーと値をセットする。<br>
   * 実質的に{@link java.util.Properties#setProperty(java.lang.String, java.lang.String)}のラッパーメソッドである。
   *
   * @param key キー
   * @param value 値
   */
  public void setProperty(String key, String value) { // {{{
    properties.setProperty(key, value);
  } // }}}

  /**
   * Stageの座標、幅、最大化状態を対象Stage内に配置されているNodeからセットする 。<br>
   *
   * @param node 取得したいウィンドウに配置されているNode
   */
  public void setProperty(Node node) { // {{{
    Stage stage = (Stage) node.getScene().getWindow();
    setProperty(stage);
  } // }}}

  /**
   * Stageの座標、幅、最大化状態をセットする。<br>
   *
   * @param stage Stage
   */
  public void setProperty(Stage stage) { // {{{
    boolean isMaximized = stage.isMaximized();
    if (isMaximized) stage.setMaximized(false);

    properties.setProperty("x", "" + stage.getX());
    properties.setProperty("y", "" + stage.getY());
    properties.setProperty("width", "" + stage.getWidth());
    properties.setProperty("height", "" + stage.getHeight());
    properties.setProperty("isMaximized", "" + isMaximized);
  } // }}}

  /**
   * デフォルト言語を変更する。
   *
   * <p>設定ファイル内にlangsキーがセットしてあった場合、それに対応した言語に変更す る。
   *
   * <p>言語の判定は単純で、日本語の言語コードがセットされていれば日本語に変更し、 日本語でない言語コードの場合はすべて英語にセットされる。
   */
  public void changeLanguages() { // {{{
    String ja = Locale.JAPAN.getLanguage();
    String langs = getProperty("langs").orElse(ja);
    if (!langs.equals(ja)) Locale.setDefault(Locale.ENGLISH);
  } // }}}
}

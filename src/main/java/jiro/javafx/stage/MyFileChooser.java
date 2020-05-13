package jiro.javafx.stage;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FileChooserのラッパークラス。
 *
 * <p>標準のFileChooserクラスとの違いは下記のとおりである。
 *
 * <ul>
 *   <li>ダイアログを開いた時に取得したファイルのディレクトリを次回起動時に反映す るオプションを持つ。
 *   <li>ダイアログを開いた時に取得したファイルのファイル名を次回起動時に反映する オプションを持つ。
 *   <li>Properteisインスタンスを渡すことで、開いたファイルの初期ディレクトリや ファイル名を自動でセットするオプションを持つ。
 *   <li>標準のメソッドはnullを返すので、戻り値をOptionalでラッピングして返す。
 * </ul>
 *
 * @author Jiro
 * @version 1.1.1
 */
public class MyFileChooser {

  private File openedFile;

  private final FileChooser fc;
  private final boolean AUTO_SET_DIR;
  private final boolean AUTO_SET_FILE_NAME;
  private final Properties properties;
  private final String initDirKey;
  private final String initFileNameKey;

  private static final Stage STAGE_UTIL = new Stage(StageStyle.UTILITY);

  /** MyFileChooserのインスタンスを生成するためのBuilderクラス。 */
  public static class Builder { // {{{

    private final ExtensionFilter extensionFilter;
    private File initDir = new File(".");
    private String initFileName = "";
    private boolean autoSetDir = true;
    private boolean autoSetFileName = true;
    private Properties properties = null;
    private String initDirKey = null;
    private String initFileNameKey = null;

    /**
     * MyFileChooserインスタンスを生成するためのBuilderパターン
     *
     * @param desc 説明文
     * @param extension フィルタリングするファイル拡張子
     */
    public Builder(String desc, String extension) { // {{{

      this(new ExtensionFilter(desc, extension));

      if (desc == null) throw new NullPointerException("descパラメータ(説明文)にnullをセットできません。");

      if (extension == null) throw new NullPointerException("extensionパラメータ(拡張子)にnullをセットできません。");
    } // }}}

    /**
     * MyFileChooserインスタンスを生成するためのBuilderパターン
     *
     * @param ef ExtensionFilter
     */
    public Builder(ExtensionFilter ef) { // {{{

      if (ef == null) throw new NullPointerException("ExtensionFilterにnullをセットできません。");

      extensionFilter = ef;
    } // }}}

    /**
     * FileChooserを開いたときの初期ディレクトリをセットする。
     *
     * @param dirName 初期ディレクトリのパス文字列
     * @return Builderインスタンス
     */
    public Builder initDir(String dirName) { // {{{

      initDir(new File(dirName));
      return this;
    } // }}}

    /**
     * FileChooserを開いたときの初期ディレクトリをセットする。
     *
     * @param dir 初期ディレクトリを指すFile
     * @return Builderインスタンス
     */
    public Builder initDir(File dir) { // {{{

      if (dir.exists()) initDir = dir;
      return this;
    } // }}}

    /**
     * FileChooserを開いたときの初期ファイル名をセットする。
     *
     * @param fileName 初期ファイル名文字列
     * @return Builderインスタンス
     */
    public Builder initFileName(String fileName) { // {{{

      initFileName = fileName;
      return this;
    } // }}}

    /**
     * 開いたファイルから次回起動時のフォルダを変更するオプションをセットする。
     *
     * @param bool ON or OFF
     * @return Builderインスタンス
     */
    public Builder autoSetDir(boolean bool) { // {{{

      autoSetDir = bool;
      return this;
    } // }}}

    /**
     * 開いたファイルから次回起動時のファイル名を変更するオプションをセットする 。
     *
     * @param bool ON or OFF
     * @return Builderインスタンス
     */
    public Builder autoSetFileName(boolean bool) { // {{{

      autoSetFileName = bool;
      return this;
    } // }}}

    /**
     * 開いたファイルのパスを自動でPropertiesにセットするための保存対象 Properteisをセットする。 このオプションはinitDirKey(),
     * initFileNameKey()オプションを使用する場合は 必須である。
     *
     * @param prop 開いたファイルのパスをセットするProperties
     * @return Builderインスタンス
     */
    public Builder properties(Properties prop) { // {{{

      properties = prop;
      return this;
    } // }}}

    /**
     * 初期ディレクトリのパスを保存するときの、Propertiesで使用するキーをセット する。
     *
     * @param key 初期ディレクトリパスをPropertiesに保存するときのキー
     * @return Builderインスタンス
     */
    public Builder initDirKey(String key) { // {{{

      initDirKey = key;
      return this;
    } // }}}

    /**
     * 初期ファイル名を保存するときの、Propertiesで使用するキーをセットする。
     *
     * @param key 初期ファイル名をPropertiesに保存するときのキー
     * @return Builderインスタンス
     */
    public Builder initFileNameKey(String key) { // {{{

      initFileNameKey = key;
      return this;
    } // }}}

    /**
     * MyFileChooserのインスタンスを生成する。 initDirKeyとinitFileNameKeyをオプションにセットした時に、同時にproperties
     * もセットしていなかった場合、例外を返す。
     *
     * @return MyFileChooserインスタンス
     */
    public MyFileChooser build() { // {{{

      return new MyFileChooser(this);
    } // }}}
  } // }}}

  /** privateコンストラクタ. */
  private MyFileChooser(Builder builder) { // {{{

    fc = new FileChooser();

    fc.getExtensionFilters().add(builder.extensionFilter);
    fc.setInitialDirectory(builder.initDir);
    fc.setInitialFileName(builder.initFileName);

    AUTO_SET_DIR = builder.autoSetDir;
    AUTO_SET_FILE_NAME = builder.autoSetFileName;

    properties = builder.properties;
    initDirKey = builder.initDirKey;
    initFileNameKey = builder.initFileNameKey;

    if ((properties == null && initDirKey != null)
        || (properties == null && initFileNameKey != null)) {

      throw new NullPointerException(
          "propertiesが未定義の状態でinitDirKeyまたはinitFileNameKeyを設定することはできません。");
    }
  } // }}}

  // ************************************************************
  //
  // ダイアログ表示メソッド
  //
  // ************************************************************

  /**
   * ファイル選択ダイアログを開く。
   *
   * <p>実質的に{@link javafx.stage.FileChooser#showOpenDialog(javafx.stage.Window)
   * showOpenDialog}のラッピングをしているメソッドだが、オプションをセットしてい た場合に、次回起動時の初期ディレクトリが変更されている。
   *
   * <p>また、戻り値にnullが来る可能性があるため、Optionalでラッピングした戻 り値を返す。
   *
   * @return 開いたファイル
   */
  public Optional<File> openFile() { // {{{

    checkCanShowDialog();
    File file = fc.showOpenDialog(STAGE_UTIL);
    setInitDir(file);
    return Optional.ofNullable(file);
  } // }}}

  /**
   * 複数ファイル選択ダイアログを開く。
   *
   * <p>実質的に{@link javafx.stage.FileChooser#showOpenMultipleDialog(javafx.stage.Window)
   * showOpenMultipleDialog}のラッピングをしているメソッドだが、オプションをセッ トしていた場合に、次回起動時の初期ディレクトリが変更されている。
   *
   * <p>初期ディレクトリを変更する場合は、開いた複数のファイルの内の先頭の要 素をセットする。
   *
   * <p>また、戻り値にnullが来る可能性があるため、Optionalでラッピングした戻 り値を返す。
   *
   * @return 開いたファイルのリスト
   */
  public Optional<List<File>> openFiles() { // {{{

    checkCanShowDialog();
    List<File> files = fc.showOpenMultipleDialog(STAGE_UTIL);
    setInitDir(files);
    return Optional.ofNullable(files);
  } // }}}

  /**
   * ファイル保存ダイアログを開く。
   *
   * <p>実質的に{@link javafx.stage.FileChooser#showSaveDialog(javafx.stage.Window)
   * showSaveDialog}のラッピングをしているメソッドだが、オプションをセットしてい た場合に、次回起動時の初期ディレクトリやファイル名が変更される。
   *
   * <p>また、戻り値にnullが来る可能性があるため、Optionalでラッピングした戻 り値を返す。
   *
   * @return 保存したファイル
   */
  public Optional<File> saveFile() { // {{{

    checkCanShowDialog();
    File file = fc.showSaveDialog(STAGE_UTIL);
    setInitDir(file);
    setInitFileName(file);
    return Optional.ofNullable(file);
  } // }}}

  // ************************************************************
  //
  // private メソッド
  //
  // ************************************************************

  private void setInitDir(List<File> files) { // {{{

    if (files != null) setInitDir(files.get(0));
  } // }}}

  private void setInitDir(File file) { // {{{

    if (file != null) {
      openedFile = file;

      if (AUTO_SET_DIR) {

        File parent = file.getParentFile();
        fc.setInitialDirectory(parent != null ? parent : new File("."));
        if (initDirKey != null) properties.setProperty(initDirKey, parent.toString());
      }
    }
  } // }}}

  private void setInitFileName(File file) { // {{{

    if (file != null) {

      if (AUTO_SET_FILE_NAME) {

        String fileName = file.getName();
        fc.setInitialFileName(fileName);
        if (initFileNameKey != null) properties.setProperty(initFileNameKey, fileName);
      }
    }
  } // }}}

  private void checkCanShowDialog() { // {{{

    File file = fc.getInitialDirectory();
    if (!file.exists()) fc.setInitialDirectory(new File("."));
  } // }}}

  // ************************************************************
  //
  // Getter
  //
  // ************************************************************

  /**
   * 初期ディレクトリを返す。
   *
   * @return 初期ディレクトリ
   */
  public File getInitialDirectory() { // {{{

    return fc.getInitialDirectory();
  } // }}}

  public Optional<File> getOpenedFile() {
    return Optional.ofNullable(openedFile);
  }

  // ************************************************************
  //
  // Setter
  //
  // ************************************************************

  /**
   * 初期ファイル名をセットする。
   *
   * @param fileName 初期ファイル名
   */
  public void setInitialFileName(String fileName) { // {{{

    fc.setInitialFileName(fileName);
  } // }}}
}

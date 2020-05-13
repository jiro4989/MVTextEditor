package jiro.javafx.stage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;
import javafx.scene.layout.*;

/** {@link AboutStage}のコントローラクラス。 */
public class AboutStageController {

  // FXML Component//{{{

  @FXML private ImageView appIconImageView;

  // 各種ラベルなどを保持するペイン
  @FXML private GridPane gridPane;

  // アプリ名
  @FXML private Label appDescLabel;
  @FXML private Label appLabel;

  // 作者情報
  @FXML private HBox authorHBox;
  @FXML private Label authorDescLabel;
  @FXML private Label authorLabel;
  @FXML private ImageView authorIconImageView;

  // ブログ情報
  @FXML private HBox blogHBox;
  @FXML private Label blogDescLabel;
  @FXML private Label blogLabel;
  @FXML private Hyperlink blogHyperlink;

  @FXML private Button closeButton;

  // }}}

  // ************************************************************
  // 初期化 メソッド
  // ************************************************************

  /** FXML初期化 */
  @FXML
  private void initialize() { // {{{

    changeLanguages();
  } // }}}

  /** 言語を変更する。 */
  private void changeLanguages() { // {{{

    Locale locale = Locale.getDefault();
    if (locale == Locale.JAPAN) {

      appDescLabel.setText(Langs.JP.APP);
      authorDescLabel.setText(Langs.JP.AUTHOR);
      blogDescLabel.setText(Langs.JP.BLOG);

    } else {

      appDescLabel.setText(Langs.EN.APP);
      authorDescLabel.setText(Langs.EN.AUTHOR);
      blogDescLabel.setText(Langs.EN.BLOG);
    }
  } // }}}

  // ************************************************************
  // FXMLイベントメソッド
  // ************************************************************

  /** ウィンドウを閉じる。 */
  @FXML
  private void closeButtonOnAction() { // {{{

    closeButton.getScene().getWindow().hide();
  } // }}}

  /**
   * ハイパーリンククリックで関連付けブラウザでURLを開く。<br>
   * 関連付けブラウザが存在しなかった場合は警告を表示する。
   */
  @FXML
  private void blogHyperlinkOnAction() { // {{{

    Desktop desktop = Desktop.getDesktop();
    try {

      URI uri = new URI(blogHyperlink.getText());
      desktop.browse(uri);

    } catch (URISyntaxException e) {

      e.printStackTrace();

    } catch (IOException e) {

      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setHeaderText("関連付けられたブラウザがありません。");
      alert.setContentText("お手数ですが、リンクをコピーして手動で移動してください。");
      alert.showAndWait();
    }
  } // }}}

  // ************************************************************
  // package メソッド
  // ************************************************************

  /** 作者情報レイアウトを削除する。 */
  void removeAuthorPane() { // {{{

    gridPane.getChildren().remove(authorDescLabel);
    gridPane.getChildren().remove(authorHBox);
  } // }}}

  /** ブログ情報レイアウトを削除する。 */
  void removeBlogPane() { // {{{

    gridPane.getChildren().remove(blogDescLabel);
    gridPane.getChildren().remove(blogHBox);
  } // }}}

  /** アプリアイコン画像を削除する。 */
  void removeAppIcon() { // {{{

    gridPane.getChildren().remove(appIconImageView);
  } // }}}

  // ************************************************************
  // Setter
  // ************************************************************

  /**
   * 作者名をセットする。
   *
   * @param author 作者名
   */
  void setAuthor(String author) { // {{{

    authorLabel.setText(author);
  } // }}}

  /**
   * ブログ名をセットする。
   *
   * @param blog ブログ名
   */
  void setBlog(String blog) { // {{{

    blogLabel.setText(blog);
  } // }}}

  /**
   * ブログURLをセットする。
   *
   * @param link ブログリンクｖ
   */
  void setBlogUrl(String url) { // {{{

    blogHyperlink.setText(url);
  } // }}}

  /**
   * アプリ名をセットする。
   *
   * @param appName アプリ名
   */
  void setAppName(String appName) { // {{{

    appLabel.setText(appName);
  } // }}}

  /**
   * アプリアイコン画像をセットする。
   *
   * @param imgUrl アプリアイコン画像のURL
   */
  void setAppIcon(String imgUrl) { // {{{

    appIconImageView.setImage(new Image(getClass().getResource(imgUrl).toExternalForm()));
  } // }}}

  /**
   * 作者アイコン画像をセットする。
   *
   * @param imgUrl 作者アイコン画像
   */
  void setAuthorIcon(String imgUrl) { // {{{

    authorIconImageView.setImage(new Image(getClass().getResource(imgUrl).toExternalForm()));
  } // }}}
}

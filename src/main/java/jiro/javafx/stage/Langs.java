package jiro.javafx.stage;

enum Langs {
  JP("バージョン情報", "ソフト名 :", "作者 :", "作者ブログ :", "閉じる"),
  EN("About", "App Name :", "Author :", "Author Blog :", "CLOSE");

  final String STAGE_TITLE;
  final String APP;
  final String AUTHOR;
  final String BLOG;
  final String CLOSE;

  private Langs(String title, String app, String author, String blog, String close) {

    STAGE_TITLE = title;
    APP = app;
    AUTHOR = author;
    BLOG = blog;
    CLOSE = close;
  }
}

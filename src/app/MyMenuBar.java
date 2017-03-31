package app;

import jiro.java.lang.Brackets;
import jiro.java.lang.FormattableText;
import jiro.javafx.stage.MyFileChooser;

import java.io.IOException;
import javafx.scene.control.*;

class MyMenuBar {
  private final MainController mainController;
  private final MyFileChooser textFileManager;

  private final MenuItem openTextFileMenuItem;

  MyMenuBar(MainController mc, MenuItem openTextFileMenuItem) {//{{{
    mainController = mc;
    textFileManager = new MyFileChooser.Builder("Text Files", "*.txt").build();

    this.openTextFileMenuItem = openTextFileMenuItem;
    this.openTextFileMenuItem.setOnAction(e -> {
      textFileManager.openFile().ifPresent(file -> {
        // TODO 一時変数
        final int RETURN_SIZE = 27 * 2;
        final int INDENT_SIZE = 2;
        final Brackets BRACKETS = Brackets.TYPE1;

        // TODO test code
        try {
          FormattableText ft = new FormattableText.Builder(file)
            .actorNameOption(true)
            .returnOption(true)
            .returnSize(RETURN_SIZE)
            .indentOption(true)
            .indentSize(INDENT_SIZE)
            .bracketsOption(true)
            .brackets(BRACKETS)
            .joiningOption(false)
            .build();
          mainController.setTextList(ft.format().getTextList());
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }
      });
    });
  }//}}}

}

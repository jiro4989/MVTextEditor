package com.jiro4989.mvte.table;

import static com.jiro4989.mvte.util.Texts.*;
import static com.jiro4989.mvte.util.Utils.r;

import com.jiro4989.mvte.MainController;
import com.jiro4989.mvte.selector.ImageSelector;
import com.jiro4989.mvte.util.Brackets;
import com.jiro4989.mvte.util.JsonUtils;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javax.xml.parsers.ParserConfigurationException;
import jiro.java.util.MyProperties;

public class TextTable {

  private final MainController mainController;
  private Optional<List<TextDB>> copyTextDBs = Optional.empty();

  private static final String CR = System.lineSeparator();

  // fxml component {{{
  private final TextField tableFilterTextField;
  private final TableView<TextDB> tableView;
  private final TableColumn<TextDB, String> iconColumn;
  private final TableColumn<TextDB, String> actorNameColumn;
  private final TableColumn<TextDB, String> textColumn;
  private final TableColumn<TextDB, String> backgroundColumn;
  private final TableColumn<TextDB, String> positionColumn;
  // }}}

  // 変数パネルの検索される元のデータベース
  private final ObservableList<TextDB> masterData;

  public TextTable(
      MainController mc,
      TextField tftf,
      TableView<TextDB> tv,
      TableColumn<TextDB, String> ic,
      TableColumn<TextDB, String> anc,
      TableColumn<TextDB, String> tc,
      TableColumn<TextDB, String> bgc,
      TableColumn<TextDB, String> pc) { // {{{

    mainController = mc;
    tableFilterTextField = tftf;
    tableView = tv;
    iconColumn = ic;
    actorNameColumn = anc;
    textColumn = tc;
    backgroundColumn = bgc;
    positionColumn = pc;
    masterData = FXCollections.observableArrayList();

    iconColumn.setCellValueFactory(new PropertyValueFactory<TextDB, String>("icon"));
    actorNameColumn.setCellValueFactory(new PropertyValueFactory<TextDB, String>("actorName"));
    textColumn.setCellValueFactory(new PropertyValueFactory<TextDB, String>("text"));
    backgroundColumn.setCellValueFactory(new PropertyValueFactory<TextDB, String>("background"));
    positionColumn.setCellValueFactory(new PropertyValueFactory<TextDB, String>("position"));

    iconColumn.setCellFactory(col -> new ImageTableCell());

    tableView
        .getFocusModel()
        .focusedCellProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              updateTextView();
            });

    tableView.setOnMouseClicked(
        e -> {
          if (e.getClickCount() == 2) {
            openImageSelector();
          }
        });

    tableView.setOnKeyPressed(
        e -> {
          if (KeyCode.J == e.getCode() && !e.isControlDown()) {
            selectNext();
          } else if (KeyCode.K == e.getCode() && !e.isControlDown()) {
            selectPrevious();
          } else if (KeyCode.ENTER == e.getCode()) {
            openImageSelector();
          }
        });

    tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    // tableView.getSelectionModel().setCellSelectionEnabled(true);

    // masterData.add(createInitTextDB());

    // テーブルのフィルタリング
    FilteredList<TextDB> filteredData = new FilteredList<>(masterData, p -> true);
    tableFilterTextField
        .textProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              filteredData.setPredicate(db -> existsMatchedText(db, newVal));
            });
    tableView.setItems(filteredData);

    tableView.getSelectionModel().selectFirst();
  } // }}}

  // public methods

  public void saveXml(File file) throws ParserConfigurationException { // {{{
    SavingData data = new SavingData(tableView.getItems());
    data.saveXml(file);
  } // }}}

  public void exportJson(File file, int id) throws FileNotFoundException, IOException { // {{{
    JsonMap data = new JsonMap(1, new ArrayList<>(masterData));
    JsonUtils.writeValue(file, data);

    MainController.preferencesProperties
        .getProperty("project")
        .ifPresent(
            dir -> {
              try {
                final String SP = File.separator;
                JsonUtils.updateMapInfos(
                    new File(dir + SP + "data" + SP + "MapInfos.json"), file.getName(), id);
              } catch (IOException e) {
                com.jiro4989.mvte.util.MyLogger.log("ファイル出力に失敗しましたエラー", e);
              } catch (Exception e) {
                com.jiro4989.mvte.util.MyLogger.log(e);
              }
            });
  } // }}}

  public void changeIconIndex(int index) { // {{{
    getSelectedItems()
        .ifPresent(
            selectedItems -> {
              selectedItems
                  .stream()
                  .forEach(
                      selectedItem -> {
                        selectedItem.setIconIndex(index);
                      });
            });
    updateTextView();
  } // }}}

  public void cutRecords() { // {{{
    copyRecords();
    deleteRecords();
  } // }}}

  public void copyRecords() { // {{{
    getSelectedItems()
        .ifPresent(
            selectedItems -> {
              copyTextDBs = Optional.ofNullable(new ArrayList<>(selectedItems));
            });
  } // }}}

  public void pasteRecords() { // {{{
    getSelectedItems()
        .ifPresent(
            i -> {
              copyTextDBs
                  .map(l -> l.stream().map(TextDB::new).collect(Collectors.toList()))
                  .ifPresent(
                      records -> {
                        int index = tableView.getSelectionModel().getSelectedIndices().get(0);
                        masterData.addAll(index + 1, records);
                      });
            });
  } // }}}

  public void deleteRecords() { // {{{
    int index = tableView.getSelectionModel().getSelectedIndices().get(0);
    getSelectedItems()
        .ifPresent(
            selectedItems -> {
              masterData.removeAll(selectedItems);
            });
    if (0 < index) selectNext();
    if (masterData.size() < 1) {
      masterData.add(createInitTextDB());
      tableView.getSelectionModel().selectFirst();
    }
  } // }}}

  public void selectPrevious() { // {{{
    int index = tableView.getSelectionModel().getSelectedIndex();
    index = Math.max(0, --index);
    tableView.getSelectionModel().clearAndSelect(index);
    tableView.scrollTo(index);
  } // }}}

  public void selectNext() { // {{{
    int index = tableView.getSelectionModel().getSelectedIndex();
    int max = tableView.getItems().size();
    index = Math.min(++index, max);
    tableView.getSelectionModel().clearAndSelect(index);
    tableView.scrollTo(index);
  } // }}}

  public void updateSelectedRecords() { // {{{
    getSelectedItems()
        .ifPresent(
            items -> {
              getSelectedItem()
                  .ifPresent(
                      si -> {
                        TextDB db = si;
                        items
                            .stream()
                            .forEach(
                                item -> {
                                  TextDB newDb = new TextDB(db);
                                  item.setIcon(newDb.iconProperty().get());
                                  item.setActorName(newDb.actorNameProperty().get());
                                  item.setText(newDb.textProperty().get());
                                  item.setBackground(newDb.backgroundProperty().get());
                                  item.setPosition(newDb.positionProperty().get());
                                });
                      });
            });
  } // }}}

  public void addNewRecord() { // {{{
    getSelectedItem()
        .ifPresent(
            si -> {
              int index = tableView.getSelectionModel().getSelectedIndex();
              masterData.add(index + 1, new TextDB());
            });
  } // }}}

  private String mkReturnedString(
      int size, String text, boolean textIndent, String indent, boolean facePathExists) { // {{{
    int len = len(text);

    if (facePathExists) size -= FACE_FONT_SIZE;

    if (len <= size) {
      return text;
    }

    int cnt = 0;
    StringBuilder sb = new StringBuilder();
    for (String s : text.split("")) {
      int a = len(s);
      cnt += a;
      sb.append(s);

      if (size <= cnt) {
        sb.append(CR);
        cnt = 0;

        if (textIndent) {
          sb.append(indent);
          cnt += len(indent);
        }
      }
    }
    return sb.toString();
  } // }}}

  private void textFormat() { // {{{
    getSelectedItems()
        .ifPresent(
            items -> {
              MyProperties mp = MainController.formatProperties;
              String sizeStr = mp.getProperty("textReturnSize").orElse("54");

              int size = Integer.parseInt(sizeStr);

              boolean textIndent = mp.getProperty("textIndent").map(Boolean::valueOf).orElse(true);
              int braIndex = mp.getProperty("bracketStart").map(s -> len(s)).orElse(0);
              int braLen = len(Brackets.values()[braIndex].START);
              String indent = String.format("%" + braLen + "s", "");

              items
                  .stream()
                  .forEach(
                      item -> {
                        String icon = item.iconProperty().get().replaceAll("\\s", "");
                        boolean facePathExists = !Objects.equals(icon, "");
                        String text = item.textProperty().get();

                        BufferedReader br = new BufferedReader(new StringReader(text));
                        List<String> lines =
                            br.lines()
                                .map(
                                    l ->
                                        mkReturnedString(
                                            size, l, textIndent, indent, facePathExists))
                                .collect(Collectors.toList());

                        String formattedText = String.join(CR, lines);
                        item.setText(formattedText);
                        updateTextView();
                      });
            });
  } // }}}

  /** 複数レコードの連結をしないタイプのjoin */
  public void format() { // {{{
    MyProperties mp = MainController.formatProperties;
    int size = mp.getProperty("textReturnSize").map(Integer::parseInt).orElse(54);
    int braIndex = mp.getProperty("bracketStart").map(s -> len(s)).orElse(0);
    int braLen = len(Brackets.values()[braIndex].START);
    String indent = String.format("%" + braLen + "s", "");

    getSelectedItems()
        .ifPresent(
            items -> {
              items
                  .stream()
                  .forEach(
                      item -> {
                        String joinedString = item.textProperty().get();
                        BufferedReader br = new BufferedReader(new StringReader(joinedString));
                        final String REG = "^.*[^(,\\.!\\?、。！？)]$";
                        String newJoinedString =
                            br.lines()
                                .map(
                                    s -> {
                                      if (s.matches(REG)) {
                                        if (s.matches(".*[a-zA-Z0-9]$")) {
                                          return s + " ";
                                        }
                                        return s;
                                      }
                                      return s + CR + indent;
                                    })
                                .map(s -> s.replaceAll("^ +", ""))
                                .collect(Collectors.joining());

                        item.textProperty().set(newJoinedString);
                      });
              textFormat();
            });
  } // }}}

  /**
   * 複数のレコードの文字列を連結する。<br>
   * 連結を行う際の挙動は、以下の仕様を満たす必要がある。
   *
   * <ol>
   *   <li>連結する際、インデント文字を自動で削除する。
   *   <li>文字列を折り返す際、インデントを自動で付与する。
   * </ol>
   */
  public void join() { // {{{
    getSelectedItems()
        .ifPresent(
            items -> {
              MyProperties mp = MainController.formatProperties;

              int size = mp.getProperty("textReturnSize").map(Integer::parseInt).orElse(54);
              int braIndex = mp.getProperty("bracketStart").map(s -> len(s)).orElse(0);
              int braLen = len(Brackets.values()[braIndex].START);
              String indent = String.format("%" + braLen + "s", "");

              String joinedString =
                  items.stream().map(i -> i.textProperty().get()).collect(Collectors.joining(CR));
              BufferedReader br = new BufferedReader(new StringReader(joinedString));
              final String REG = "^.*[^(,\\.!\\?、。！？)]$";
              String newJoinedString =
                  br.lines()
                      .map(
                          s -> {
                            if (s.matches(REG)) {
                              if (s.matches(".*[a-zA-Z0-9]$")) {
                                return s + " ";
                              }
                              return s;
                            }
                            return s + CR + indent;
                          })
                      .map(s -> s.replaceAll("^ +", ""))
                      .collect(Collectors.joining());

              items.get(0).textProperty().set(newJoinedString);
              masterData.removeAll(items.subList(1, items.size()));
              textFormat();
            });
  } // }}}

  public void deleteFaceImages() { // {{{
    getSelectedItems()
        .ifPresent(
            items -> {
              items
                  .stream()
                  .forEach(
                      item -> {
                        item.setIcon("");
                      });
            });
    updateTextView();
  } // }}}

  // private methods

  private Optional<TextDB> getSelectedItem() { // {{{
    SelectionModel<TextDB> model = tableView.getSelectionModel();
    if (!model.isEmpty()) {
      return Optional.ofNullable(model.getSelectedItem());
    }
    return Optional.empty();
  } // }}}

  private Optional<ObservableList<TextDB>> getSelectedItems() { // {{{
    MultipleSelectionModel<TextDB> model = tableView.getSelectionModel();
    if (!model.isEmpty()) {
      List<TextDB> list = model.getSelectedItems().stream().collect(Collectors.toList());
      ObservableList<TextDB> selectedItems = FXCollections.observableArrayList(list);
      return Optional.ofNullable(selectedItems);
    }
    return Optional.empty();
  } // }}}

  private void updateTextView() { // {{{
    getSelectedItem()
        .ifPresent(
            item -> {
              mainController.updateTextView(item);
            });
  } // }}}

  // setter

  public void setTextDB(List<TextDB> dbs) { // {{{
    tableView.getSelectionModel().clearSelection();
    masterData.clear();
    dbs.stream()
        .forEach(
            db -> {
              masterData.add(db);
            });
    tableView.getSelectionModel().selectFirst();
  } // }}}

  public void setTextList(List<List<String>> listList) { // {{{
    tableView.getSelectionModel().clearSelection();
    masterData.clear();
    listList
        .stream()
        .forEach(
            list -> {
              String bg = getBackgroundInitText();
              String pos = getPositionInitText();
              masterData.add(new TextDB("", list, bg, pos));
            });
  } // }}}

  // データベースのsetter

  public void setActorName(String value) { // {{{
    getSelectedItem()
        .ifPresent(
            selectedItem -> {
              selectedItem.setActorName(value);
            });
  } // }}}

  public void setText(String value) { // {{{
    getSelectedItem()
        .ifPresent(
            selectedItem -> {
              selectedItem.setText(value);
            });
  } // }}}

  public void setBackground(String value) { // {{{
    getSelectedItem()
        .ifPresent(
            selectedItem -> {
              selectedItem.setBackground(value);
            });
  } // }}}

  public void setPosition(String value) { // {{{
    getSelectedItem()
        .ifPresent(
            selectedItem -> {
              selectedItem.setPosition(value);
            });
  } // }}}

  public void setActorNames(String value) { // {{{
    getSelectedItems()
        .ifPresent(
            items -> {
              items
                  .stream()
                  .forEach(
                      item -> {
                        item.setActorName(value);
                      });
            });
  } // }}}

  public void setTexts(String value) { // {{{
    getSelectedItems()
        .ifPresent(
            items -> {
              items
                  .stream()
                  .forEach(
                      item -> {
                        item.setText(value);
                      });
            });
  } // }}}

  public void addInitRecord() { // {{{
    tableView.getSelectionModel().clearSelection();
    masterData.clear();
    masterData.add(createInitTextDB());
    tableView.getSelectionModel().selectFirst();
  } // }}}

  public void clear() { // {{{
    tableView.getSelectionModel().clearSelection();
    masterData.clear();
  } // }}}

  // private methods

  private boolean existsMatchedText(TextDB db, String newVal) { // {{{
    if (newVal == null || newVal.isEmpty()) {
      return true;
    }

    String actorName = db.actorNameProperty().get();
    String text = db.textProperty().get();
    String bg = db.backgroundProperty().get();
    String pos = db.positionProperty().get();

    return (r(actorName, newVal) || r(text, newVal) || r(bg, newVal) || r(pos, newVal));
  } // }}}

  private TextDB createInitTextDB() { // {{{
    String bg = getBackgroundInitText();
    String pos = getPositionInitText();
    return new TextDB("", "", "", bg, pos);
  } // }}}

  private void openImageSelector() { // {{{
    getSelectedItems()
        .ifPresent(
            items -> {
              TextDB db = items.get(0);
              String icon = db.iconProperty().get();
              if (icon != null || icon.length() == 0) {
                String path = createFilePath(icon.split(":"));
                ImageSelector selector = new ImageSelector(path);
                selector.showAndWait();
                boolean selected = selector.isSelected();

                if (selected) {
                  selector
                      .getSelectedTileString()
                      .ifPresent(
                          s -> {
                            items
                                .stream()
                                .forEach(
                                    item -> {
                                      item.setIcon(s);
                                      updateTextView();
                                    });
                          });
                }
              }
            });
  } // }}}
}

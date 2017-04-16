package app.table;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class SavingData {

  private final Document doc;
  private final Element root;

  private static final String ROOT = "root";
  private static final String NODE = "node";

  private static final String ICON       = "icon";
  private static final String ACTOR_NAME = "actorName";
  private static final String TEXT       = "text";
  private static final String BG         = "background";
  private static final String POS        = "position";

  SavingData(List<TextDB> dbList) throws ParserConfigurationException {//{{{
    doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    root = doc.createElement(ROOT);
    doc.appendChild(root);

    dbList.stream().forEach(db -> {
      Element node = doc.createElement(NODE);
      node . setAttribute(ICON       , db . iconProperty()       . get());
      node . setAttribute(ACTOR_NAME , db . actorNameProperty()  . get());
      node . setAttribute(TEXT       , db . textProperty()       . get());
      node . setAttribute(BG         , db . backgroundProperty() . get());
      node . setAttribute(POS        , db . positionProperty()   . get());
      root.appendChild(node);
    });
  }//}}}

  /**
   * xmlファイルからTextDBのリストを生成する。
   * @param file xml file
   * @return TextDBのデータリスト
   */
  public static List<TextDB> convertTextDB(File xmlFile) throws SAXException, ParserConfigurationException, IOException {//{{{
    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
    Element root = document.getDocumentElement();
    NodeList children = root.getChildNodes();

    List<TextDB> dbList = new ArrayList<>();
    for (int i=0; i<children.getLength(); i++) {
      Node node = children.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) node;

        String icon      = element.getAttribute(ICON);
        String actorName = element.getAttribute(ACTOR_NAME);
        String text      = element.getAttribute(TEXT);
        String bg        = element.getAttribute(BG);
        String pos       = element.getAttribute(POS);

        dbList.add(new TextDB(icon, actorName, text, bg, pos));
      }
    }

    return dbList;
  }//}}}

  void saveXml(File file) {//{{{
    try {
      DOMSource src = new DOMSource(root);

      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.ENCODING , "UTF-8");
      transformer.setOutputProperty(OutputKeys.INDENT   , "yes");
      transformer.setOutputProperty(OutputKeys.METHOD   , "xml");

      FileOutputStream fos = new FileOutputStream(file);
      StreamResult result  = new StreamResult(fos);

      transformer.transform(src, result);
    } catch (TransformerConfigurationException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (TransformerException e) {
      e.printStackTrace();
    }
  }//}}}

}

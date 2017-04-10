package app.save;

import app.table.TextDB;

import java.io.*;
import java.util.*;
import java.nio.file.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

public class SavingData {

  private final Document doc;
  private final Element root;

  private static final String ROOT = "root";
  private static final String NODE = "node";
  private static final String ICON = "icon";
  private static final String NAME = "name";
  private static final String TEXT = "text";
  private static final String BG   = "background";
  private static final String POS  = "position";

  public SavingData(List<TextDB> dbList) throws ParserConfigurationException {//{{{
    doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    root = doc.createElement(ROOT);
    doc.appendChild(root);

    dbList.stream().forEach(db -> {
      Element node = doc.createElement(NODE);
      node.setAttribute(ICON, db.iconProperty().get());
      root.appendChild(node);
    });
  }//}}}

  public void write(Path path) {//{{{
    try {
      DOMSource src = new DOMSource(root);

      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.ENCODING , "UTF-8");
      transformer.setOutputProperty(OutputKeys.INDENT   , "UTF-8");
      transformer.setOutputProperty(OutputKeys.METHOD   , "xml");

      FileOutputStream fos = new FileOutputStream(path.toFile());
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

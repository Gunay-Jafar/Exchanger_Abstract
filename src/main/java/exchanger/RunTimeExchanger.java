package exchanger;


import entity.Cbar_content;
import entity.Cbar_date;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import util.GeneralUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class RunTimeExchanger extends ExchangerAbstract {

    HashMap<String, HashMap<String, Cbar_content>> currencyContent = new HashMap<>();

    @Override
    boolean readAndCalculateExchangeValue(String amount, String date, String mezenne) {
        if (!notExist(date)){
            System.out.println(date+" tarixde melumat movcuddur!");
            return false;
        }
        BigDecimal amountCast = new BigDecimal(amount);
        String m=currencyContent.get(date).get(mezenne).getValue();
        BigDecimal currency = new BigDecimal(m);
        BigDecimal result = amountCast.divide(currency, 3, RoundingMode.HALF_UP);
        System.out.println(result.toString());
        return true;
    }

    @Override
    void getCurrencyDataWithDateAndSave(String date) throws Exception {
        if (!notExist(date)){
            System.out.println(date+" tarixde melumat movcuddur!");
            return;
        }
        HashMap<String, Cbar_content> cbarContentItem = new HashMap<>();
        Cbar_date cd = new Cbar_date();
        cd.setDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        URL url = new URL("https://www.cbar.az/currencies/" + date + ".xml");
        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = dBuilder.parse(url.toURI().toString());
        doc.getDocumentElement().normalize();

        NodeList n1 = doc.getElementsByTagName("ValType").item(1).getChildNodes();
        for (int i = 0, len = n1.getLength(); i < len; i++) {
            Node tempNode = n1.item(i);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) tempNode;
                Cbar_content cc = new Cbar_content();
                cc.setValue(eElement.getElementsByTagName("Value").item(0).getTextContent());
                cc.setNominal(eElement.getElementsByTagName("Nominal").item(0).getTextContent());
                cc.setName(eElement.getElementsByTagName("Name").item(0).getTextContent());
                cc.setCode(eElement.getAttribute("Code"));
                cc.setCbar_date(cd);
                cbarContentItem.put(cc.getCode(), cc);
                System.out.println(cc.getCode() + "yaddasa verildi!");
            }
        }
        currencyContent.put(date, cbarContentItem);



    }

    @Override
    boolean notExist(String date) {
        return (!currencyContent.containsKey(date));
    }


    @Override
    boolean enterExchangeInfo() throws Exception {
        return false;
    }


}

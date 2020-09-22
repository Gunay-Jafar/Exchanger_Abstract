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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class RunTimeExchanger extends ExchangerAbstract {

    HashMap<String, HashMap<String, Cbar_content>> currencyContent = new HashMap<>();

    @Override
    boolean readAndCalculateExchangeValue(String amount, String date, String mezenne) {
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
        Scanner scanner = new Scanner(System.in);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String dateCur = formatter.format(date);
        System.out.println(dateCur + " " + "gunluk tarixinin valyutasina baxmaq ucun 1 daxil edin,Tarix daxil etmek isdeyirsizse 2 daxil edin");
        if (scanner.nextLine().equals("2")) {
            String userDate = GeneralUtils.askInputFromUser("Tarix daxil et:");
            boolean isDateValid = GeneralUtils.isDateValid(userDate);
            if (!isDateValid)
                return false;

            if (notExist(userDate)) {
                String input = GeneralUtils.askInputFromUser("Fayl tapilmadi. Yükləyib davam etmək üçün 1, çıxış etmək üçün 2 daxil edin.");
                if (input.equals("1")) {
                    getCurrencyDataWithDateAndSave(userDate);

                } else if (input.equals("2")) {
                    return false;
                }
            }

            String mezenne = GeneralUtils.askInputFromUser("Mezenneni daxil edin:").toUpperCase();
            String amount = GeneralUtils.askInputFromUser("AZN Mebleg daxil edin:");

            if (!GeneralUtils.isAmountValid(amount)) {
                return false;
            }
            readAndCalculateExchangeValue(amount, userDate, mezenne);

        } else {
            if (notExist(dateCur)) {
                System.out.println("Gunluk mezenne melumatlari sistemde olmadigi ucun endirildi!");
                getCurrencyDataWithDateAndSave(dateCur);
            }
            String mezenne = GeneralUtils.askInputFromUser("Mezenneni daxil edin:").toUpperCase();
            String amount = GeneralUtils.askInputFromUser("AZN Mebleg daxil edin:");

            if (!GeneralUtils.isAmountValid(amount)) {
                return false;
            }
            readAndCalculateExchangeValue(amount, dateCur, mezenne);
        }
        return true;
    }


}

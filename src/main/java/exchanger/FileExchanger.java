package exchanger;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.io.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import java.util.*;
import org.dom4j.*;
import util.GeneralUtils;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class FileExchanger extends ExchangerAbstract {

    @Override
    String readAndCalculateExchangeValue(String amount, String date, String mezenne) throws Exception {
        BigDecimal amountCast = new BigDecimal(amount);
        File file = new File(System.getProperty("user.dir") + "/res/" + date + ".xml");
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
//        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
//                .newDocumentBuilder();
//        Document doc = dBuilder.parse(file);
//        doc.getDocumentElement().normalize();
//        NodeList nodes = doc.getElementsByTagName("Valute");

        List<org.dom4j.Node> nodes = document.selectNodes("/Valute" );

        boolean isFound = false;
        BigDecimal result = null;

        for (int i = 0; i < nodes.size(); i++) {
            Node tempNode = (Node) nodes.get(i);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) tempNode;
                if (mezenne.equals(eElement.getAttribute("Code"))) {
                    BigDecimal currency = new BigDecimal(eElement.getElementsByTagName("Value").item(0).getTextContent());
                    result = amountCast.divide(currency, 3, RoundingMode.HALF_UP);
                    System.out.println(result.toString());
                    isFound = true;
                }
            }
        }
        if (!isFound) {
            return "Sehv mezenne daxil etdiniz!";
        }
        return result.toString();
    }

    @Override
    void getCurrencyDataWithDateAndSave(String date) throws Exception {
        URL url = new URL("https://www.cbar.az/currencies/" + date + ".xml");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        con.disconnect();
        File file = new File(System.getProperty("user.dir") + "/res/" + date + ".xml");
        if (!file.exists()) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content.toString());

            writer.close();
            System.out.println("Fayl ugurla endirildi!");
        } else {
            System.out.println("Ugursuz emeliyyat,Movcud oldugu ucun endirilmedi!");
        }
    }

    @Override
    boolean notExist(String date) {
        File file = new File(System.getProperty("user.dir") + "/res/" + date + ".xml");
        return file.exists();
    }



    public boolean enterExchangeInfo() throws Exception {
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

            if (!notExist(userDate)) {
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
            if (!notExist(dateCur)) {
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


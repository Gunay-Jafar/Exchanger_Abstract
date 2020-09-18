package exchanger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
import java.util.Scanner;

public class FileExchanger extends ExchangerAbstract {

    @Override
    boolean readAndCalculateExchangeValue(String amount, String date, String mezenne) throws Exception {
        BigDecimal amountCast = new BigDecimal(amount);
        File file = new File(System.getProperty("user.dir") + "/res/" + date + ".xml");
        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodes = doc.getElementsByTagName("Valute");

        boolean isFound = false;

        for (int i = 0; i < nodes.getLength(); i++) {
            Node tempNode = nodes.item(i);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) tempNode;
                if (mezenne.equals(eElement.getAttribute("Code"))) {
                    BigDecimal currency = new BigDecimal(eElement.getElementsByTagName("Value").item(0).getTextContent());
                    BigDecimal result = amountCast.divide(currency, 3, RoundingMode.HALF_UP);
                    System.out.println(result.toString());
                    isFound = true;
                }
            }
        }
        if (!isFound) {
            System.out.println("Sehv mezenne daxil etdiniz!");
        }
        return isFound;
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
            System.out.println("Ugursuz emeliyyat,Fayl endirilmedi!");
        }
    }

    @Override
    boolean notExist(String date) {
        File file = new File(System.getProperty("user.dir") + "/res/" + date + ".xml");
        return file.exists();
    }

    public boolean downloadData() throws Exception {
        String userDate = GeneralUtils.askInputFromUser("Tarix daxil et:");
        boolean isDateValid = GeneralUtils.isDateValid(userDate);
        if (!isDateValid)
            return false;

        getCurrencyDataWithDateAndSave(userDate);
        return true;
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


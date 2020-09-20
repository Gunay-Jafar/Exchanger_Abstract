package exchanger;

import entity.Cbar_content;
import entity.Cbar_date;
import org.hibernate.Session;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import util.GeneralUtils;
import util.HibernateUtil;

import javax.persistence.NoResultException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class DBExchanger extends ExchangerAbstract {
    private Session ses;

    public DBExchanger() {
        ses = HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    boolean readAndCalculateExchangeValue(String amount, String date, String mezenne) {
        BigDecimal amountCast = new BigDecimal(amount);
        String hql = "select cc.value from Cbar_content cc where cc.cbar_date.date=:date and cc.code=:code";
        Object result = ses.createQuery(hql)
                .setParameter("date", LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .setParameter("code", mezenne)
                .getSingleResult();
        if (String.valueOf(result).isEmpty() || String.valueOf(result) == null)
            return false;

        BigDecimal currency = new BigDecimal(String.valueOf(result));
        BigDecimal resultNum = amountCast.divide(currency, 3, RoundingMode.HALF_UP);
        System.out.println(resultNum.toString());

        return true;
    }


    @Override
    void getCurrencyDataWithDateAndSave(String date) {
        try {
            ses.beginTransaction();

            Cbar_date cd = new Cbar_date();
            cd.setDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            ses.save(cd);

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
                    ses.save(cc);
                    System.out.println(cc.getCode() + "yaddasa verildi!");
                }
            }
            ses.getTransaction().commit();
            System.out.println(date + " ugurla bazaya yazildi!");

        } catch (Exception ex) {
            ex.printStackTrace();
            ses.getTransaction().rollback();
            System.out.println("xeta bas verdi");
        }
    }

    @Override
    boolean notExist(String date) {
        try {
            LocalDate ld = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            Object res = ses.createQuery("select true from Cbar_date where date=:date")
                    .setParameter("date", ld).getSingleResult();
            boolean result = res == null || res.toString().equals("false");
            return result;
        } catch (NoResultException ex) {
            System.out.println(ex.getMessage());
            return true;
        }
    }



    @Override
    boolean enterExchangeInfo() {
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
                getCurrencyDataWithDateAndSave(userDate);

            } else {
                System.out.println(userDate + " məlumatları mövcuddur");
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
                System.out.println("Yanlis mebleg daxil etdiniz!");
                return false;
            }
            readAndCalculateExchangeValue(amount, dateCur, mezenne);
        }
        return true;
    }
}

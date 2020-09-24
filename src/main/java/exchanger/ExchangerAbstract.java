package exchanger;

import util.GeneralUtils;

import java.util.Scanner;

public abstract class ExchangerAbstract {

    abstract String readAndCalculateExchangeValue(String amount, String date, String mezenne) throws Exception;

    abstract void getCurrencyDataWithDateAndSave(String date) throws Exception;

    abstract boolean notExist(String date);

    boolean downloadData() throws Exception {
        String userDate = GeneralUtils.askInputFromUser("Tarix daxil et:");
        boolean isDateValid = GeneralUtils.isDateValid(userDate);
        if (!isDateValid)
            return false;

        if (notExist(userDate)) {
            getCurrencyDataWithDateAndSave(userDate);
            return false;
        } else {
            System.out.println(userDate + " məlumatları mövcuddur");
        }
        return true;
    }

    public boolean downloadDataForService(String date) throws Exception {
        boolean isDateValid = GeneralUtils.isDateValid(date);
        if (!isDateValid)
            return false;

        if (notExist(date)) {
            getCurrencyDataWithDateAndSave(date);
            return false;
        } else {
            System.out.println(date + " məlumatları mövcuddur");
        }
        return true;
    }

    abstract boolean enterExchangeInfo() throws Exception;

    public String enterExchangeInfoForService(String date, String code, String amount) throws Exception {
        boolean isDateValid = GeneralUtils.isDateValid(date);
        if (!isDateValid)
            return "Sehv tarix daxil etdiniz!";

        return readAndCalculateExchangeValue(amount,date,code.toUpperCase());

    }
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                String input = GeneralUtils.askInputFromUser("Devam etmek ucun enter daxil et. Cixmaq ucun -1 daxil et");
                if (input.equals("-1"))
                    return;

                System.out.println("****MENU****");
                System.out.println("1.Fayli Endir");
                System.out.println("2.Mezennenin daxil edilmesi");
                String userInput = scanner.nextLine();
                boolean successfullyDone;
                switch (userInput) {
                    case "1":
                        successfullyDone = downloadData();
                        if (!successfullyDone)
                            continue;
                        break;
                    case "2":
                        successfullyDone = enterExchangeInfo();
                        if (!successfullyDone)
                            continue;
                        break;
                    default:
                        System.out.println("Yanlis daxil etdiniz");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

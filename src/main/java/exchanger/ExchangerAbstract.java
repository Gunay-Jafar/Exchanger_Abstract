package exchanger;

import util.GeneralUtils;

import java.util.Scanner;

public abstract class ExchangerAbstract {

    abstract boolean readAndCalculateExchangeValue(String amount, String date, String mezenne) throws Exception;

    abstract void getCurrencyDataWithDateAndSave(String date) throws Exception;

    abstract boolean notExist(String date);

    abstract boolean downloadData() throws Exception;

    abstract boolean enterExchangeInfo() throws Exception;

    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                if (GeneralUtils.askInputFromUser("Devam etmek ucun enter daxil et. Cixmaq ucun -1 daxil et").equals("-1"))
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
                        System.out.println("yanlis daxil etdiniz");
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}

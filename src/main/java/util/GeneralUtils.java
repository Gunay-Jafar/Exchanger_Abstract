package util;

import java.util.Scanner;

public class GeneralUtils {

    public static boolean isDateValid(String userDate) {
        if (!userDate.matches("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$")) {
            System.out.println("Yanlis vaxt daxil etdiniz!");
            return false;
        }
        return true;
    }

    public static boolean isAmountValid(String amount) {
        if (!amount.matches("^(0|[1-9]\\d*)(\\.\\d+)?$")) {
            System.out.println("Yanlis mebleg daxil etdiniz!");
            return false;
        }
        return true;
    }

    public static String askInputFromUser(String message) {
        Scanner br = new Scanner(System.in);
        System.out.println(message);
        return br.nextLine();
    }
}

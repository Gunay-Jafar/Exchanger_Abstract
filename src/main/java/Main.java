import exchanger.DBExchanger;
import exchanger.FileExchanger;
import exchanger.RunTimeExchanger;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1-File ,2-DataBase ,3-RunTime");
        switch (scanner.nextLine()) {
            case "1":
                FileExchanger fileExchanger = new FileExchanger();
                fileExchanger.run();
                break;
            case "2":
                DBExchanger dbExchanger = new DBExchanger();
                dbExchanger.run();
                break;
            case "3":
                RunTimeExchanger runTimeExchanger = new RunTimeExchanger();
                runTimeExchanger.run();
                break;
            default:
                System.out.println("Sehv daxil etdiniz!");
        }

    }
}

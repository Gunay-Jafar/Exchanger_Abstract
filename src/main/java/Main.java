import exchanger.DBExchanger;
import exchanger.ExchangerAbstract;
import exchanger.FileExchanger;
import exchanger.RunTimeExchanger;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1-File ,2-DataBase ,3-RunTime");
        switch (scanner.nextLine()) {
            case "1":
                runProgram(new FileExchanger());
                break;
            case "2":
                runProgram(new DBExchanger());
                break;
            case "3":
                runProgram(new RunTimeExchanger());
                break;
            default:
                System.out.println("Sehv daxil etdiniz!");
        }

    }

    public static void runProgram(ExchangerAbstract exchanger){
        exchanger.run();
    }
}

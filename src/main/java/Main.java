import exchanger.DBExchanger;
import exchanger.FileExchanger;

public class Main {
    public static void main(String[] args) {
//        FileExchanger fileExchanger=new FileExchanger();
//        fileExchanger.run();
        DBExchanger dbExchanger=new DBExchanger();
        dbExchanger.run();

    }
}
package service;

public class CurrentService {

    private static CurrentService ourInstance=new CurrentService();

    public static CurrentService getInstance(){
        return ourInstance;
    }

}

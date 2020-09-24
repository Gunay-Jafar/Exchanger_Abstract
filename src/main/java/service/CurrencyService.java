package service;

import exchanger.DBExchanger;
import exchanger.ExchangerAbstract;
import exchanger.FileExchanger;
import exchanger.RunTimeExchanger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/currency")
public class CurrencyService {

    @POST
    @Path("/downloadFile")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String getOrderById(@FormParam("date") String date,
                               @FormParam("type") String type) throws Exception {
        switch (type) {
            case "1":
                runProgram(new FileExchanger(), date);
                break;
            case "2":
                runProgram(new DBExchanger(), date);
                break;
            case "3":
                runProgram(new RunTimeExchanger(), date);
                break;
            default:
                return "Sehv daxil etdiniz!";
        }
        return "Fayl ugurla Endirildi";
    }

    public static void runProgram(ExchangerAbstract exchanger, String date) throws Exception {
        exchanger.downloadDataForService(date);
    }

    @POST
    @Path("/getInfo")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String getInfo(@FormParam("date") String date, @FormParam("type") String type, @FormParam("code") String code, @FormParam("amount") String amount) throws Exception {

        switch (type) {
            case "1":
                return runProgram2(new FileExchanger(), date, code, amount);

            case "2":
                return runProgram2(new DBExchanger(), date, code, amount);

            case "3":
                return runProgram2(new RunTimeExchanger(), date, code, amount);

            default:
                return "Sehv daxil etdiniz!";
        }
    }

    public static String runProgram2(ExchangerAbstract exchanger, String date, String code, String amount) throws Exception {
        return exchanger.enterExchangeInfoForService(date, code, amount);

    }
}




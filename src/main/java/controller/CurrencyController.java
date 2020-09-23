package controller;

import exchanger.DBExchanger;
import exchanger.ExchangerAbstract;
import exchanger.FileExchanger;
import exchanger.RunTimeExchanger;
import service.CurrentService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import java.awt.*;

@Path("/currency")
public class CurrencyController {

    private CurrentService currentService = CurrentService.getInstance();


    @POST
    @Path("/downloadFile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public void getOrderById(@FormParam("date") String date,
                             @FormParam("type") String type) throws Exception {
        switch (type) {
            case "1":
                runProgram(new FileExchanger(),date);
                break;
            case "2":
                runProgram(new DBExchanger(),date);
                break;
            case "3":
                runProgram(new RunTimeExchanger(),date);
                break;
            default:
                System.out.println("Sehv daxil etdiniz!");
        }
    }

    public static void runProgram(ExchangerAbstract exchanger,String date)throws Exception{
        exchanger.downloadDataForService(date);
    }
}

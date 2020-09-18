package exchanger;

public class RunTimeExchanger extends ExchangerAbstract {

    @Override
    boolean readAndCalculateExchangeValue(String amount, String date, String mezenne) {
        return false;
    }

    @Override
    void getCurrencyDataWithDateAndSave(String date) {

    }

    @Override
    boolean notExist(String date) {
        return false;
    }

    @Override
    boolean downloadData() throws Exception {
        return false;
    }

    @Override
    boolean enterExchangeInfo() throws Exception {
        return false;
    }

}

package demo.fantastic;

import demo.api.MyApi;

public class AwesomeApi implements MyApi {
    private final String myValue;

    public AwesomeApi(String value) {
        this.myValue = value;
    }

    @Override
    public String getMyValue() {
        return myValue;
    }
}

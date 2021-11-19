package demo.fantastic;

import com.google.inject.Singleton;
import demo.api.MyApi;

@Singleton
public class AwesomeApi implements MyApi {
    private final String myValue;

    public AwesomeApi() {
        this.myValue = "a value";
    }

    @Override
    public String getMyValue() {
        return myValue;
    }
}

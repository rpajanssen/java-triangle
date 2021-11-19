package demo.application;

import com.google.inject.Inject;
import demo.api.MyApi;

public class MyService {
    private final MyApi myApi;

    @Inject
    public MyService(MyApi myApi) {
        this.myApi = myApi;
    }

    public void work() {
        System.out.println("Value=" + myApi.getMyValue());
    }
}

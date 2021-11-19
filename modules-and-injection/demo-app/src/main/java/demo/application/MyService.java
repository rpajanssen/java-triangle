package demo.application;

import demo.api.MyApi;

public class MyService {
    private final MyApi myApi;

    public MyService(MyApi myApi) {
        this.myApi = myApi;
    }

    public void work() {
        System.out.println("Value=" + myApi.getMyValue());
    }
}

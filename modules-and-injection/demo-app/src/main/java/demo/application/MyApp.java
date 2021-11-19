package demo.application;

import demo.api.MyApi;

import demo.fantastic.AwesomeApi;

public class MyApp {

    public static void main(String... args) {
        MyApi myApi = new AwesomeApi("a value");

        System.out.println("Value=" + myApi.getMyValue());
    }
}

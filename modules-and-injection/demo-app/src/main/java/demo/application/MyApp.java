package demo.application;

import demo.fantastic.guice.AwesomeApiModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class MyApp {

    public static void main(String... args) {
        Injector injector = Guice.createInjector(
                new AwesomeApiModule() // introduces a direct dependency on this "implementation"
                                       // opposite to the service locator pattern
        );

        MyService myService = injector.getInstance(MyService.class);

        myService.work();
    }
}

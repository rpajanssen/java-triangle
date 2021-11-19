package demo.application;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.ServiceLoader;

public class MyApp {

    public static void main(String... args) {
        Injector injector = Guice.createInjector(
                ServiceLoader.load(AbstractModule.class) // use the service locator to find the implementation(s)
                                                         // so we now no longer depend on the implementation
        );

        MyService myService = injector.getInstance(MyService.class);

        myService.work();
    }
}

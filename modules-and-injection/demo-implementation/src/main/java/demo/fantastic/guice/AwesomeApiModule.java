package demo.fantastic.guice;

import com.google.inject.AbstractModule;
import demo.api.MyApi;
import demo.fantastic.AwesomeApi;

public class AwesomeApiModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MyApi.class).to(AwesomeApi.class);
    }
}

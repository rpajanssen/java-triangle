module demo.application {
    exports demo.application;

    requires demo.api;

    uses com.google.inject.AbstractModule; // use the Guice module instead of depending on the implementing
                                           // module so we no longer have a tight coupling
    requires com.google.guice;
    opens demo.application;  // opening up required for guice
}

module demo.fantastic {
    exports demo.fantastic;

    requires demo.api;

    requires com.google.guice;
    // use the service locator pattern to declare a service instead of exporting the
    // implementation that requires a tight coupling on this module (and exposed classes)
    provides com.google.inject.AbstractModule with demo.fantastic.guice.AwesomeApiModule;
    opens demo.fantastic; // opening up required for guice
}

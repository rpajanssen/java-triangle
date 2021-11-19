module demo.fantastic {
    exports demo.fantastic;

    requires demo.api;

    requires com.google.guice;
    exports demo.fantastic.guice;   // export the guice module implementation
    opens demo.fantastic;           // opening up required for guice
}

module demo.fantastic {
    exports demo.fantastic;

    requires demo.api;

    requires com.google.guice;
    exports demo.fantastic.guice;
    opens demo.fantastic;
}

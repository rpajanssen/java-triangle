module demo.application {
    exports demo.application;

    requires demo.api;
    requires demo.fantastic;

    requires com.google.guice;
    opens demo.application;
}

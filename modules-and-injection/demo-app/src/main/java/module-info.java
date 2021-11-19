module demo.application {
    exports demo.application;

    requires demo.api;
    requires demo.fantastic; // introduces a direct dependency

    requires com.google.guice;
    opens demo.application;  // opening up required for guice
}

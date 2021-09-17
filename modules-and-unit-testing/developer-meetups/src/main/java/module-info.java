module com.abnamro.developer.meetups {
    exports com.abnamro.developer.meetups.interfaces;
    exports com.abnamro.developer.meetups.content.external;
    exports com.abnamro.developer.meetups.media;
    exports com.abnamro.developer.meetups.schedule;

    requires com.abnamro.developer.feeding;
}

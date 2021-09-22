module com.abnamro.developer.lazy {
    requires com.abnamro.developer.api;
    requires com.abnamro.developer.feeding;
    requires com.abnamro.developer.meetups;

    provides com.abnamro.developer.api.Developer with com.abnamro.developer.lazy.LazyBastard;
}

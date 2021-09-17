module com.abnamro.developer.lazy {
    requires transitive com.abnamro.developer.api;
    requires transitive com.abnamro.developer.feeding;
    requires transitive com.abnamro.developer.meetups;

    provides com.abnamro.developer.api.Developer with com.abnamro.developer.lazy.LazyBastard;
}

package com.abnamro.developer.api;

import java.util.List;

public interface Developer {
    String getFullName();
    String getBlockId();
    List<Deliverable> work();
}

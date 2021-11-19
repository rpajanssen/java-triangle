package com.abnamro.developer.pool;

import com.abnamro.developer.api.Developer;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public final class DeveloperPool {

    private DeveloperPool() {
        throw new UnsupportedOperationException();
    }

    public static List<Developer> getDevelopers(String blockId) {
        ServiceLoader<Developer> loader = ServiceLoader.load(Developer.class);

        return loader.stream()
                .map(provider -> provider.get())
                .filter(developer -> developer.getBlockId().equals(blockId))
                .collect(Collectors.toList());
    }
}

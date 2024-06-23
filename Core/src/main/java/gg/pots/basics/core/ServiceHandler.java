package gg.pots.basics.core;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceHandler {

    @Getter
    private final List<Service> services = new ArrayList<>();

    @Getter
    private static ServiceHandler instance;

    public ServiceHandler(Service... services) {
        Arrays.stream(services).forEach(this::register);
        instance = this;
    }

    public <T extends Service> void register(T service) {
        if (service == null) {
            throw new IllegalArgumentException("Provided service is null @ " + System.currentTimeMillis());
        }

        this.services.add(service);
    }

    public <T extends Service> T find(Class<? extends T> clazz) {
        return clazz.cast(this.services.stream()
                .filter(current -> current.getClass().equals(clazz) || current.getClass().isAssignableFrom(clazz))
                .findFirst().orElse(null));
    }


    public void loadAll() {
        this.services.forEach(Service::load);
    }

    public void unloadAll() {
        this.services.forEach(Service::unload);
    }
}
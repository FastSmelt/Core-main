package gg.pots.basics.core;

public interface Service {

    default void load() {}

    default void unload() {}

    @SuppressWarnings("unchecked")
    default <T extends Service> T register(ServiceHandler serviceHandler) {
        serviceHandler.register(this);
        return (T) this;
    }
}
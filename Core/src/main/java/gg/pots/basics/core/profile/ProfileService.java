package gg.pots.basics.core.profile;

import gg.pots.basics.core.Service;
import gg.pots.basics.core.json.JsonAppender;
import gg.pots.basics.core.saving.SavingService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public class ProfileService implements Service {

    private final Map<UUID, CoreProfile> profileMap = new HashMap<>();

    @Getter
    private final SavingService savingService;

    @Override
    public void load() {
        this.savingService.getSavingType().getJsonObjects("profiles").forEach(CoreProfile::new);
    }

    @Override
    public void unload() {
        this.profileMap.values().forEach(profile -> this.savingService
                .getSavingType()
                .saveJsonObjectAsync(profile.toJson(), "profiles"));

        this.profileMap.clear();
    }

    /**
     * Get a {@link Stream<CoreProfile>} from the current {@link ProfileService} object
     *
     * @return the stream of profiles
     */

    public Stream<CoreProfile> stream() {
        return this.profileMap.values().stream();
    }

    /**
     * Register a new {@link CoreProfile}
     *
     * @param profile the profile to register
     */

    public void registerProfile(CoreProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("The provided profile to register is null");
        }

        this.profileMap.put(profile.getUuid(), profile);
    }

    public void deleteProfile(CoreProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("The provided profile to delete is null");
        }

        this.profileMap.remove(profile.getUuid());
        this.savingService.getSyncType().publish("profile-delete", new JsonAppender().append("uuid", profile.getUuid().toString()).get());

        this.savingService.getSavingType().deleteFromCollection(profile.getUuid(), "profiles");
    }

    /**
     * Save an existing {@link CoreProfile} in a different thread
     *
     * @param profile the profile to save
     */

    public void saveProfileAsync(CoreProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("The provided profile to save is null");
        }

        this.savingService.getSavingType().saveJsonObjectAsync(profile.toJson(), "profiles");
    }

    /**
     * Synchronize an existing {@link CoreProfile} object
     *
     * @param profile the profile to sync
     */

    public void syncProfile(CoreProfile profile) {
        this.savingService.getSyncType().publish("profiles", profile.toJson());
    }

    /**
     * Find a {@link CoreProfile} by a {@link UUID}
     *
     * @param uuid the unique identifier
     * @return the profile or null
     */

    public CoreProfile find(UUID uuid) {
        if (this.profileMap.containsKey(uuid)) {
            return this.profileMap.get(uuid);
        }

        return null;
    }

    /**
     * Find a {@link CoreProfile} by a {@link String}
     *
     * @param name the name of the profile to identify it by
     * @return the profile or null
     */

    public CoreProfile find(String name) {
        for (CoreProfile coreProfile : this.profileMap.values()) {
            if (coreProfile.getName().equalsIgnoreCase(name)) {
                return coreProfile;
            }
        }

        return null;
    }

    /**
     * Find a {@link CoreProfile} by a {@link UUID}
     * <p>
     * If no profile can be found,
     * it will make a new profile using the provided {@link UUID} and {@link String}
     *
     * @param uuid the unique identifier
     * @param name the name to use if the profile returns null
     * @return the profile
     */

    public CoreProfile findOrElseMake(UUID uuid, String name) {
        CoreProfile coreProfile = this.profileMap.get(uuid);

        if (coreProfile == null) {
            coreProfile = new CoreProfile(
                    uuid,
                    name
            );

            coreProfile.setFirstJoin(System.currentTimeMillis());
            this.profileMap.put(uuid, coreProfile);
        } else if (coreProfile.getFirstJoin() == 0) {
            coreProfile.setFirstJoin(System.currentTimeMillis());
        }

        return coreProfile;
    }
}

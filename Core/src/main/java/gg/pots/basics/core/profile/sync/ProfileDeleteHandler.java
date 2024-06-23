package gg.pots.basics.core.profile.sync;

import com.google.gson.JsonObject;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.saving.sync.SyncHandler;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ProfileDeleteHandler implements SyncHandler {

    private final ProfileService profileService;

    @Override
    public void incoming(String channel, JsonObject object) {
        if (channel.equals("profile-delete")) {
            final UUID profileUuid = UUID.fromString(object.get("uuid").getAsString());
            final CoreProfile profile = this.profileService.find(profileUuid);

            if (profile != null) {
                this.profileService.deleteProfile(profile);
            }
        }
    }

    @Override
    public String getChannel() {
        return "profile-delete";
    }
}

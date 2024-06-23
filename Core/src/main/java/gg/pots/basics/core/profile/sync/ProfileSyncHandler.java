package gg.pots.basics.core.profile.sync;

import com.google.gson.JsonObject;
import gg.pots.basics.core.expirable.grant.Grant;
import gg.pots.basics.core.expirable.punishment.Punishment;
import gg.pots.basics.core.json.JsonUtils;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import gg.pots.basics.core.saving.sync.SyncHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ProfileSyncHandler implements SyncHandler {

    private final ProfileService profileService;

    @Override
    public void incoming(String channel, JsonObject object) {
        if (channel.equals("profiles")) {
            final UUID profileUuid = UUID.fromString(object.get("uuid").getAsString());
            final String profileName = object.get("name").getAsString();
            final CoreProfile profile;

            if (this.profileService.find(profileUuid) == null) {
                profile = new CoreProfile(profileUuid, profileName);
            } else {
                profile = this.profileService.find(profileUuid);
            }

            if (object.has("ipAddress")) {
                profile.setIpAddress(object.get("ipAddress").getAsString());
            }

            if (object.has("customColor")) {
                profile.setCustomColor(ChatColor.valueOf(object.get("customColor").getAsString()));
            }

            if (object.has("togglePrivateMessages")) {
                profile.setTogglePrivateMessages(object.get("togglePrivateMessages").getAsBoolean());
            }

            if (object.has("canViewBroadcasts")) {
                profile.setCanViewBroadcasts(object.get("canViewBroadcasts").getAsBoolean());
            }

            if (object.has("gems")) {
                profile.setGems(object.get("gems").getAsInt());
            }

            if (object.has("firstJoin")) {
                profile.setFirstJoin(object.get("firstJoin").getAsLong());
            }

            if (object.has("lastJoin")) {
                profile.setLastJoin(object.get("lastJoin").getAsLong());
            }

            final List<Grant> grants = new ArrayList<>();

            JsonUtils.getMap(JsonUtils.getJsonFromString(object.get("grants").getAsString()))
                    .forEach((string, element) -> grants.add(new Grant(JsonUtils.getJsonFromString(element.getAsString()))));

            grants.stream()
                    .filter(grant -> profile.getGrants().stream().anyMatch(current -> current.getUuid().equals(grant.getUuid())))
                    .forEach(grant -> profile.getGrants().stream().filter(current -> current.getUuid().equals(grant.getUuid())).forEach(current -> current.setActive(grant.isActive())));

            grants.stream()
                    .filter(grant -> profile.getGrants().stream().noneMatch(current -> current.getUuid().equals(grant.getUuid())))
                    .forEach(grant -> profile.getGrants().add(grant));

            if (object.has("punishments")) {
                final List<Punishment> punishments = new ArrayList<>();

                JsonUtils.getMap(JsonUtils.getJsonFromString(object.get("punishments").getAsString()))
                        .forEach((string, element) -> punishments.add(new Punishment(JsonUtils.getJsonFromString(element.getAsString()))));

                punishments.stream()
                        .filter(punishment -> profile.getPunishments().stream().anyMatch(current -> current.getUuid().equals(punishment.getUuid())))
                        .forEach(punishment -> profile.getPunishments().stream().filter(current -> current.getUuid().equals(punishment.getUuid())).forEach(current -> current.setActive(punishment.isActive())));

                punishments.stream()
                        .filter(punishment -> profile.getPunishments().stream().noneMatch(current -> current.getUuid().equals(punishment.getUuid())))
                        .forEach(punishment -> profile.getPunishments().add(punishment));
            }

            if (object.has("ignoredPlayers")) {
                final List<String> ignoredPlayers = new ArrayList<>();

                JsonUtils.getMap(JsonUtils.getJsonFromString(object.get("ignoredPlayers").getAsString()))
                        .forEach((string, element) -> ignoredPlayers.add(element.getAsString()));

                ignoredPlayers.stream()
                        .filter(ignoredPlayer -> profile.getIgnoredPlayers().stream().noneMatch(current -> current.equals(ignoredPlayer)))
                        .forEach(ignoredPlayer -> profile.getIgnoredPlayers().add(ignoredPlayer));

                ignoredPlayers.stream()
                        .filter(ignoredPlayer -> profile.getIgnoredPlayers().stream().anyMatch(current -> current.equals(ignoredPlayer)))
                        .forEach(ignoredPlayer -> profile.getIgnoredPlayers().remove(ignoredPlayer));
            }
        }
    }

    @Override
    public String getChannel() {
        return "profiles";
    }
}
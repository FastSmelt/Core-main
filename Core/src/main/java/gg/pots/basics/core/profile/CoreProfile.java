package gg.pots.basics.core.profile;

import com.google.gson.JsonObject;
import gg.pots.basics.core.expirable.grant.Grant;
import gg.pots.basics.core.expirable.punishment.Punishment;
import gg.pots.basics.core.expirable.punishment.PunishmentType;
import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.tag.Tag;
import gg.pots.basics.core.util.SortableArrayList;
import gg.pots.basics.core.json.JsonAppender;
import gg.pots.basics.core.json.JsonUtils;
import gg.pots.basics.core.rank.Rank;
import gg.pots.basics.core.rank.RankService;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class CoreProfile {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);
    private final RankService rankService = ServiceHandler.getInstance().find(RankService.class);

    private final UUID uuid;
    private String name;
    private String ipAddress;

    private ChatColor customColor;
    private Tag activeTag;

    private boolean togglePrivateMessages = true;
    private boolean canViewBroadcasts = true;

    private int gems = 0;

    private long firstJoin = 0L;
    private long lastJoin = System.currentTimeMillis();

    private final SortableArrayList<Grant> grants = new SortableArrayList<>(Comparator.comparingInt(Grant::getWeight).reversed());
    private final List<String> ignoredPlayers = new ArrayList<>();
    private final List<String> permissions = new ArrayList<>();
    private final List<Punishment> punishments = new ArrayList<>();

    /**
     * Constructor for making a new {@link CoreProfile} object
     *
     * @param uuid the unique identifier of the profile
     * @param name the name assigned to the profile
     */

    public CoreProfile(UUID uuid, String name) {
        final Optional<JsonObject> jsonObject = profileService.getSavingService().getSavingType().getJsonObject(uuid, "profiles");

        this.uuid = uuid;
        this.name = name;
        this.customColor = ChatColor.WHITE;

        if (jsonObject.isPresent()) {
            new CoreProfile(jsonObject.get());
        } else {
            this.profileService.registerProfile(this);
        }
    }

    /**
     * Constructor for loading a {@link CoreProfile} object from a {@link JsonObject}
     *
     * @param object the object
     */

    public CoreProfile(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.name = object.get("name").getAsString();

        if (object.has("customColor")) {
            this.customColor = ChatColor.valueOf(object.get("customColor").getAsString());
        } else {
            this.customColor = ChatColor.WHITE;
        }

        if (object.has("togglePrivateMessages")) {
            this.togglePrivateMessages = object.get("togglePrivateMessages").getAsBoolean();
        }

        if (object.has("canViewBroadcasts")) {
            this.canViewBroadcasts = object.get("canViewBroadcasts").getAsBoolean();
        }

        if (object.has("gems")) {
            this.gems = object.get("gems").getAsInt();
        }

        if (object.has("firstJoin")) {
            this.firstJoin = object.get("firstJoin").getAsLong();
        }

        if (object.has("lastJoin")) {
            this.lastJoin = object.get("lastJoin").getAsLong();
        }

        JsonUtils.getMap(JsonUtils.getJsonFromString(object.get("grants").getAsString()))
                .forEach((string, element) -> this.grants.add(new Grant(JsonUtils.getJsonFromString(element.getAsString()))));

        if (object.has("ignoredPlayers")) {
            JsonUtils.getMap(JsonUtils.getJsonFromString(object.get("ignoredPlayers").getAsString()))
                    .forEach((string, element) -> this.ignoredPlayers.add(element.getAsString()));
        }

        if (object.has("punishments")) {
            JsonUtils.getMap(JsonUtils.getJsonFromString(object.get("punishments").getAsString()))
                    .forEach((string, element) -> this.punishments.add(new Punishment(JsonUtils.getJsonFromString(element.getAsString()))));
        }

        if (object.has("permissions")) {
            JsonUtils.getMap(JsonUtils.getJsonFromString(object.get("permissions").getAsString()))
                    .forEach((string, element) -> this.permissions.add(element.getAsString()));
        }

        if (object.has("ipAddress")) {
            this.ipAddress = object.get("ipAddress").getAsString();
        }

        this.profileService.registerProfile(this);
    }

    /**
     * Serialize a {@link CoreProfile} to a {@link JsonObject}
     *
     * @return the profile to serialize
     */

    public JsonObject toJson() {
        final JsonObject object = new JsonObject();

        if (this.ipAddress != null) {
            object.addProperty("ipAddress", this.ipAddress);
        }

        if (this.customColor != null) {
            object.addProperty("customColor", this.customColor.name());
        }

        return new JsonAppender(object)
                .append("uuid", this.uuid.toString())
                .append("name", this.name)
                .append("togglePrivateMessages", this.togglePrivateMessages)
                .append("canViewBroadcasts", this.canViewBroadcasts)
                .append("gems", this.gems)
                .append("firstJoin", this.firstJoin)
                .append("lastJoin", this.lastJoin)
                .append("grants", JsonUtils.getFromMap(grants.stream().collect(Collectors.toMap(
                        grant -> grant.getUuid().toString(),
                        grant -> grant.toJson().toString())
                )).toString())

                .append("ignoredPlayers", JsonUtils.getFromMap(ignoredPlayers.stream().collect(Collectors.toMap(
                        ignoredPlayer -> ignoredPlayer,
                        ignoredPlayer -> ignoredPlayer
                ))).toString())

                .append("permissions", JsonUtils.getFromMap(this.permissions.stream().collect(Collectors.toMap(
                        permission -> permission,
                        permission -> permission
                ))).toString())

                .append("punishments", JsonUtils.getFromMap(punishments.stream().collect(Collectors.toMap(
                        punishment -> punishment.getUuid().toString(),
                        punishment -> punishment.toJson().toString()
                ))).toString()).get();
    }

    /**
     * Add a default {@link Rank} to the user's grants.
     */

    public void addDefaultRank() {
        final Grant grant = new Grant(
                UUID.fromString("94a0b5e0-4c5a-4b9f-9f9f-4c5a4b9f9f9f"),
                this.rankService.getDefaultRank(),
                "Console",
                "First Join",
                -1L
        );

        this.grants.add(grant);
    }

    /**
     * Get the primary {@link Grant} of the {@link CoreProfile}
     *
     * @return the primary grant or null
     */

    public Grant getPrimaryGrant() {
        return this.grants.stream()
                .filter(Grant::isActive)
                .findFirst().orElse(null);
    }

    /**
     * Get a {@link List} of active {@link Punishment}s with a specific {@link PunishmentType}
     *
     * @param type the type of the punishment
     * @return the list of active punishments
     */

    public List<Punishment> getActivePunishments(PunishmentType type) {
        return this.punishments.stream()
                .filter(punishment -> punishment.getType().equals(type) && punishment.isActive())
                .collect(Collectors.toList());
    }

    /**
     * Check if the {@link CoreProfile} has a specific {@link Rank}
     *
     * @param rank the rank to check
     * @return true if the profile has the rank
     */

    public boolean canGrant(Rank rank) {
        return this.grants.stream()
                .filter(Grant::isActive)
                .anyMatch(grant -> grant.getRank().getWeight() >= rank.getWeight());
    }

    /**
     * Get a {@link Optional} of a {@link Punishment} with a specific {@link PunishmentType}
     *
     * @param type the type of the punishment
     * @return the current active punishment
     */

    public Optional<Punishment> getActivePunishment(PunishmentType type) {
        return this.getActivePunishments(type).stream().findFirst();
    }

    /**
     * Get a {@link List} of all {@link Permission}s of the {@link CoreProfile}
     *
     * @return the list of permissions
     */

    public List<String> getAllPermissions() {
        final List<String> permissions = new ArrayList<>(this.permissions);
        for (Grant grant : this.grants) {
            if (grant.isActive()) {
                permissions.addAll(grant.getRank().getPermissions());
            }
        }
        return permissions;
    }

    /**
     * Get the {@link Player} by a {@link UUID}
     *
     * @return the player.
     */

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    /**
     * Get the {@link Rank} of the {@link CoreProfile}
     *
     * @return the rank.
     */

    public Rank getRank() {
        return this.grants.isEmpty() ? null : this.grants.get(0).getRank();
    }

    /**
     * Get the color of a {@link Rank}
     *
     * @return the name.
     */

    public String getColoredName() {
        return this.getPrimaryGrant().getRank().getColor() + this.name;
    }

    /**
     * Check if the {@link CoreProfile} is banned.
     *
     * @return whether the profile is banned.
     */

    public boolean isBanned() {
        return this.getActivePunishment(PunishmentType.BAN).isPresent();
    }

    /**
     * Check if the profile is complete
     *
     * @return whether the profile is complete
     */

    public boolean isComplete() {
        return this.uuid != null && this.name != null && this.grants.size() >= 1;
    }
}
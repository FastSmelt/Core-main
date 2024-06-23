package gg.pots.basics.core.rank;

import gg.pots.basics.core.saving.SavingService;
import gg.pots.basics.core.util.SortableArrayList;
import gg.pots.basics.core.Service;
import gg.pots.basics.core.json.JsonAppender;
import lombok.Getter;

import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
public class RankService implements Service {

    private final SortableArrayList<Rank> ranks = new SortableArrayList<>(Comparator.comparingInt(Rank::getWeight).reversed());
    private final SavingService savingService;

    /**
     * Constructor for making a new {@link RankService}
     *
     * @param savingService the savingService.
     */

    public RankService(SavingService savingService) {
        this.savingService = savingService;
    }

    @Override
    public void load() {
        this.savingService.getSavingType().getJsonObjects("ranks").forEach(Rank::new);
    }

    @Override
    public void unload() {
        this.stream().forEach(rank -> this.savingService.getSavingType().saveJsonObject(rank.toJson(), "ranks"));
    }

    /**
     * Register a new {@link Rank}
     *
     * @param rank the rank
     */

    public void registerRank(Rank rank) {
        if (rank == null) {
            throw new IllegalArgumentException("The provided rank to create is null");
        }

        this.ranks.add(rank);
    }

    /**
     * Delete an existing {@link Rank} object
     *
     * @param rank the rank to delete
     */

    public void deleteRank(Rank rank) {
        if (rank == null) {
            throw new IllegalArgumentException("The provided rank to delete is null");
        }

        this.ranks.remove(rank);
        this.savingService.getSyncType().publish("rank-delete", new JsonAppender().append("uuid", rank.getUuid().toString()).get());

        this.savingService.getSavingType().deleteFromCollection(rank.getUuid(), "ranks");
    }

    /**
     * Save a {@link Rank} object
     *
     * @param rank the rank to save
     */

    public void saveRank(Rank rank) {
        this.savingService.getSavingType().saveJsonObject(rank.toJson(), "ranks");
    }

    /**
     * Synchronize a {@link Rank} object
     *
     * @param rank the rank to sync
     */

    public void syncRank(Rank rank) {
        this.savingService.getSyncType().publish("ranks", rank.toJson());
    }

    /**
     * Get a {@link Stream} of the current list of {@link Rank}s
     *
     * @return the stream
     */

    public Stream<Rank> stream() {
        return this.ranks.stream();
    }

    /**
     * Sort the list of ranks with a {@link Comparator<Rank>}
     *
     * @param rankComparator the comparator to sort the ranks with
     */

    public void sort(Comparator<Rank> rankComparator) {
        this.ranks.sort(rankComparator);
    }

    /**
     * Get the default {@link Rank}
     *
     * @return the default rank
     */

    public Rank getDefaultRank() {
        return this.ranks.stream()
                .filter(Rank::isDefaultRank)
                .findFirst().orElseGet(() -> new Rank(UUID.randomUUID(), "Default", true));
    }

    /**
     * Find a rank by a name
     *
     * @param name the name
     * @return the found rank or null
     */

    public Rank find(String name) {
        return this.ranks.stream()
                .filter(current -> current.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    /**
     * Find a rank by a {@link UUID}
     *
     * @param uuid the unique identifier
     * @return the found rank or null
     */

    public Rank find(UUID uuid) {
        return this.ranks.stream()
                .filter(current -> current.getUuid().equals(uuid))
                .findFirst().orElse(null);
    }

    /**
     * Find a {@link Rank} by a {@link UUID} and make a new rank if the rank could not be found.
     *
     * @param rankUuid the unique identifier of the rank
     * @param rankName the name of the rank
     * @return the rank
     */

    public Rank findOrMake(UUID rankUuid, String rankName) {
        return this.ranks.stream()
                .filter(current -> current.getUuid().equals(rankUuid))
                .findFirst().orElseGet(() -> new Rank(rankUuid, rankName));
    }
}
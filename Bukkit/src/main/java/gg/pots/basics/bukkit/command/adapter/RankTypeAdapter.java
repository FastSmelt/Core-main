package gg.pots.basics.bukkit.command.adapter;

import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.rank.Rank;
import gg.pots.basics.core.rank.RankService;
import io.github.nosequel.command.adapter.TypeAdapter;
import io.github.nosequel.command.executor.CommandExecutor;

public class RankTypeAdapter implements TypeAdapter<Rank> {

    private final RankService rankService = ServiceHandler.getInstance().find(RankService.class);

    @Override
    public Rank convert(CommandExecutor commandExecutor, String source) {
        return this.rankService.find(source);
    }
}

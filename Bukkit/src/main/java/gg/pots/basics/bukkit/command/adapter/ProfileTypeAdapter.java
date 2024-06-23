package gg.pots.basics.bukkit.command.adapter;

import gg.pots.basics.core.ServiceHandler;
import gg.pots.basics.core.profile.CoreProfile;
import gg.pots.basics.core.profile.ProfileService;
import io.github.nosequel.command.adapter.TypeAdapter;
import io.github.nosequel.command.executor.CommandExecutor;

public class ProfileTypeAdapter implements TypeAdapter<CoreProfile> {

    private final ProfileService profileService = ServiceHandler.getInstance().find(ProfileService.class);

    @Override
    public CoreProfile convert(CommandExecutor commandExecutor, String source) {
        return this.profileService.find(source);
    }
}

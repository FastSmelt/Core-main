package gg.pots.basics.core.saving;

import gg.pots.basics.core.Service;
import lombok.Getter;

@Getter
public class SavingService implements Service {

    private final SavingType savingType;
    private final SyncType syncType;

    public SavingService(SavingType savingType, SyncType syncType) {
        this.savingType = savingType;
        this.syncType = syncType;
    }

    @Override
    public void load() {
        this.savingType.load();
    }

    @Override
    public void unload() {
        this.savingType.unload();
    }
}

package gg.pots.basics.bukkit.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ChatProcedureWeight {

    SEVERE(4),
    HIGH(3),
    NORMAL(2),
    CHAT(1);

    private final int weight;
}

package gg.pots.basics.bukkit.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ChatProcedureResult {

    private final boolean cancelled;
    private final String format;
}

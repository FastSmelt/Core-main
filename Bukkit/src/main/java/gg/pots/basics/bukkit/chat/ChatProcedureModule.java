package gg.pots.basics.bukkit.chat;

import gg.pots.basics.core.Service;
import gg.pots.basics.core.util.SortableArrayList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Comparator;

@RequiredArgsConstructor
public class ChatProcedureModule implements Service {

    @Getter
    private final SortableArrayList<ChatProcedure> procedures = new SortableArrayList<>(Comparator.comparingInt(ChatProcedure::getNumericWeight).reversed());
    private final JavaPlugin plugin;

    /**
     * Constructor for making a new {@link ChatProcedureModule} with an array of {@link ChatProcedure} to register
     *
     * @param plugin     the plugin to register the module to
     * @param procedures the procedures to register
     */
    public ChatProcedureModule(JavaPlugin plugin, ChatProcedure... procedures) {
        this(plugin);
        Arrays.stream(procedures).forEach(this::register);
    }

    @Override
    public void load() {
        Bukkit.getPluginManager().registerEvents(new ChatProcedureListener(this), plugin);
    }

    /**
     * Register a new {@link ChatProcedure}
     *
     * @param procedure the procedure to register
     * @param <T>       the type of the procedure
     */
    public <T extends ChatProcedure> void register(T procedure) {
        this.procedures.add(procedure);
    }

    /**
     * Find a {@link ChatProcedure} by a {@link Class}
     *
     * @param clazz the class to find the procedure by
     * @param <T>   the type of the procedure
     * @return the found procedure or null
     */
    public <T extends ChatProcedure> T find(Class<T> clazz) {
        return clazz.cast(this.procedures.stream()
                .filter(procedure -> procedure.getClass().equals(clazz))
                .findFirst().orElse(null));
    }
}
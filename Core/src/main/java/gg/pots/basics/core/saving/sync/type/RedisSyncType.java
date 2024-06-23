package gg.pots.basics.core.saving.sync.type;

import com.google.gson.JsonObject;
import gg.pots.basics.core.saving.sync.SyncHandler;
import gg.pots.basics.core.json.JsonAppender;
import gg.pots.basics.core.json.JsonUtils;
import gg.pots.basics.core.saving.SyncType;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Getter
public class RedisSyncType implements SyncType {

    private final List<SyncHandler> syncHandlers = new ArrayList<>();
    private final JedisPool pool;

    private boolean authenticate;
    private String username;
    private String password;

    /**
     * Constructor for making a new {@link RedisSyncType} object
     *
     * @param hostname the hostname of the redis server
     * @param port     the port of the redis server
     */
    public RedisSyncType(String hostname, int port) {
        this.pool = new JedisPool(hostname, port);
        this.authenticate = false;
        this.executeCommand(jedis -> jedis.subscribe(this.getPubSub(), "core"));
    }

    /**
     * Constructor for making a new {@link RedisSyncType} object with authentication
     *
     * @param hostname the hostname of the redis server
     * @param port     the port of the redis server
     * @param username the username to authenticate to the redis server with
     * @param password the password to authenticate to the redis server with
     */
    public RedisSyncType(String hostname, int port, String username, String password) {
        this(hostname, port);
        this.authenticate = true;
        this.username = username;
        this.password = password;
    }

    /**
     * Execute a {@link Jedis} command through a {@link Consumer}
     *
     * @param consumer the consumer to execute
     */
    public void executeCommand(Consumer<Jedis> consumer) {
        CompletableFuture.runAsync(() -> {
            final Jedis jedis = pool.getResource();

            if (jedis != null) {
                if (this.authenticate) {
                    jedis.auth(this.password);
                }

                consumer.accept(jedis);
                jedis.close();
            }
        });
    }

    /**
     * Get a new {@link JedisPubSub} handler to handle the synchronization
     *
     * @return the pubsub handler
     */

    private JedisPubSub getPubSub() {
        return new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                RedisSyncType.this.incoming(message);
            }
        };
    }

    @Override
    public void publish(String channel, JsonObject object) {
        this.executeCommand(jedis -> jedis.publish("core", new JsonAppender(object).append("channel", channel).get().toString()));
    }

    @Override
    public void incoming(String message) {
        final JsonObject object = JsonUtils.getParser().parse(message).getAsJsonObject();
        final String channel = object.get("channel").getAsString();

        this.syncHandlers.stream()
                .filter(handler -> handler.getChannel().equals(channel))
                .forEach(handler -> handler.incoming(channel, object));
    }

    @Override
    public void registerHandler(SyncHandler handler) {
        this.syncHandlers.add(handler);
    }
}
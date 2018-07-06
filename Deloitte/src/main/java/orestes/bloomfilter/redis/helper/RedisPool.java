package orestes.bloomfilter.redis.helper;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.Response;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * Encapsulates a Connection Pool and offers convenience methods for safe access
 * through Java 8 Lambdas.
 */
public class RedisPool {
	private static final Logger LOG = LoggerFactory.getLogger(RedisPool.class);
	private final JedisPool pool;
	private final String host;
	private final int port;
	private final int redisConnections;
	private final boolean ssl;
	private List<RedisPool> slavePools;
	private Random random;

	public RedisPool(String host, int port, int redisConnections, String password, boolean ssl) {
		this.host = host;
		this.port = port;
		this.redisConnections = redisConnections;
		this.ssl = ssl;
		this.pool = createJedisPool(host, port, redisConnections, password, ssl);
	}

	public RedisPool(String host, int port, int redisConnections, boolean ssl) {
		this(host, port, redisConnections, (String) null, false);
	}

	public RedisPool(String host, int port, int redisConnections, Set<Entry<String, Integer>> readSlaves, String password, boolean ssl) {
		this(host, port, redisConnections, password, ssl);
		if (readSlaves != null && !readSlaves.isEmpty()) {
			slavePools = new ArrayList<RedisPool>();
			random = new Random();
			for (Entry<String, Integer> slave : readSlaves) {
				slavePools.add(new RedisPool(slave.getKey(), slave.getValue(), redisConnections, ssl));
			}
		}
	}

	public JedisPool getInteralPool() {
		return pool;
	}

	private JedisPool createJedisPool(String host, int port, int redisConnections, String password, boolean ssl) {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setBlockWhenExhausted(true);
		config.setMaxTotal(redisConnections);
		if (password == null) {
			// poolConfig, host, port, timeout, password, database
			return new JedisPool(config, host, port, Protocol.DEFAULT_TIMEOUT * 4);
		} else {
			return new JedisPool(config, host, port, Protocol.DEFAULT_TIMEOUT * 4, password);
		}
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public int getRedisConnections() {
		return redisConnections;
	}

	public boolean getSsl() {
		return ssl;
	}

	public RedisPool allowingSlaves() {
		if (slavePools == null) {
			return this;
		}
		int index = random.nextInt(slavePools.size());
		return slavePools.get(index);
	}

	public Jedis getResource() {
		return pool.getResource();
	}

	public void safelyDo(Consumer<Jedis> f) {
		safelyReturn(jedis -> {
			f.accept(jedis);
			return null;
		});
	}

	public <T> T safelyReturn(Function<Jedis, T> f) {
		try (Jedis jedis = pool.getResource()) {
			return f.apply(jedis);
		}
	}

	public <T> void safeForEach(Collection<T> collection, BiConsumer<Pipeline, T> f) {
		safelyReturn(jedis -> {
			Pipeline p = jedis.pipelined();
			collection.stream().forEach(e -> f.accept(p, e));
			p.sync();
			return null;
		});
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> transactionallyDo(Consumer<Pipeline> f, String... watch) {
		return (List<T>) safelyReturn(jedis -> {
			Pipeline p = jedis.pipelined();
			if (watch.length != 0) {
				p.watch(watch);
			}
			p.multi();
			f.accept(p);
			Response<List<Object>> exec = p.exec();
			p.sync();
			return exec.get();
		});
	}

	public <T> List<T> transactionallyRetry(Consumer<Pipeline> f, String... watch) {
		while (true) {
			List<T> result = transactionallyDo(f, watch);
			if (result != null) {
				return result;
			}
		}
	}

	public Clock getClock() {
		List<String> time = this.safelyReturn(Jedis::time);
		Instant local = Instant.now();
		// Format: [0]: unix ts [1]: microseconds
		Instant redis = Instant.ofEpochSecond(Long.valueOf(time.get(0)), Long.valueOf(time.get(1)) * 1000);
		return Clock.offset(Clock.systemDefaultZone(), Duration.between(local, redis));
	}

	/**
	 * Start an automatically reconnecting Thread with a dedicated connection to
	 * Redis (e.g. for PubSub or blocking pops)
	 *
	 * @param redisHost host
	 * @param redisPort port
	 * @param whenConnected executed when the connections is active
	 * @param abort lambda that allows to abort processing when an error occurs
	 * @return the started thread
	 */
	public static Thread startThread(String redisHost, int redisPort, Consumer<Jedis> whenConnected, Function<Exception, Boolean> abort) {
		Thread thread = new Thread(() -> {
			boolean connected = false;
			while (!connected && !Thread.currentThread().isInterrupted()) {
				try {
					// pubsub has its own Redis connection
					Jedis jedis = new Jedis(redisHost, redisPort);
					jedis.ping();
					connected = true;
					whenConnected.accept(jedis);
				} catch (JedisConnectionException | JedisDataException e) {
					connected = false;
					if (abort.apply(e)) {
						break;
					} else {
						LOG.error("Could not establish connection to Redis server.", e);
						// Rate Limit to 4 reconnects per second
						try {
							Thread.sleep(250);
						} catch (InterruptedException e1) {
							LOG.error("Interrupted {}", e1);
						}
					}
				}
			}
		});
		thread.start();
		return thread;
	}

	public Thread startThread(Consumer<Jedis> whenConnected) {
		return startThread(whenConnected, (ex) -> false);
	}

	public Thread startThread(Consumer<Jedis> whenConnected, Function<Exception, Boolean> abort) {
		return startThread(host, port, whenConnected, abort);
	}

	public void destroy() {
		pool.destroy();
	}
}
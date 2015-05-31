package de.timmeey.libTimmeey.pooling;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.timmeey.libTimmeey.collection.ListHelper;

/**
 * A very simple object pool. This pool will only return a object once, before
 * it is returned back to the pool via the giveBack method
 * 
 * This pool stores metadata about its objects. This data will stay in the pool,
 * even when a borrowed object gets garbage collected because there is no way of
 * knowig whether an object is still in use or discarded. If you don't intend to
 * use an object anymore remove it by calling remove() If this pool is cleanup
 * enabled, it will remove stale metadata by itself after idleTime of the object
 * is expired.
 * 
 * The cleanupTask will run every @cleanUpTime milliseconds, and will remove all
 * objects that are idle (after returning them to the pool) for @idleTimeout
 * seconds
 * 
 * This pool is capable of verifying whether an object is still reusable or not,
 * when the pool is created with an appropriate @Verifier. The verify method of
 * the Verifier is called on every object before it is lend out.
 * 
 * @author timmeey
 *
 * @param <K>
 * @param <V>
 */
public class SimpleObjectPool<K, V> extends TimerTask implements
		ObjectPool<K, V>, Runnable {
	private static Logger logger = LoggerFactory
			.getLogger(SimpleObjectPool.class);
	private final Map<Integer, K> hashToKey = new ConcurrentHashMap<Integer, K>();
	private final Map<K, V> poolObjectList = new ConcurrentHashMap<K, V>();

	// is threadsafe because we needed to implement locking here for iterating
	// over the list in cleanup
	private final Map<K, Long> timeOutMap = new HashMap<K, Long>();

	private final long idleTimeout;
	private final long cleanUpTime;
	private final Verifier<V> verifier;
	private Timer timer;
	private boolean isStopped = false;

	/**
	 * Creates a simpleObjectPool without automated Cleanup or verification
	 */
	public SimpleObjectPool() {
		logger.debug("Creating");
		this.idleTimeout = -1;
		this.cleanUpTime = -1;
		this.verifier = null;

	}

	/**
	 * creates a simpleObjectPool with a cleanUp taks that will run every @cleanUpTime
	 * milliseconds removing all objects that are idle (after storing or
	 * returning them to the pool) for more than @idleTime milliseconds.
	 * 
	 * If the verifier is not null every object will get checked prior to
	 * 
	 * CleanUpTime and idleTimeout must be greater 0 if you want to use the
	 * cleanUp feature lending out
	 * 
	 *
	 * 
	 *
	 * @param idleTimeout
	 *            approximate max idle time of an object before it gets deleted
	 *            from the cache
	 * @param cleanUpTime
	 *            The period in milliseconds for the cleanUpTask to run
	 * @param verifier
	 *            the verifier whch will verify each object prior to lending out
	 */
	public SimpleObjectPool(Long idleTimeout, Long cleanUpTime,
			Verifier<V> verifier) {
		logger.debug(
				"Creating with verifier: {} idleTImeout: {} and cleanUpTime: {}",
				verifier, idleTimeout, cleanUpTime);
		this.idleTimeout = checkNotNull(idleTimeout);
		this.verifier = verifier;
		this.cleanUpTime = checkNotNull(cleanUpTime);
		if (cleanUpTime > 0 && idleTimeout > 0) {
			logger.trace("Enableing cleanUp Thread");
			this.timer = new Timer(true);
			timer.schedule(this, this.cleanUpTime, cleanUpTime);
		}

	}

	@Override
	public void releasePool() {
		logger.debug("Releasing pool");
		this.isStopped = true;
		this.cancel();
		synchronized (timeOutMap) {
			timeOutMap.clear();
		}
		poolObjectList.clear();
		synchronized (hashToKey) {
			hashToKey.clear();
		}

	};

	@Override
	public V borrow(K key) {
		logger.trace("Trying to lend object with key: {}", key);
		synchronized (poolObjectList) {
			return internalBorrow(key);
		}

	}

	@Override
	public V borrow(K key, Callable<V> callable) throws Exception {
		if (isStopped) {
			throw new IllegalStateException("Pool was already released");
		}
		logger.trace("Trying to lend object with key: {}, with callable", key);
		V object = internalBorrow(key);
		if (object == null) {
			logger.trace("Using callable to create object with key: {}", key);
			removeByKey(key);

			object = null;
			object = callable.call();
			// Needed so no one can steal the just inserted object, because
			// borrow with a callable is guaranteed to return exactly the
			// object which is returned by the callable
			synchronized (poolObjectList) {
				this.store(key, object);
				// Thread.sleep(20); //Can be used to force the test to fail
				// when the synchronized blocks are removed
				object = internalBorrow(key);
			}

		}
		return object;
	}

	private V internalBorrow(K key) {
		if (isStopped) {
			throw new IllegalStateException("Pool was already released");
		}

		V object = poolObjectList.get(key);
		if (object != null && verify(object)) {

			poolObjectList.remove(key);
			return object;
		}
		logger.trace(
				"Trying to lend object with key: {}, but no object was found",
				key);
		return null;
	}

	@Override
	public ObjectPool<K, V> store(K key, V objectToPool) {
		if (isStopped) {
			throw new IllegalStateException("Pool was already released");
		}
		logger.trace("Storing new object to key: {}", key);
		int hash = objectToPool.hashCode();
		synchronized (hashToKey) {
			hashToKey.put(hash, key);
		}
		synchronized (timeOutMap) {
			timeOutMap.put(key, System.currentTimeMillis());
		}
		poolObjectList.put(key, objectToPool);
		return this;
	}

	@Override
	public ObjectPool<K, V> giveBack(V borrowedObjeced) {
		if (isStopped) {
			throw new IllegalStateException("Pool was already released");
		}
		putBack(borrowedObjeced, true);
		return this;
	}

	@Override
	public ObjectPool<K, V> giveBackWithoutIdleTimeReset(V borrowedObject) {
		if (isStopped) {
			throw new IllegalStateException("Pool was already released");
		}
		putBack(borrowedObject, false);
		return this;
	}

	@Override
	public ObjectPool<K, V> removeByKey(K key) {
		if (isStopped) {
			throw new IllegalStateException("Pool was already released");
		}
		return internalRemove(key);
	}

	@Override
	public ObjectPool<K, V> removeByObject(V object) {
		if (isStopped) {
			throw new IllegalStateException("Pool was already released");
		}
		K key = hashToKey.get(object.hashCode());
		return removeByKey(key);

	}

	private boolean verify(V object) {
		if (verifier != null) {
			return verifier.verify(object);
		}
		return true;

	}

	private void resetIdleTime(K key) {
		logger.trace("Resetting idletime for object: {}", key);
		synchronized (timeOutMap) {
			timeOutMap.put(key, System.currentTimeMillis());
		}

	}

	private void putBack(V object, boolean resetTime) {

		K key = hashToKey.get(object.hashCode());
		logger.trace(
				"Object is returned back.The old key is: {}. idleTime Should be resettet: {}",
				key, resetTime);
		if (key == null) {
			logger.debug("Could not find any old key for the returned object, discarding it");
			return;
		}
		poolObjectList.put(key, object);
		if (resetTime) {
			resetIdleTime(key);
		}

	}

	@Override
	public void run() {
		logger.trace("Timed cleanup was triggered");
		this.cleanUp();

	}

	private void cleanUp() {
		logger.trace("Cleaning up");
		long now = System.currentTimeMillis();
		List<K> keysToRemove = new ArrayList<K>();
		synchronized (timeOutMap) {
			for (Entry<K, Long> entry : timeOutMap.entrySet()) {

				if (entry.getValue() + idleTimeout < now) {
					keysToRemove.add(entry.getKey());
				}
			}
		}
		logger.trace("Found {} keys to remove", keysToRemove.size());
		internalRemove(keysToRemove);

	}

	private ObjectPool<K, V> internalRemove(K key) {

		internalRemove(ListHelper.initListWithValue(key));

		return this;

	}

	private ObjectPool<K, V> internalRemove(List<K> keys) {
		logger.trace("Removing {} keys", keys.size());
		for (K k : keys) {
			if (k == null) {
				break;
			}
			V object = poolObjectList.get(k);
			if (object instanceof Closeable) {
				try {
					logger.trace(
							"Object {} is of type Closable, trying to close it prior to removal",
							k);
					((Closeable) object).close();
				} catch (IOException e) {
					logger.info(
							"Got exception {} during autoclosing of object: {}",
							e, k);
				}
			}
			Integer hashToKeyKey = null;
			synchronized (hashToKey) {

				int kHash = k.hashCode();
				for (Entry<Integer, K> tmpValue : hashToKey.entrySet()) {
					if (tmpValue.getValue().hashCode() == kHash) {
						hashToKeyKey = tmpValue.getKey();
						break;
					}
				}
			}
			if (hashToKeyKey != null) {
				hashToKey.remove(hashToKeyKey);
			}
			synchronized (timeOutMap) {
				timeOutMap.remove(k);
			}
			poolObjectList.remove(k);
		}

		return this;

	}
}

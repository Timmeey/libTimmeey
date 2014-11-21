package timmeeyLib.pooling;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import timmeeyLib.collection.ListHelper;
import timmeeyLib.pooling.ObjectPool;
import timmeeyLib.pooling.Verifier;

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

	private final Map<Integer, K> hashToKey = new ConcurrentHashMap<Integer, K>();
	private final Map<K, V> poolObjectList = new ConcurrentHashMap<K, V>();

	// is threadsafe because we needed to implement locking here for iterating
	// over the list in cleanup
	private final Map<K, Long> timeOutMap = new HashMap<K, Long>();

	private final long idleTimeout;
	private final long cleanUpTime;
	private final Verifier<V> verifier;
	private final Object modificationLock = null;
	private Timer timer;
	private boolean isStopped = false;

	/**
	 * Creates a simpleObjectPool without automated Cleanup or verification
	 */
	public SimpleObjectPool() {
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
		this.idleTimeout = idleTimeout;
		this.verifier = verifier;
		this.cleanUpTime = cleanUpTime;
		if (cleanUpTime > 0 && idleTimeout > 0) {
			this.timer = new Timer(true);
			timer.schedule(this, cleanUpTime, cleanUpTime);
		}

	}

	@Override
	public void releasePool() {
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
		if (isStopped) {
			throw new IllegalStateException("Pool was already released");
		}
		V object = poolObjectList.get(key);
		if (object != null && verify(object)) {
			poolObjectList.remove(key);
			return object;
		}
		return null;
	}

	@Override
	public ObjectPool<K, V> store(K key, V objectToPool) {
		if (isStopped) {
			throw new IllegalStateException("Pool was already released");
		}
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
		synchronized (timeOutMap) {
			timeOutMap.put(key, System.currentTimeMillis());
		}

	}

	private void putBack(V object, boolean resetTime) {
		K key = hashToKey.get(object.hashCode());
		if (key == null) {
			return;
		}
		poolObjectList.put(key, object);
		if (resetTime) {
			resetIdleTime(key);
		}

	}

	@Override
	public void run() {
		this.cleanUp();

	}

	private void cleanUp() {
		long now = System.currentTimeMillis();
		List<K> keysToRemove = new ArrayList<K>();
		synchronized (timeOutMap) {
			for (Entry<K, Long> entry : timeOutMap.entrySet()) {

				if (entry.getValue() + idleTimeout < now) {
					keysToRemove.add(entry.getKey());
				}
			}
		}
		internalRemove(keysToRemove);

	}

	private ObjectPool<K, V> internalRemove(K key) {

		internalRemove(ListHelper.initListWithValue(key));

		return this;

	}

	private ObjectPool<K, V> internalRemove(List<K> keys) {
		for (K k : keys) {
			V object = poolObjectList.get(k);
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

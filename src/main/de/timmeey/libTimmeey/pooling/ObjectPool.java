package de.timmeey.libTimmeey.pooling;

import java.util.concurrent.Callable;

/**
 * Simple object pool. Objects can be stored here to be reused later.
 * 
 * @author timmeey
 *
 * @param <K>
 *            Key type (Strings, Integer etc.)
 * @param <V>
 *            pooledOjects Type (Socket, Streams files etc.)
 */
public interface ObjectPool<K, V> {

	/**
	 * Borrows a object from the pool.
	 * 
	 * @param key
	 *            the key under which the object should be stored
	 * @return
	 */
	public V borrow(K key);

	/**
	 * Borrows a object from the pool. The callable is used if the wanted object
	 * is currently not active in the pool (either lent out or non existent)
	 * 
	 * @param key
	 *            the key under which the object should be stored
	 * @param callable
	 *            a callable which will return the wanted object.
	 * @return
	 */
	public V borrow(K key, Callable<V> callable);

	/**
	 * Stores an object into the pool
	 * 
	 * @param key
	 *            the key
	 * @param objectToPool
	 *            the object to store
	 * @return
	 */
	public ObjectPool store(K key, V objectToPool);

	/**
	 * Gives a borrowed Object back to the pool. This is important when the
	 * underlying pool lends just one instance of an object
	 * 
	 * @param borrowedObjeced
	 *            the earlier borrowed Object
	 * @return
	 */
	public ObjectPool giveBack(V borrowedObjeced);

	/**
	 * If this pool supports idleTime cleanup, this method will return the
	 * borrowed object to the pool, but will not reset its idletim
	 * 
	 * @param borrowedObject
	 *            the borrowed Object
	 * @return
	 */
	public ObjectPool giveBackWithoutIdleTimeReset(V borrowedObject);

	/**
	 * Removes an object permanently from the pool
	 * 
	 * @param key
	 *            the key of the object you want to remove
	 * @return
	 */
	public ObjectPool removeByKey(K key);

	/**
	 * Removes an Object permanently from the pool
	 * 
	 * @param object
	 *            the object to remove
	 * @return
	 */
	public ObjectPool removeByObject(V object);

	/**
	 * Cleans up the pool, stopping all maintenance threads and closing the pool
	 * Several calls to this method won't have any effect. But calls to other
	 * methods will result in an IllegalStateException after the pool is
	 * released
	 */
	public void releasePool();

}

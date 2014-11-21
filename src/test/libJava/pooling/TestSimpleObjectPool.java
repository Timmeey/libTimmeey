package libJava.pooling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;

import timmeeyLib.math.RandomHelper;
import timmeeyLib.pooling.ObjectPool;
import timmeeyLib.pooling.SimpleObjectPool;
import timmeeyLib.pooling.Verifier;

public class TestSimpleObjectPool {

	@Test
	public void testInsertion() {
		ObjectPool<String, Object> pool = new SimpleObjectPool<String, Object>();

		Object object1 = new Object();
		String key1 = "object1";
		pool.store(key1, object1);

		assertEquals(object1, pool.borrow(key1));
		assertNull(pool.borrow(key1));
		pool.giveBack(object1);
		assertEquals(object1, pool.borrow(key1));

	}

	@Test
	public void testRemove() {
		ObjectPool<String, Object> pool = new SimpleObjectPool<String, Object>();

		Object object1 = new Object();
		String key1 = "object1";
		pool.store(key1, object1);
		pool.removeByKey(key1);
		assertNull(pool.borrow(key1));

		pool.store(key1, object1);
		pool.removeByObject(object1);
		assertNull(pool.borrow(key1));

	}

	@Test
	public void testInsertionMultiple() {
		ObjectPool<String, Object> pool = new SimpleObjectPool<String, Object>();

		Object object1 = new Object();
		String key1 = "object1";
		Object object2 = new Object();
		String key2 = "object2";
		pool.store(key1, object1);
		pool.store(key2, object2);
		assertEquals(object1, pool.borrow(key1));
		assertEquals(object2, pool.borrow(key2));
		assertNull(pool.borrow(key1));
		assertNull(pool.borrow(key2));
		pool.giveBack(object1);
		pool.giveBack(object2);
		assertEquals(object1, pool.borrow(key1));
		assertEquals(object2, pool.borrow(key2));
		assertNotEquals(object2, pool.borrow(key1));
		assertNotEquals(object1, pool.borrow(key2));

	}

	@Test
	public void testCleanup() throws InterruptedException {
		ObjectPool<String, Object> pool = new SimpleObjectPool<String, Object>(
				5L, 10L, null);

		Object object1 = new Object();
		String key1 = "object1";
		pool.store(key1, object1);
		assertEquals(object1, pool.borrow(key1));

		Thread.sleep(40L);
		assertNull(pool.borrow(key1));

		pool.giveBack(object1);
		assertNull(pool.borrow(key1));
		pool.store(key1, object1);
		for (int i = 0; i < 100; i++) {
			assertEquals(object1, pool.borrow(key1));
			pool.giveBack(object1);
			Thread.sleep(3);
		}
		assertEquals(object1, pool.borrow(key1));
		pool.giveBack(object1);
		Thread.sleep(50);
		assertNull(pool.borrow(key1));

		pool.store(key1, object1);
		for (int i = 0; i < 100; i++) {
			pool.borrow(key1);
			pool.giveBackWithoutIdleTimeReset(object1);
			Thread.sleep(3);
		}
		assertNull(pool.borrow(key1));

	}

	@Test
	public void testVerifierRemoves() {
		ObjectPool<String, Object> pool = new SimpleObjectPool<String, Object>(
				5L, 10L, new Verifier<Object>() {

					@Override
					public boolean verify(Object object) {
						return false;
					}
				});

		Object object1 = new Object();
		String key1 = "object1";
		pool.store(key1, object1);
		assertNull(pool.borrow(key1));

	}

	@Test
	public void testVerifierPasses() {
		ObjectPool<String, Object> pool = new SimpleObjectPool<String, Object>(
				5L, 10L, new Verifier<Object>() {

					@Override
					public boolean verify(Object object) {
						return true;
					}
				});

		Object object1 = new Object();
		String key1 = "object1";
		pool.store(key1, object1);
		assertEquals(object1, pool.borrow(key1));

	}

	@Test
	public void testThreadSafe() throws InterruptedException {
		final ObjectPool<Integer, Object> pool = new SimpleObjectPool<Integer, Object>(
				5L, 10L, null);

		final HashMap<Integer, Object> map = new HashMap<Integer, Object>();
		map.put(1, new Object());
		map.put(2, new Object());
		map.put(3, new Object());
		map.put(4, new Object());
		map.put(5, new Object());
		map.put(6, new Object());
		map.put(7, new Object());

		final List<Thread> runs = new ArrayList<Thread>();

		// for (int i = 0; i < 5; i++) {
		// runs.add(new Thread() {
		//
		// @Override
		// public void run() {
		// int counter0 = 0;
		//
		// int ranO;
		// int ranF;
		// Object obj;
		// for (long j = 0; j < 80000; j++) {
		//
		// ranO = RandomHelper.getRandom(1, 7);
		// ranF = RandomHelper.getRandom(0, 4);
		// obj = map.get(ranO);
		// switch (ranF) {
		// case 0:
		// pool.store(ranO, obj);
		// counter0++;
		// break;
		// case 1:
		// pool.borrow(ranO);
		// break;
		// case 2:
		// pool.giveBack(obj);
		// break;
		// case 3:
		// pool.removeByKey(ranO);
		// break;
		// case 4:
		// pool.removeByObject(obj);
		// break;
		// }
		//
		// }
		//
		// }
		// });
		// }
		//
		// for (Thread runnable : runs) {
		// runnable.start();
		// }
		for (Thread thread : runs) {
			thread.join();
		}

	}
}

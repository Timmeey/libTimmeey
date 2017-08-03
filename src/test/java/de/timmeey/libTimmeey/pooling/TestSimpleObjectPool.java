package de.timmeey.libTimmeey.pooling;

import de.timmeey.libTimmeey.random.RandomNumber;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import net.jodah.concurrentunit.Waiter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

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
        ObjectPool<String, Object> pool = new SimpleObjectPool<String, Object>(5L, 10L, null);

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
        ObjectPool<String, Object> pool = new SimpleObjectPool<String, Object>(5L, 10L, new Verifier<Object>() {

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
        ObjectPool<String, Object> pool = new SimpleObjectPool<String, Object>(5L, 10L, new Verifier<Object>() {

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
    public void testCallableBorrow() throws Exception {
        ObjectPool<String, Object> pool = new SimpleObjectPool<String, Object>();
        String key = "key1";
        Object obj = new Object();
        Callable<Object> call = new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                return obj;
            }
        };

        assertEquals(obj, pool.borrow(key, call));
        assertNull(pool.borrow(key));
    }

    @Test
    public void testCallableObjectSteal() throws Exception {
        ObjectPool<String, Object> pool = new SimpleObjectPool<String, Object>();
        String key = "key1";
        Object obj = new Object();
        Thread run = new Thread() {
            // This runnable tries to steal the object from the pool that is
            // inserted by the borrow call with a callable

            // This trhead should not be allowed to borrow this object ever,
            // because the borrow(key,callable) method is guaranteed to return
            // the object returned by the callable

            @Override
            public void run() {
                while (true) {
                    pool.borrow(key);
                }

            }
        };
        run.setDaemon(true);
        run.start();
        Thread.sleep(50);

        Callable<Object> call = new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                return obj;
            }
        };

        assertEquals("Object returned by the callable was stolen", obj, pool.borrow(key, call));

    }

    @Test
    public void testThreadSafe() throws Throwable {
        final Waiter waiter = new Waiter();
        final int threads = 5;
        final ObjectPool<Integer, Object> pool = new SimpleObjectPool<Integer, Object>(5L, 10L, null);

        final HashMap<Integer, Object> map = new HashMap<Integer, Object>();
        map.put(1, new Object());
        map.put(2, new Object());
        map.put(3, new Object());
        map.put(4, new Object());
        map.put(5, new Object());
        map.put(6, new Object());
        map.put(7, new Object());

        final List<Thread> runs = new ArrayList<Thread>();

        for (int i = 0; i < threads; i++) {
            runs.add(new Thread() {

                @Override
                public void run() {
                    int counter0 = 0;

                    int ranO;
                    int ranF;
                    Object obj;
                    for (long j = 0; j < 1000000; j++) {

                        ranO = Math.round(new RandomNumber(1, 7).value());
                        ranF = Math.round(new RandomNumber(0, 5).value());
                        obj = map.get(ranO);
                        switch (ranF) {
                            case 0:
                                pool.store(ranO, obj);
                                counter0++;
                                break;
                            case 1:
                                pool.borrow(ranO);
                                break;
                            case 2:
                                pool.giveBack(obj);
                                break;
                            case 3:
                                pool.removeByKey(ranO);
                                break;
                            case 4:
                                pool.removeByObject(obj);
                                break;
                            case 5:
                                try {
                                    assertNotEquals(null, pool.borrow(ranO, new Callable<Object>() {

                                        @Override
                                        public Object call() throws Exception {
                                            return new Object();

                                        }
                                    }));
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                break;
                        }

                    }
                    waiter.resume();

                }

            });

        }
        waiter.expectResumes(threads);

        for (Thread runnable : runs) {
            runnable.start();
        }
        waiter.await();

        // for (Thread thread : runs) {
        // thread.join();
        // }

    }
}

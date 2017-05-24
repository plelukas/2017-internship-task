package pl.codewise.internship;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by luke on 24.05.17.
 */
public class SchedulerTest {

    private Scheduler scheduler;

    @Before
    public void beforeTest(){
        scheduler = new Scheduler();
        new Thread(scheduler).start();
    }

    @Test
    public void testCorrectSchedule(){
        TestRunnable callback = new TestRunnable();
        TestRunnable callback2 = new TestRunnable();
        scheduler.schedule(1, callback);
        scheduler.schedule(2, callback2);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(callback.isSuccess());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(callback2.isSuccess());
    }

    @Test
    public void testNegativeSchedule(){
        TestRunnable callback = new TestRunnable();

        scheduler.schedule(-5, callback);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(callback.isSuccess());
    }

    @Test
    public void testCorrectClosing(){
        TestRunnable callback = new TestRunnable();
        long timerId = scheduler.schedule(5, callback);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        scheduler.stop(timerId);
        assertFalse(callback.isSuccess());
    }

    @Test
    public void testMassiveExecutionPlan(){
        TestRunnable[] callbacks = new TestRunnable[100];
        for(int i=0; i<100; i++)
            callbacks[i] = new TestRunnable();

        for(int i=0; i<100; i++){
            if(i < 50)
                scheduler.schedule(0, callbacks[i]);
            else
                scheduler.schedule(2, callbacks[i]);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i=0; i<100; i++){
            if(i < 50)
                assertTrue(callbacks[i].isSuccess());
            else
                assertFalse(callbacks[i].isSuccess());
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i=0; i<100; i++)
            assertTrue(callbacks[i].isSuccess());

    }

    @Test
    public void testClosingNotExistingTask(){
        assertFalse(scheduler.stop(0));
    }

    @Test
    public void testStoppingAfterCallbackExecuted(){
        TestRunnable callback = new TestRunnable();
        long timerId = scheduler.schedule(0, callback);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(scheduler.stop(timerId));
    }

    @After
    public void afterTest(){
        scheduler.stopScheduler();
    }

    private class TestRunnable implements Runnable{

        private boolean success;

        public TestRunnable(){
            success = false;
        }

        public boolean isSuccess() {
            return success;
        }

        @Override
        public void run() {
            success = true;
        }

    }

}
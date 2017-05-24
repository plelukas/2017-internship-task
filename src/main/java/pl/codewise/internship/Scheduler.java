package pl.codewise.internship;

import java.util.Date;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by luke on 24.05.17.
 */
public class Scheduler implements Runnable{

    /***
     * Scheduling tasks.
     */

    private final AtomicLong atomicLong;
    private final HashMap<Long, Task> callbacksMap;
    private final PriorityQueue<Task> queue;

    // for closing this thread
    private boolean running;

    // resolution
    private final int resolution = 1000;

    // maximum callback at one time
    private final int maxCallbacks = 10;

    public Scheduler(){
        this.atomicLong = new AtomicLong(1);
        this.callbacksMap = new HashMap<>();
        this.queue = new PriorityQueue<>();
    }

    public Scheduler(AtomicLong atomicLong, HashMap<Long, Task> callbacksMap, PriorityQueue<Task> queue) {
        this.atomicLong = atomicLong;
        this.callbacksMap = callbacksMap;
        this.queue = queue;
    }

    /***
     * Schedule task
     * @param expirationTime - time is seconds until callback to execute,
     *                       if negative or 0 - callback will be executed within 1 second
     * @param callback - callback to execute after expirationTime
     * @return timerId - task unique ID or 0 if errors
     */
    public long schedule(int expirationTime, Runnable callback){
        if(callback == null)
            return 0;

        long expirationDate = new Date().getTime() + expirationTime * 1000;
        long timerId = atomicLong.incrementAndGet();

        // add Task
        Task task = new Task(callback, expirationDate, timerId);
        queue.offer(task);
        callbacksMap.put(timerId, task);

        return timerId;
    }

    /***
     * Cancel task execution
     * @param timerId - ID of the task
     * @return status
     */
    public boolean stop(long timerId){
        if(!callbacksMap.containsKey(timerId)){
            return false;
        }

        Task toRemove = callbacksMap.get(timerId);
        callbacksMap.remove(timerId, toRemove);

        synchronized (queue) {
            queue.remove(toRemove);
        }

        return true;
    }

    @Override
    public void run() {
        running = true;
        ExecutorService es = Executors.newFixedThreadPool(maxCallbacks);

        while(running){
            long now = new Date().getTime();

            // get from priority queue as long as the time expired
            while (true){
                Task toExecute;
                synchronized (queue) {
                    if (!(queue.peek() != null && queue.peek().getExpirationDate() < now))
                        break;
                    else
                        toExecute = queue.poll();
                }
                callbacksMap.remove(toExecute.getTimerId(), toExecute);

                es.submit(toExecute.getCallback());
            }

            // sleeping time
            long after = new Date().getTime();
            long passedTime = after - now;
            if(passedTime > resolution / 2) passedTime = resolution / 2;

            try {
                Thread.sleep(resolution / 2 - passedTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * Stops the whole scheduler.
     */
    public void stopScheduler(){
        running = false;
    }
}

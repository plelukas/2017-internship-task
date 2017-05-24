package pl.codewise.internship;

/**
* In an event driven system efficiently keeping track of expiry timers is a common problem.
* This can apply to any system keeping track of outstanding requests or messages with an expiry time.
* <p>
* Scheduler component is composed of two user-facing operations: <code>start</code> and <code>stop</code>.
* <p>
* Start is called by clients specifying a <code>timer duration</code> and a <code>callback</code>.
* In return they get a <code>timerId</code>.
* <p>Stop takes a <code>timerId</code> and finds and stops (removes) the associated timer.
* <p>Additionally <code>Scheduler</code> needs to check for timers that have expired, remove them and invoke a callback
* that was provided by the user during timer registration.
* <p>
* Implement a <code>Scheduler</code> component without using <code>ScheduledExecutorService</code> or <code>Timer/TimerTask</code>.
* <b>Solution should be thread safe</b>.
* <p>For simplicity assume scheduling resolution is <code>1s</code>.
*/
public interface Scheduler {

    TimerId register(int expireInSeconds, Runnable callback);

    void stop(TimerId timerId);

    interface TimerId {

    }
}

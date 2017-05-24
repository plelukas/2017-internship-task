package pl.codewise.internship;

/**
 * Created by luke on 24.05.17.
 */
public class Task implements Comparable<Task>{

    private Runnable callback;
    private Long expirationDate;
    private Long timerId;

    public Task(Runnable callback, Long expirationDate, Long timerId) {
        this.callback = callback;
        this.expirationDate = expirationDate;
        this.timerId = timerId;
    }

    public Runnable getCallback() {
        return callback;
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Long getTimerId() {
        return timerId;
    }

    public void setTimerId(Long timerId) {
        this.timerId = timerId;
    }

    @Override
    public int compareTo(Task task) {
        return expirationDate.compareTo(task.getExpirationDate());
    }
}

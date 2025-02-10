import java.util.List;
import java.util.stream.Stream;

public class Server {
    private static final int DEFAULT_MAX_QUEUE = 200;
    private final int id;
    private final double timeAvailable;
    private final List<Customer> queue;
    private final int maxQueue;
    private final double serviceTime;

    public Server(int id) {
        this.id = id;
        this.timeAvailable = 0.0;
        this.queue = List.of();
        this.maxQueue = 0;
        this.serviceTime = 0.0;
    }

    public Server(int id, double timeAvailable) {
        this.id = id;
        this.timeAvailable = timeAvailable;
        this.queue = List.of();
        this.maxQueue = DEFAULT_MAX_QUEUE;
        this.serviceTime = 0.0;
    }

    public Server(int id, double timeAvailable, List<Customer> queue) {
        this.id = id;
        this.timeAvailable = timeAvailable;
        this.queue = queue;
        this.maxQueue = 0;
        this.serviceTime = 0.0;
    }

    public Server(int id, double timeAvailable, List<Customer> queue, int maxQueue) {
        this.id = id;
        this.timeAvailable = timeAvailable;
        this.queue = queue;
        this.maxQueue = maxQueue;
        this.serviceTime = 0.0;
    }

    private Server(int id, double timeAvailable, 
        List<Customer> queue, int maxQueue, double serviceTime) {
        this.id = id;
        this.timeAvailable = timeAvailable;
        this.queue = queue;
        this.maxQueue = maxQueue;
        this.serviceTime = serviceTime;
    }

    // public int getQueueSize() {
    //     return queue.size();
    // }

    // public int getMaxQueue() {
    //     return maxQueue;
    // }

    // public List<Customer> getQueue() {
    //     return queue;
    // }

    public double getTimeAvailable() {
        return timeAvailable;
    }

    public int getId() {
        return id;
    }

    public boolean canServe(Customer c) {
        return c.getArrivalTime() >= timeAvailable;
    }

    public boolean canWait(Customer c) {
        return queue.size() < maxQueue;
    }

    public String toString() {
        return "server " + id;
    }

    public Server wait(Customer c) {
        List<Customer> newQueue = Stream.concat(
            queue.stream(),
            Stream.of(c)
        ).toList();
        return new Server(id, timeAvailable, newQueue, maxQueue, serviceTime);
    }

    public Server serve(Customer c, double serviceTime) {
        List<Customer> newQueue = Stream.concat(
            queue.stream(),
            Stream.of(c)
        ).toList();
        return new Server(id, c.getArrivalTime() + serviceTime, newQueue, maxQueue, serviceTime);
    }

    public boolean isNextCustomer(Customer c) {
        if (queue.isEmpty()) {
            return false;
        }
        Customer nextCustomer = queue.get(0);
        return nextCustomer.getId() == c.getId() && 
               nextCustomer.getArrivalTime() == c.getArrivalTime();
    }

    public Server finishServing() {
        if (queue.isEmpty()) {
            return this;
        }
        Customer finishedCustomer = queue.get(0);
        double newTimeAvailable = finishedCustomer.getArrivalTime() + this.serviceTime;
        List<Customer> newQueue = queue.stream().skip(1).toList();
        return new Server(id, newTimeAvailable, newQueue, maxQueue, 0.0);
    }

    public Server updateTime(double time) {
        return new Server(id, time, queue, maxQueue, serviceTime);
    }

    public double getServiceTime() {
        return serviceTime;
    }
}
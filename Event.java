abstract class Event implements Comparable<Event> {
    private final double time;
    protected final Customer customer;
    protected final boolean isDoneOrLeaveEvent;
    protected final boolean isLastEvent;

    public Event(double time, Customer customer, 
                boolean isDoneOrLeaveEvent, boolean isLastEvent) {
        this.time = time;
        this.customer = customer;
        this.isDoneOrLeaveEvent = isDoneOrLeaveEvent;
        this.isLastEvent = isLastEvent;
    }

    abstract Pair<Event, Shop> next(Shop shop);

    abstract boolean isArriveEvent();

    abstract boolean isServeEvent();

    abstract boolean isDoneEvent();

    abstract boolean isWaitEvent();
    
    abstract boolean isLeaveEvent();

    public Customer getCustomer() {
        return customer; 
    }

    public boolean isDoneOrLeaveEvent() { 
        return isDoneOrLeaveEvent; 
    }

    public boolean getLastEvent() { 
        return isLastEvent; 
    }

    public int getCustomerID() { 
        return customer.getId();
    }

    public double getArrivalTime() { 
        return customer.getArrivalTime(); 
    }


    public double getTime() {
        return time;
    }

    public int compareTo(Event that) {
        if (this.time < that.time) {
            return -1;
        }
        if (this.time > that.time) {
            return 1;
        }
        if (this.time == that.time) {
            if (this.getArrivalTime() < that.getArrivalTime()) {
                return -1;
            }
            if (this.getArrivalTime() > that.getArrivalTime()) {
                return 1;
            }
            if (this.getCustomerID() < that.getCustomerID()) {
                return -1;
            }
            if (this.getCustomerID() > that.getCustomerID()) {
                return 1;
            }
        }
        return 0;
    }
}
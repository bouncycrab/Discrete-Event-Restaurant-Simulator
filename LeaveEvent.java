public class LeaveEvent extends Event {
    public LeaveEvent(Customer customer, double time) {
        super(time, customer, true, false);
    }

    public LeaveEvent(Customer customer, double time, boolean isLastEvent) {
        super(time, customer, true, isLastEvent);
    }

    @Override
    public boolean isArriveEvent() {
        return false;
    }

    @Override
    public boolean isServeEvent() {
        return false;
    }

    @Override
    public boolean isDoneEvent() {
        return false;
    }

    @Override
    public boolean isWaitEvent() {
        return false;
    }

    @Override
    public boolean isLeaveEvent() {
        return true;
    }

    public boolean getLastEvent() {
        return this.isLastEvent;
    }

    public int getCustomerID() {
        return this.customer.getId();
    }

    public boolean isDoneOrLeaveEvent() {
        return this.isDoneOrLeaveEvent;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    @Override
    public Pair<Event, Shop> next(Shop shop) {
        return new Pair<Event, Shop>(this, shop);
    }
    
    public double getArrivalTime() {
        return this.customer.getArrivalTime();
    }
    
    @Override
    public String toString() {
        return String.format("%.3f", this.getTime()) + " " + customer + " leaves";
    }
    
}

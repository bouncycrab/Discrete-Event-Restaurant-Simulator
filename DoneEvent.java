public class DoneEvent extends Event {
    public DoneEvent(Customer customer, double time) {
        super(time, customer, true, false);
    }

    public DoneEvent(Customer customer, double time, boolean isLastEvent) {
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
        return true;
    }

    @Override
    public boolean isWaitEvent() {
        return false;
    }

    @Override
    public boolean isLeaveEvent() {
        return false;
    }

    public boolean getLastEvent() {
        return this.isLastEvent;
    }

    // public double getArrivalTime() {
    //     return this.customer.getArrivalTime();
    // }

    // public boolean isDoneOrLeaveEvent() {
    //     return this.isDoneOrLeaveEvent;
    // }

    // public Customer getCustomer() {
    //     return this.customer;
    // }

    // public int getCustomerID() {
    //     return this.customer.getId();
    // }

    @Override
    public Pair<Event, Shop> next(Shop shop) {
        Server targetServer = shop.getServers().stream()
            .filter(s -> s.isNextCustomer(customer))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Customer not found in queue"));
            
        return new Pair<Event, Shop>(this, shop.update(targetServer.finishServing()));
    }

    @Override
    public String toString() {
        if (this.isLastEvent) {
            return "";
        }
        return String.format("%.3f", getTime()) + " " + customer + " done";
    }
}
public class ArriveEvent extends Event {
    public ArriveEvent(Customer customer, double time) {
        super(time, customer, false, false);
    }

    @Override
    public boolean isArriveEvent() {
        return true;
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
        return false;
    }

    // public Customer getCustomer() {
    //     return this.customer;
    // }

    // public int getCustomerID() {
    //     return this.customer.getId();
    // }

    // public double getArrivalTime() {
    //     return this.customer.getArrivalTime();
    // }

    @Override
    public Pair<Event, Shop> next(Shop shop) {
        return shop.getServers().stream()
            .filter(server -> server.canServe(customer))
            .findFirst()
            .map(server -> new Pair<Event, Shop>(
                new ServeEvent(customer, server.getId(), getTime()),  
                shop
            ))
            .or(() -> shop.getServers().stream()
                .filter(server -> server.canWait(customer))
                .findFirst()
                .map(server -> {
                    Server updatedServer = server.wait(customer);
                    return new Pair<Event, Shop>(
                        new WaitEvent(customer, server.getId(), getTime()),
                        shop.update(updatedServer)
                    );
                }))
            .orElse(new Pair<Event, Shop>(
                new LeaveEvent(customer, getTime()),
                shop
            ));
    }

    
    @Override
    public String toString() {
        return String.format("%.3f", this.getTime()) + " " + customer.toString() + " arrives";
    }
}
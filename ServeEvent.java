public class ServeEvent extends Event {
    private final int serverId;

    public ServeEvent(Customer customer, int serverId, double time) {
        super(time, customer, false, false);
        this.serverId = serverId;
    }

    @Override
    public boolean isArriveEvent() {
        return false;
    }

    @Override
    public boolean isServeEvent() {
        return true;
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

    @Override
    public Pair<Event, Shop> next(Shop shop) {
        Server targetServer = shop.getServers().stream()
            .filter(s -> s.getId() == serverId)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Server not found"));
            
        double serviceTime = shop.getSupplier().get();
        double doneTime = this.getTime() + serviceTime;
        Server updatedServer = targetServer.isNextCustomer(customer) ?
            targetServer.updateTime(doneTime) :
            targetServer.serve(customer, serviceTime).updateTime(doneTime);

        return new Pair<Event, Shop>(
            new DoneEvent(customer, doneTime),
            shop.update(updatedServer)
        );
    }

    @Override
    public String toString() {
        return String.format("%.3f", getTime()) + " " + customer + 
               " serves by server " + serverId;
    }
}
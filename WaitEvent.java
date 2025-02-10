public class WaitEvent extends Event {
    private final int serverId;  // Only store the ID, not the whole Server object

    public WaitEvent(Customer customer, int serverId, double time) {
        super(time, customer, false, false);
        this.serverId = serverId;
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
        return true;
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
            
        if (targetServer.isNextCustomer(customer)) {
            double serviceTime = targetServer.getServiceTime();
            Server updatedServer = targetServer.updateTime(getTime() + serviceTime);
            return new Pair<Event, Shop>(
                new ServeEvent(customer, serverId, getTime()),
                shop.update(updatedServer)
            );
        }

        return new Pair<Event, Shop>(
            new WaitEvent(customer, serverId, targetServer.getTimeAvailable()),
            shop
        );
    }

    @Override
    public String toString() {
        return String.format("%.3f", this.getTime()) + " " 
            + this.customer + " waits at server " + serverId;
    }
}
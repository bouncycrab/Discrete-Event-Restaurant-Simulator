public class TerminalEvent extends Event {
    public TerminalEvent(double time) {
        super(time, new Customer(1, 0), true, true);
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

    @Override
    public Pair<Event, Shop> next(Shop shop) {
        return new Pair<Event, Shop>(this, shop);
    }

    @Override
    public String toString() {
        return "";
    }
}
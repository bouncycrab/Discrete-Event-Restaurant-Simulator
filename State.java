import java.util.Optional;

public class State {
    private final Shop shop;
    private final PQ<Event> pq;
    private final String stringOutput;
    private final double totalWaitingTime;
    private final int customersServed;
    private final int customersLeft;  
    
    public State(PQ<Event> pq, Shop shop, String s) {
        this.pq = pq;
        this.shop = shop;
        this.stringOutput = s;
        this.totalWaitingTime = 0.0;
        this.customersServed = 0;
        this.customersLeft = 0;
    }

    public State(PQ<Event> pq, Shop shop) {
        this.shop = shop;
        this.stringOutput = "";
        this.pq = pq;
        this.totalWaitingTime = 0.0;
        this.customersServed = 0;
        this.customersLeft = 0;
    }

    private State(PQ<Event> pq, Shop shop, String stringOutput, 
            double totalWaitingTime, int customersServed, int customersLeft) {
        this.pq = pq;
        this.shop = shop;
        this.stringOutput = stringOutput;
        this.totalWaitingTime = totalWaitingTime;
        this.customersServed = customersServed;
        this.customersLeft = customersLeft;
    }

    public boolean isEmpty() {
        return this.getPQ().poll().t().map(e -> false).orElse(true);
    }

    private boolean isLastState() {
        Pair<Optional<Event>, PQ<Event>> newPQ = this.getPQ().poll();
        Event currentEvent = newPQ.t().orElse(
            new DoneEvent(new Customer(0, 0), 0.0, true) 
        );
        return currentEvent.isDoneOrLeaveEvent() 
            && this.getPQ().poll().u().isEmpty() 
            && !currentEvent.getLastEvent();
    }

    private PQ<Event> getPQ() {
        return pq;
    }

    private Shop getShop() {
        return shop;
    }

    public State next() {
        if (!this.isLastState()) {
            Pair<Optional<Event>, PQ<Event>> newPQ = this.getPQ().poll();
            return newPQ.t().map(currentEvent -> {
                Pair<Event, Shop> iteratePair = currentEvent.next(getShop());
                PQ<Event> updatedPQ = newPQ.u();
                
                // Calculate new statistics
                double newWaitingTime = totalWaitingTime;
                int newServed = customersServed;
                int newLeft = customersLeft;
                
                if (currentEvent.isServeEvent()) {
                    newWaitingTime += currentEvent.getTime() 
                        - currentEvent.getCustomer().getArrivalTime();
                    newServed++;
                } else if (currentEvent.isLeaveEvent()) {
                    newLeft++;
                }
                
                if (!currentEvent.isDoneOrLeaveEvent()) {
                    updatedPQ = updatedPQ.add(iteratePair.t());
                }
                
                String newOutput = stringOutput;
                if (currentEvent.isArriveEvent() 
                        || currentEvent.isDoneEvent() 
                        || currentEvent.isServeEvent()
                        || currentEvent.isLeaveEvent()
                        || (currentEvent.isWaitEvent() 
                            && iteratePair.t().isWaitEvent() 
                            && stringOutput.endsWith("arrives\n"))) {
                    newOutput = stringOutput + currentEvent + "\n";
                }
                
                return new State(
                    updatedPQ, 
                    iteratePair.u(), 
                    newOutput,
                    newWaitingTime,
                    newServed,
                    newLeft
                );
            }).orElse(this);
        } else {
            Event currentEvent = this.getPQ().poll().t().orElse(
                new DoneEvent(new Customer(0, 0), 0.0, true)  
            );
            int newLeft = customersLeft;
            if (currentEvent.isLeaveEvent()) {
                newLeft++;
            }
            PQ<Event> updatedPQ = getPQ().poll().u();
            return new State(
                updatedPQ.add(new TerminalEvent(1.0)), 
                this.getShop(), 
                stringOutput + currentEvent + "\n",
                totalWaitingTime,
                customersServed,
                newLeft
            );
        }
    }

    public State getCompleteOutput() {
        String stats = String.format("[%.3f %d %d]", 
            customersServed > 0 ? totalWaitingTime / customersServed : 0.0,
            customersServed,
            customersLeft);
            
        if (stringOutput.isEmpty()) {
            return new State(
                this.getPQ(), 
                this.getShop(), 
                stats
            );
        }
        
        if (stringOutput.endsWith("\n")) {
            return new State(
                this.getPQ(), 
                this.getShop(), 
                stringOutput.substring(0, stringOutput.length() - 1) + "\n" + stats
            );
        }
        return new State(
            this.getPQ(), 
            this.getShop(), 
            stringOutput + "\n" + stats
        );
    }
    
    @Override
    public String toString() {
        if (stringOutput.isEmpty()) {
            return "";
        }
        
        if (stringOutput.endsWith("\n")) {
            return stringOutput.substring(0, stringOutput.length() - 1);
        }
        return stringOutput;
    }
}
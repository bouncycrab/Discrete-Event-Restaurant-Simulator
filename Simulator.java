import java.util.List;
import java.util.stream.Stream;
import java.util.function.Supplier;

class Simulator {
    private final int numOfServers;
    private final int numOfCustomers;
    private final List<Pair<Integer, Double>> arrivals;
    private final int qmax;
    private final Supplier<Double> randomGen;

    Simulator(int numOfServers, int qmax, Supplier<Double> randomGen, 
            int numOfCustomers, List<Pair<Integer, Double>> arrivals) {
        this.numOfServers = numOfServers;
        this.qmax = qmax;
        this.randomGen = randomGen;
        this.numOfCustomers = numOfCustomers;
        this.arrivals = arrivals;
    }

    private PQ<Event> getPQ() {
        PQ<Event> pq = new PQ<Event>();
        for (int i = 0; i < numOfCustomers; i++) {
            Customer customer = new Customer(arrivals.get(i).t(), arrivals.get(i).u());
            pq = pq.add(new ArriveEvent(customer, arrivals.get(i).u()));
        }
        return pq;
    }

    State run() {
        Shop shop = new Shop(numOfServers, randomGen, qmax);
        PQ<Event> pq = getPQ();
        State s =  Stream.iterate(
                new State(pq, shop),
                state -> !state.isEmpty(),
                state -> state.next())
            .reduce(new State(pq, shop), (x, y) -> y);
        return s.getCompleteOutput();
        
    }



}
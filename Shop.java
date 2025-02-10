import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.List;

public class Shop {
    private final int numberOfServers;
    private final List<Server> servers;
    private final Supplier<Double> randomGen;
    private final int maxQueue;

    public Shop(int numberOfServers, Supplier<Double> randomGen, int maxQueue) {
        this.numberOfServers = numberOfServers;
        this.servers = IntStream.rangeClosed(1, numberOfServers)
            .mapToObj(i -> new Server(i, 0.0, List.of(), maxQueue + 1))
            .toList();
        this.randomGen = randomGen;
        this.maxQueue = maxQueue;
    }

    private Shop(int numberOfServers, List<Server> servers, Supplier<Double> randomGen, 
        int maxQueue) {
        this.numberOfServers = numberOfServers;
        this.servers = servers;
        this.randomGen = randomGen;
        this.maxQueue = maxQueue;
    }
    
    public Optional<Server> findServer(Customer c) {
        double serviceTime = randomGen.get();  // Generate service time only once
        return servers.stream()
            .filter(server -> server.canServe(c))
            .map(server -> server.serve(c, serviceTime))
            .findFirst()
            .or(() -> servers.stream()
                .filter(server -> server.canWait(c))
                .map(server -> server.wait(c))
                .findFirst());
    }

    public List<Server> getServers() {
        return servers;
    }

    public Supplier<Double> getSupplier() {
        return randomGen;
    }

    public Shop update(Server s) {
        List<Server> newServers = servers.stream()
            .map(server -> server.getId() == s.getId() ? s : server)
            .toList();
        return new Shop(numberOfServers, newServers, randomGen, maxQueue);
    }

    public String toString() {
        return servers.toString();
    }
}
public class Customer {
    private final int id;
    private final double arrivalTime;

    public Customer(int id, double arrivalTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
    }

    public int getId() {
        return id;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    // private boolean canBeServed(double time) {
    //     return arrivalTime >= time;
    // }

    // public double serveTill(double time) {
    //     return arrivalTime + time;
    // }

    public String toString() {
        return "customer " + id;
    }
}
import java.util.function.Supplier;

public class DefaultServiceTime implements Supplier<Double> {
    
    private double generateServiceTime() {
        return 1.0;
    }

    public Double get() {
        return generateServiceTime();
    }
}
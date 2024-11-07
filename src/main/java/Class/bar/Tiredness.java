package Class.bar;

public class Tiredness extends bar {
    public Tiredness(String name, int maxCapacity, int currentCapacity) {
        super(name, maxCapacity, currentCapacity, "blue");
    }

    public void sleep(float amount) {
        if (currentCapacity + amount > maxCapacity) {
            currentCapacity = maxCapacity;
        } else {
            currentCapacity += amount;
        }
    }

    public void decreaseSleep(float amount) {
        if (currentCapacity - amount < 0f) {
            currentCapacity = 0f;
        } else {
            currentCapacity -= amount;
        }
    }
}

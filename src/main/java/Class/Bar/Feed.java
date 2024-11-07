package Class.Bar;

public class Feed extends Bar {
    public Feed(String name, int maxCapacity, int currentCapacity) {
        super(name, maxCapacity, currentCapacity, "brown");
    }

    public void feedMe(float amount) {
        if (currentCapacity + amount > maxCapacity) {
            currentCapacity = maxCapacity;
        } else {
            currentCapacity += amount;
        }
    }

    public void decreaseFeed(float amount) {
        if (currentCapacity - amount < 0f) {
            currentCapacity = 0f;
        } else {
            currentCapacity -= amount;
        }
    }
}

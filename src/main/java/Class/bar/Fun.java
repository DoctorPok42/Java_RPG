package Class.bar;

public class Fun extends bar {
    public Fun(String name, int maxCapacity, int currentCapacity) {
        super(name);
    }

    public void play(float amount) {
        if (currentCapacity + amount > maxCapacity) {
            currentCapacity = maxCapacity;
        } else {
            currentCapacity += amount;
        }
    }

    public void decreasePlay(float amount) {
        if (currentCapacity - amount < 0f) {
            currentCapacity = 0f;
        } else {
            currentCapacity -= amount;
        }
    }
}

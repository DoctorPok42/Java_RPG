package Class.bar;

import javafx.scene.image.Image;

public class Tiredness extends bar {
    public Tiredness(String name, int currentCapacity) {
        super(name, 60, currentCapacity, "blue");
        this.texture = new Image("file:assets/items/canap.png");
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

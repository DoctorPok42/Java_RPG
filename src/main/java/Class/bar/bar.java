package Class.bar;

import javafx.scene.image.Image;
import java.util.Objects;

public class bar {
    protected String name;
    protected float maxCapacity;
    protected float currentCapacity;
    protected String color;
    protected Image texture;

    public bar(String name, int maxCapacity, int currentCapacity, String color) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public float getMaxCapacity() {
        return maxCapacity;
    }

    public float getCurrentCapacity() {
        return currentCapacity;
    }

    public String getColor() {
        return color;
    }
    public Image getTexture() {
        return texture;
    }

    public void setTexture(Image texture) {
        this.texture = texture;
    }
}

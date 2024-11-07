package Class.Bar;

import javafx.scene.image.Image;
import java.util.Objects;

public class Bar {
    protected String name;
    protected float maxCapacity;
    protected float currentCapacity;
    protected String color;
    protected Image texture;

    public Bar(String name, int maxCapacity, int currentCapacity, String color) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
        this.color = color;
        this.texture = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/bar.png")));
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

    public void setTexture(Image texture) {
        this.texture = texture;
    }
}

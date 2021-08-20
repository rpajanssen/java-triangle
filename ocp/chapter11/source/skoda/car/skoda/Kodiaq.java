package car.skoda;

import vehicle.api.Type;
import vehicle.car.Car;
import vehicle.car.Engine;

public class Kodiaq extends Car {

    public Kodiaq() {
        super(new Engine(110, 150));
    }

    @Override
    public Type getType() {
        return Type.PASSENGER;
    }

    @Override
    public String getName() {
        return "Kodiaq";
    }
}

package car.ferrari;

import vehicle.api.Type;
import vehicle.car.Car;
import vehicle.car.Engine;

public class Testarossa extends Car {

    public Testarossa() {
        super(new Engine(560, 450));
    }

    @Override
    public Type getType() {
        return Type.PASSENGER;
    }

    @Override
    public String getName() {
        return "Testarossa";
    }
}

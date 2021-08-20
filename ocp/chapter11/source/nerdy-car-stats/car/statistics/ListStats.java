package car.statistics;

import vehicle.car.Car;
import car.ferrari.Testarossa;
import car.skoda.Kodiaq;

import java.util.List;

public class ListStats  {
    private static List<Car> cars = List.of( new Kodiaq(), new Testarossa());

    public static void main(String... args) {
        cars.forEach(
            car -> {
                System.out.println(car.getName());
                System.out.println("    " + car.getEngine().getPower() + " kW");
                System.out.println("    " + car.getEngine().getTorque() + " Nm");
                System.out.println();
            }
        );
    }
}

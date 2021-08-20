package vehicle.car;

public class Engine  {
    private int power; // kW
    private int torque; // Nm

    public Engine(int power, int torque) {
        this.power = power;
        this.torque = torque;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getTorque() {
        return torque;
    }

    public void setTorque(int torque) {
        this.torque = torque;
    }
}

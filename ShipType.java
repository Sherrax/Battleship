package battleship;
public enum ShipType {
    AircraftCarrier("Aircraft Carrier", 5,6),
    BattleShip("Battleship", 4,5),
    Submarine("Submarine", 3,4),
    Cruiser("Cruiser", 3,3),
    Destroyer("Destroyer", 2,2);

    private final String name;
    private final int shipLength;
    private final int shipPower;

    ShipType(String name, int shipLength, int shipPower) {
        this.name = name;
        this.shipLength = shipLength;
        this.shipPower = shipPower;
    }

    public String getName() {
        return name;
    }

    public int getShipLength() {
        return shipLength;
    }

    public int getShipPower() {
        return shipPower;
    }
}

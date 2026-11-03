# ParkingLog

A simple parking lot system in Java. Vehicles enter and are assigned a spot that matches their type, receive a ticket, and pay an hourly fee when they exit.

## Features

- Spot assignment by vehicle type (motorcycle, car, large)
- Ticket issued on entry with a unique ID and entry time
- Hourly fee on exit, with any partial hour rounded up to a full hour
- Spots are freed on exit and tickets can't be reused
- Injectable `Clock` so fees can be tested deterministically

## Project structure

```
src/io/github/eelizan1/parkinglot/
├── Main.java              # Example usage
├── ParkingLot.java        # Core logic: enter, exit, fee calculation
├── enums/
│   ├── SpotType.java
│   └── VehicleType.java
└── model/
    ├── ParkingSpot.java
    └── Ticket.java
test/io/github/eelizan1/parkinglot/
└── ParkingLotTest.java    # JUnit 5 tests
```

## Usage

```java
List<ParkingSpot> spots = Arrays.asList(
        new ParkingSpot("car-1", SpotType.CAR),
        new ParkingSpot("moto-1", SpotType.MOTORCYCLE),
        new ParkingSpot("large-1", SpotType.LARGE)
);

ParkingLot parkingLot = new ParkingLot(spots, 5.0); // $5 per hour

Ticket ticket = parkingLot.enter(VehicleType.CAR);
Double fee = parkingLot.exit(ticket.getId());
```

### Fee examples ($5/hour)

| Time parked   | Fee  |
|---------------|------|
| 0 minutes     | $0   |
| 1 minute      | $5   |
| 3 hours       | $15  |
| 3 hours + 1ms | $20  |
| 24 hours      | $120 |

## Requirements

- Java 17
- JUnit 5 (for tests)

## Running

### IntelliJ IDEA

Open the project folder. `src/` is the source root and `test/` is the test root. Run `Main` for the example, or `ParkingLotTest` for the tests. The JUnit library is configured to load from your local Maven repository (`~/.m2`); if IntelliJ can't find it, add JUnit 5 via **File → Project Structure → Libraries**.

### Command line

```bash
javac -d out $(find src -name '*.java')
java -cp out io.github.eelizan1.parkinglot.Main
```

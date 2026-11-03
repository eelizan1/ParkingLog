import java.time.Clock;
import java.util.*;

public class ParkingLot {
    private final List<ParkingSpot> spots;
    private final Set<String> occupiedSpotIds;
    private final Map<String, Ticket> activeTickets;
    private final double hourlyRate;
    private final Clock clock;

    public ParkingLot(List<ParkingSpot> spots, double hourlyRate) {
        this(spots, hourlyRate, Clock.systemUTC());
    }

    // clock is injectable so tests can control entry/exit times
    public ParkingLot(List<ParkingSpot> spots, double hourlyRate, Clock clock) {
        this.spots = spots;
        this.hourlyRate = hourlyRate;
        this.clock = clock;
        this.occupiedSpotIds = new HashSet<>();
        this.activeTickets = new HashMap<>();
    }

    // Finds available spot, marks spot as occupied, creates ticket and marks as active
    public Ticket enter(VehicleType vehicleType) {
        ParkingSpot spot = findAvailableSpot(vehicleType);

        if (spot == null) throw new RuntimeException("No available spots for this parking lot");

        occupiedSpotIds.add(spot.getId());
        Ticket ticket = new Ticket(
                UUID.randomUUID().toString(),
                spot.getId(),
                vehicleType,
                clock.millis()
        );
        activeTickets.put(ticket.getId(), ticket);

        return ticket;
    }

    // retrieves ticket, computes fee, removes occupied spot and active ticket
    public Double exit(String ticketId) {
        Ticket ticket = activeTickets.get(ticketId);

        if (ticket == null) throw new RuntimeException("Ticket not found");

        Double fee = computeFee(ticket.getEntryTime(), clock.millis());

        occupiedSpotIds.remove(ticket.getSpotId());
        activeTickets.remove(ticketId);

        return fee;
    }

    // goes through each parking spot to find an unoccupied spot matching vehicle type
    private ParkingSpot findAvailableSpot(VehicleType vehicleType) {
        SpotType requiredSpotType = mapVehicleTypeToSpotType(vehicleType);

        if (requiredSpotType == null) throw new RuntimeException("Vehicle type not found");

        for (ParkingSpot spot : spots) {
            if (spot.getSpotType() == requiredSpotType && !occupiedSpotIds.contains(spot.getId())) {
                return spot;
            }
        }

        return null;
    }

    private SpotType mapVehicleTypeToSpotType(VehicleType vehicleType) {
        if (vehicleType == VehicleType.MOTORCYCLE) {
            return SpotType.MOTORCYCLE;
        } else if (vehicleType == VehicleType.CAR) {
            return SpotType.CAR;
        } else if (vehicleType == VehicleType.LARGE) {
            return SpotType.LARGE;
        }

        return null;
    }

    private Double computeFee(long entryTime, long exitTime) {
        long durationMillis = exitTime - entryTime;
        long durationHours = durationMillis / (1000 * 60 * 60);

        if (durationMillis % (1000 * 60 * 60) > 0) {
            durationHours++;
        }

        return durationHours * hourlyRate;
    }
}

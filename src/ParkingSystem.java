import java.util.*;

public class ParkingSystem {
    private enum Status { EMPTY, OCCUPIED, DELETED }
    static class Spot {
        String licensePlate;
        long entryTime;
        Status status = Status.EMPTY;
        Spot() {}
    }
    private final Spot[] lot;
    private final int capacity;
    private int occupiedCount = 0;
    private int totalProbes = 0;
    public ParkingSystem(int size) {
        this.capacity = size;
        this.lot = new Spot[size];
        for (int i = 0; i < size; i++) lot[i] = new Spot();
    }
    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }
    public String parkVehicle(String licensePlate) {
        if (occupiedCount >= capacity) return "Parking Lot Full";
        int preferredSpot = hash(licensePlate);
        int probes = 0;
        int current = preferredSpot;
        while (lot[current].status == Status.OCCUPIED) {
            current = (current + 1) % capacity;
            probes++;
        }
        lot[current].licensePlate = licensePlate;
        lot[current].entryTime = System.currentTimeMillis();
        lot[current].status = Status.OCCUPIED;
        occupiedCount++;
        totalProbes += probes;
        return String.format("Assigned spot #%d (%d probes)", current, probes);
    }
    public String exitVehicle(String licensePlate) {
        int startSpot = hash(licensePlate);
        int current = startSpot;
        int checked = 0;
        while (checked < capacity) {
            if (lot[current].status == Status.EMPTY) break;
            if (lot[current].status == Status.OCCUPIED && lot[current].licensePlate.equals(licensePlate)) {
                long durationMs = System.currentTimeMillis() - lot[current].entryTime;
                double fee = (durationMs / 1000.0) * 0.50; // $0.50 per second for demo

                lot[current].status = Status.DELETED; // Use lazy deletion
                lot[current].licensePlate = null;
                occupiedCount--;

                return String.format("Spot #%d freed. Fee: $%.2f", current, fee);
            }
            current = (current + 1) % capacity;
            checked++;
        }
        return "Vehicle not found";
    }
    public void getStatistics() {
        double occupancy = (occupiedCount * 100.0) / capacity;
        double avgProbes = occupiedCount == 0 ? 0 : (double) totalProbes / occupiedCount;
        System.out.printf("Occupancy: %.1f%%, Avg Probes: %.2f\n", occupancy, avgProbes);
    }
    public static void main(String[] args) {
        ParkingSystem p = new ParkingSystem(500);
        System.out.println(p.parkVehicle("ABC-1234"));
        System.out.println(p.parkVehicle("ABC-1235"));
        System.out.println(p.exitVehicle("ABC-1234"));
        p.getStatistics();
    }
}
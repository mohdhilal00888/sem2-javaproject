
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class Vehicle {
    String vehicleNumber;
    String vehicleType;
    int slotNumber;
    LocalDateTime entryTime;
    LocalDateTime exitTime;

    Vehicle(String number, String type, int slot,
            LocalDateTime entry) {
        vehicleNumber = number;
        vehicleType = type;
        slotNumber = slot;
        entryTime = entry;
    }
}

public class ParkingManagementSystem {

    static final int TOTAL_SLOTS = 10;
    static final double FEE_PER_HOUR = 20.0;

    static Vehicle[] slots = new Vehicle[TOTAL_SLOTS];
    static Vehicle[] history = new Vehicle[1000];
    static int historyCount = 0;

    static Scanner sc = new Scanner(System.in);

    static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // Display parking slots
    static void displaySlots() {
        System.out.println("\n===== PARKING SLOT STATUS =====");

        for (int i = 0; i < TOTAL_SLOTS; i++) {
            if (slots[i] == null) {
                System.out.println("Slot " + (i + 1) + ": Available");
            } else {
                System.out.println("Slot " + (i + 1)
                        + ": Occupied by "
                        + slots[i].vehicleNumber
                        + " (" + slots[i].vehicleType + ")");
            }
        }
    }

    // Vehicle entry and slot allocation
    static void vehicleEntry() {
        int freeSlot = -1;

        for (int i = 0; i < TOTAL_SLOTS; i++) {
            if (slots[i] == null) {
                freeSlot = i;
                break;
            }
        }

        if (freeSlot == -1) {
            System.out.println("Parking is full!");
            return;
        }

        System.out.print("Enter vehicle number: ");
        String number = sc.nextLine().trim().toUpperCase();

        if (number.isEmpty()) {
            System.out.println("Vehicle number cannot be empty.");
            return;
        }

        for (Vehicle v : slots) {
            if (v != null && v.vehicleNumber.equals(number)) {
                System.out.println("Vehicle is already parked!");
                return;
            }
        }

        System.out.print("Enter vehicle type (Car/Bike/Van): ");
        String type = sc.nextLine().trim();

        if (type.isEmpty()) {
            System.out.println("Vehicle type cannot be empty.");
            return;
        }

        if (historyCount == history.length) {
            System.out.println("Parking history is full.");
            return;
        }

        Vehicle vehicle = new Vehicle(
                number, type, freeSlot + 1, LocalDateTime.now());

        slots[freeSlot] = vehicle;
        history[historyCount++] = vehicle;

        System.out.println("\nVehicle entry recorded successfully!");
        System.out.println("Vehicle Number : " + number);
        System.out.println("Parking Slot  : " + (freeSlot + 1));
        System.out.println("Entry Time    : "
                + vehicle.entryTime.format(formatter));
    }

    // Vehicle exit and fee calculation
    static void vehicleExit() {
        System.out.print("Enter vehicle number for exit: ");
        String number = sc.nextLine().trim().toUpperCase();

        for (int i = 0; i < TOTAL_SLOTS; i++) {
            Vehicle v = slots[i];

            if (v != null && v.vehicleNumber.equals(number)) {
                v.exitTime = LocalDateTime.now();

                long minutes = Math.max(1,
                        Duration.between(v.entryTime, v.exitTime)
                                .toMinutes());

                long hours = (minutes + 59) / 60;
                double fee = hours * FEE_PER_HOUR;

                System.out.println("\n===== PARKING RECEIPT =====");
                System.out.println("Vehicle Number : " + v.vehicleNumber);
                System.out.println("Vehicle Type   : " + v.vehicleType);
                System.out.println("Slot Number    : " + v.slotNumber);
                System.out.println("Entry Time     : "
                        + v.entryTime.format(formatter));
                System.out.println("Exit Time      : "
                        + v.exitTime.format(formatter));
                System.out.println("Duration       : " + minutes + " minutes");
                System.out.println("Billable Hours : " + hours);
                System.out.printf("Parking Fee    : Rs. %.2f%n", fee);
                System.out.println("===========================");

                slots[i] = null;
                System.out.println("Parking slot released successfully.");
                return;
            }
        }

        System.out.println("Vehicle not found in parking area.");
    }

    // View current parked vehicles
    static void viewParkedVehicles() {
        System.out.println("\n===== PARKED VEHICLES =====");
        boolean found = false;

        for (Vehicle v : slots) {
            if (v != null) {
                found = true;
                System.out.println(
                        "Vehicle: " + v.vehicleNumber
                        + " | Type: " + v.vehicleType
                        + " | Slot: " + v.slotNumber
                        + " | Entry: " + v.entryTime.format(formatter));
            }
        }

        if (!found) {
            System.out.println("No vehicles currently parked.");
        }
    }

    // View entry and exit history
    static void viewHistory() {
        System.out.println("\n===== VEHICLE HISTORY =====");

        if (historyCount == 0) {
            System.out.println("No vehicle records found.");
            return;
        }

        for (int i = 0; i < historyCount; i++) {
            Vehicle v = history[i];

            System.out.println("\nVehicle Number : " + v.vehicleNumber);
            System.out.println("Vehicle Type   : " + v.vehicleType);
            System.out.println("Slot Number    : " + v.slotNumber);
            System.out.println("Entry Time     : "
                    + v.entryTime.format(formatter));

            if (v.exitTime != null) {
                System.out.println("Exit Time      : "
                        + v.exitTime.format(formatter));
            } else {
                System.out.println("Status         : Currently Parked");
            }
        }
    }

    // Main menu
    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n================================");
            System.out.println(" PARKING MANAGEMENT SYSTEM");
            System.out.println("================================");
            System.out.println("1. Vehicle Entry");
            System.out.println("2. Vehicle Exit");
            System.out.println("3. Display Parking Slots");
            System.out.println("4. View Parked Vehicles");
            System.out.println("5. View Vehicle History");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                choice = 0;
                continue;
            }

            switch (choice) {
                case 1:
                    vehicleEntry();
                    break;
                case 2:
                    vehicleExit();
                    break;
                case 3:
                    displaySlots();
                    break;
                case 4:
                    viewParkedVehicles();
                    break;
                case 5:
                    viewHistory();
                    break;
                case 6:
                    System.out.println("Thank you for using the system!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 6);

        sc.close();
    }
}

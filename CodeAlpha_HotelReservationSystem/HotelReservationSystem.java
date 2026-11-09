
import java.util.*;
import java.io.*;

class Booking {
    int id, room, days;
    String name;

    Booking(int id, String name, int room, int days) {
        this.id = id;
        this.name = name;
        this.room = room;
        this.days = days;
    }

    double getBill() {
        return days * 1500.0;
    }

    void display() {
        System.out.println("--------------------------");
        System.out.println("Booking ID : " + id);
        System.out.println("Name       : " + name);
        System.out.println("Room       : " + room);
        System.out.println("Days       : " + days);
        System.out.println("Total Bill : Rs. " + getBill());
    }
}

public class HotelReservationSystem {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Booking> bookings = new ArrayList<>();
    static int[] rooms = {101, 102, 103, 104, 105};
    static int nextId = 1;
    static final String FILE_NAME = "bookings.txt";

    public static void main(String[] args) {
        loadBookings();

        while (true) {
            System.out.println("\n===== HOTEL RESERVATION SYSTEM =====");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. View All Bookings");
            System.out.println("4. Search Booking");
            System.out.println("5. Cancel Booking");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int choice = readInt();

            switch (choice) {
                case 1:
                    viewRooms();
                    break;
                case 2:
                    bookRoom();
                    break;
                case 3:
                    viewBookings();
                    break;
                case 4:
                    searchBooking();
                    break;
                case 5:
                    cancelBooking();
                    break;
                case 6:
                    saveBookings();
                    System.out.println("Data saved successfully!");
                    System.out.println("Thank you for visiting!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    static int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            sc.nextLine();
        }
        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    static boolean isBooked(int room) {
        for (Booking b : bookings)
            if (b.room == room)
                return true;
        return false;
    }

    static boolean isValidRoom(int room) {
        for (int r : rooms)
            if (r == room)
                return true;
        return false;
    }

    static void viewRooms() {
        System.out.println("\n===== AVAILABLE ROOMS =====");
        boolean found = false;

        for (int room : rooms) {
            if (!isBooked(room)) {
                System.out.println("Room " + room + " - Rs. 1500/day");
                found = true;
            }
        }

        if (!found)
            System.out.println("No rooms available.");
    }

    static void bookRoom() {
        System.out.print("Enter customer name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty() || name.contains("|")) {
            System.out.println("Invalid customer name.");
            return;
        }

        viewRooms();
        System.out.print("Enter room number: ");
        int room = readInt();

        if (!isValidRoom(room) || isBooked(room)) {
            System.out.println("Room is invalid or already booked.");
            return;
        }

        System.out.print("Enter number of days: ");
        int days = readInt();

        if (days <= 0) {
            System.out.println("Days must be greater than zero.");
            return;
        }

        Booking b = new Booking(nextId++, name, room, days);
        bookings.add(b);
        saveBookings();

        System.out.println("\nRoom booked successfully!");
        b.display();
    }

    static void viewBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }

        System.out.println("\n===== ALL BOOKINGS =====");
        for (Booking b : bookings)
            b.display();
    }

    static void searchBooking() {
        System.out.print("Enter booking ID: ");
        int id = readInt();

        for (Booking b : bookings) {
            if (b.id == id) {
                System.out.println("Booking found!");
                b.display();
                return;
            }
        }

        System.out.println("Booking not found.");
    }

    static void cancelBooking() {
        System.out.print("Enter booking ID to cancel: ");
        int id = readInt();

        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).id == id) {
                bookings.remove(i);
                saveBookings();
                System.out.println("Booking cancelled successfully!");
                return;
            }
        }

        System.out.println("Booking not found.");
    }

    static void saveBookings() {
        try (PrintWriter out =
                 new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Booking b : bookings) {
                out.println(b.id + "|" + b.name + "|" +
                            b.room + "|" + b.days);
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings.");
        }
    }

    static void loadBookings() {
        File file = new File(FILE_NAME);
        if (!file.exists())
            return;

        try (Scanner input = new Scanner(file)) {
            while (input.hasNextLine()) {
                String[] data = input.nextLine().split("\\|", -1);

                if (data.length != 4)
                    continue;

                try {
                    int id = Integer.parseInt(data[0]);
                    String name = data[1];
                    int room = Integer.parseInt(data[2]);
                    int days = Integer.parseInt(data[3]);

                    if (id > 0 && !name.trim().isEmpty()
                            && isValidRoom(room) && days > 0
                            && !isBooked(room)) {
                        bookings.add(new Booking(id, name, room, days));
                        nextId = Math.max(nextId, id + 1);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid booking record.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading bookings.");
        }
    }
}

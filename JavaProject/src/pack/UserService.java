package pack;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class UserService {

    public static void userAccess(Scanner scanner) {
        System.out.println("\n----- User Login -----");

        String email = getEmail(scanner);

        while (true) {
            System.out.println("\n1. Book a ticket");
            System.out.println("2. View booked tickets");
            System.out.println("3. Return to main menu");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> bookTicket(scanner, email);
                case 2 -> viewBookedTickets(email);
                case 3 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static String getEmail(Scanner scanner) {
        String email;
        do {
            System.out.print("Enter your email: ");
            email = scanner.nextLine().trim();
            if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                System.out.println("Invalid email format. Please enter a valid email.");
            }
        } while (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"));
        return email;
    }

    private static void bookTicket(Scanner scanner, String email) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            // Check if user exists, create if not
            int userId = DatabaseUtil.getUserIdByEmail(conn, email);
            if (userId == -1) {
                String name = getUserName(scanner);
                String location = getUserLocation(scanner);
                userId = DatabaseUtil.createUser(conn, name, email, location);
            }

            // Display destinations
            DatabaseUtil.displayDestinations();

            // Get destination and payment method
            System.out.print("Choose a destination by ID: ");
            int destinationId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter payment method (Card/Online): ");
            String paymentMethod = scanner.nextLine().trim();

            // Book the ticket
            DatabaseUtil.bookTicket(conn, userId, destinationId, paymentMethod);

            System.out.println("Booking confirmed!");
        } catch (SQLException e) {
            System.out.println("Error booking ticket: " + e.getMessage());
        }
    }

    private static void viewBookedTickets(String email) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            DatabaseUtil.displayUserBookings(conn, email);
        } catch (SQLException e) {
            System.out.println("Error fetching booked tickets: " + e.getMessage());
        }
    }

    private static String getUserName(Scanner scanner) {
        System.out.print("Enter your name: ");
        return scanner.nextLine().trim();
    }

    private static String getUserLocation(Scanner scanner) {
        System.out.print("Enter your location: ");
        return scanner.nextLine().trim();
    }
}

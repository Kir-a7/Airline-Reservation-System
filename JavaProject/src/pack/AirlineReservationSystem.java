package pack;

import java.util.Scanner;

public class AirlineReservationSystem {
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        Destination.initializeDestinations();
        boolean running = true;

        while (running) {
            try {
                System.out.println("\nWelcome to the Airline Reservation System");
                System.out.println("1. User Access");
                System.out.println("2. Admin Access");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> UserService.userAccess(scanner);
                    case 2 -> AdminService.adminAccess(scanner);
                    case 3 -> {
                        running = false;
                        System.out.println("Exiting the system. Goodbye!");
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid choice.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}


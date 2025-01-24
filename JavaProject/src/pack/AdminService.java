package pack;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminService {

    private static final String ADMIN_PASSWORD = "admin123";

    public static void adminAccess(Scanner scanner) {
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine().trim();

        if (!password.equals(ADMIN_PASSWORD)) {
            System.out.println("Invalid password. Access denied.");
            return;
        }

        System.out.println("\n----- Total Bookings -----");
        try (Connection conn = DatabaseUtil.getConnection()) {
            DatabaseUtil.displayAllBookings(conn);
        } catch (SQLException e) {
            System.out.println("Error fetching bookings: " + e.getMessage());
        }
    }
}

package pack;

import java.sql.*;

public class DatabaseUtil {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/airlinereservation";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Common method to execute query and return result
    public static ResultSet executeQuery(String query, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);

        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        
        return stmt.executeQuery();
    }

    // Fetch user ID by email
    public static int getUserIdByEmail(Connection conn, String email) throws SQLException {
        String query = "SELECT id FROM Users WHERE email = ?";
        try (ResultSet rs = executeQuery(query, email)) {
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }

    // Create a new user and return the generated ID
    public static int createUser(Connection conn, String name, String email, String location) throws SQLException {
        String query = "INSERT INTO Users (name, email, location) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, location);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // Display destinations
    public static void displayDestinations() throws SQLException {
        String query = "SELECT * FROM Destinations";
        try (ResultSet rs = executeQuery(query)) {
            while (rs.next()) {
                System.out.printf("%d. %s (Departure: %s)%n", rs.getInt("id"), rs.getString("location"), rs.getString("departure_time"));
            }
        }
    }

    // Book a ticket
    public static void bookTicket(Connection conn, int userId, int destinationId, String paymentMethod) throws SQLException {
        int ticketNumber = (int) (Math.random() * 100000);  // Random ticket number
        String planeType = "Boeing 747";
        String seatNumber = "A" + ((int) (Math.random() * 50) + 1);

        String query = "INSERT INTO Bookings (user_id, destination_id, payment_method, ticket_number, plane_type, seat_number) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, destinationId);
            stmt.setString(3, paymentMethod);
            stmt.setInt(4, ticketNumber);
            stmt.setString(5, planeType);
            stmt.setString(6, seatNumber);
            stmt.executeUpdate();
        }
    }

    // Display user bookings
    public static void displayUserBookings(Connection conn, String email) throws SQLException {
        String query = """
                SELECT b.ticket_number, d.location, d.departure_time, b.payment_method
                FROM Bookings b
                JOIN Users u ON b.user_id = u.id
                JOIN Destinations d ON b.destination_id = d.id
                WHERE u.email = ?""";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    System.out.println("No bookings found for this email.");
                    return;
                }

                while (rs.next()) {
                    System.out.printf("Ticket: %d | Destination: %s | Departure: %s | Payment: %s%n",
                            rs.getInt("ticket_number"),
                            rs.getString("location"),
                            rs.getString("departure_time"),
                            rs.getString("payment_method"));
                }
            }
        }
    }

    // Display all bookings
    public static void displayAllBookings(Connection conn) throws SQLException {
        String query = """
                SELECT b.ticket_number, u.name, d.location, d.departure_time, b.payment_method
                FROM Bookings b
                JOIN Users u ON b.user_id = u.id
                JOIN Destinations d ON b.destination_id = d.id""";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.printf("Ticket: %d | User: %s | Destination: %s | Departure: %s | Payment: %s%n",
                        rs.getInt("ticket_number"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("departure_time"),
                        rs.getString("payment_method"));
            }
        }
    }
}

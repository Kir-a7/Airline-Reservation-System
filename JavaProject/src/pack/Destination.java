package pack;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Destination {
    private final String location;
    private final String departureTime;
    private static final ArrayList<Destination> destinations = new ArrayList<>();

    public Destination(String location, String departureTime) {
        this.location = location;
        this.departureTime = departureTime;
    }

    public String getLocation() {
        return location;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public static ArrayList<Destination> getDestinations() {
        return destinations;
    }

    public static void initializeDestinations() {
        try (Connection conn = DatabaseUtil.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery("SELECT * FROM Destinations")) {

            while (rs.next()) {
                String location = rs.getString("location");
                String departureTime = rs.getString("departure_time");
                destinations.add(new Destination(location, departureTime));
            }
        } catch (SQLException e) {
            System.out.println("Error loading destinations: " + e.getMessage());
        }
    }
}

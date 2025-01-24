package pack;

public class Booking {
    private final int ticketNumber;
    private final String paymentMethod;
    private final String planeType;
    private final String seatNumber;
    private final Destination destination;
    private final User user;

    public Booking(int ticketNumber, String paymentMethod, String planeType, String seatNumber, Destination destination, User user) {
        this.ticketNumber = ticketNumber;
        this.paymentMethod = paymentMethod;
        this.planeType = planeType;
        this.seatNumber = seatNumber;
        this.destination = destination;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "ticketNumber=" + ticketNumber +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", planeType='" + planeType + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", destination=" + destination.getLocation() +
                ", user=" + user.getEmail() +
                '}';
    }
}

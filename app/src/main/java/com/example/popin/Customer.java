import com.example.popin.User;
import com.example.popin.UserType;
import java.util.HashMap;

public class Customer extends User{
    private String name, email, password, phoneNumber;
    private UserType userType;
    private HashMap<Integer, Reservation> reservations;


    public Customer(String name, String email, String password, String phoneNumber) {
        super(name, email, password, UserType.CUSTOMER);

        this.phoneNumber = phoneNumber;
        this.reservations = new HashMap<>();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public HashMap<Integer, Reservation> getReservations() {
        return reservations;
    }

    public void addReservation(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
    }

    public void cancelReservation(int reservationId) {
        reservations.remove(reservationId);
    }
}

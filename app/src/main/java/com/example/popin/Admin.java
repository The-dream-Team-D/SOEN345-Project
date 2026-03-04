import com.example.popin.User.UserType;

public class Admin extends User {
    private String id;

    public Admin(String name, String email, String password, String id) {
        super(name, email, password, UserType.ADMIN);
        this.id = id;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void addEvent(String name, String location, String description, Date date, EventCategory eventCategory) {
        Event event = new Event(name, location, description, date, eventCategory);
        EventCatalog.getInstance().addEvent(event);
    }

    public void removeEvent(int eventId) {
        EventCatalog.getInstance().removeEvent(eventId);
    }

    public void updateEvent(int id, String name, String location, String description, Date date, EventCategory eventCategory) {
        Event e = EventCatalog.getInstance().getEvent(id);

        if(e != null){
            e.setName(name);
            e.setLocation(location);
            e.setDescription(description);
            e.setDate(date);
            e.setEventCategory(eventCategory);
            //update the event in the category list as well
            EventCatalog.getInstance().updateEvent(e);
        }
    }
    
}

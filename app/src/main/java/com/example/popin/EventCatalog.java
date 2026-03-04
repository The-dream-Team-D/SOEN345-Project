import java.sql.Date;
import java.util.HashMap;

import com.example.popin.Event;


//SHOULD BE MODIFIED TO BE A SINGLETON CLASS
public class EventCatalog {
    private HashMap<Integer, org.w3c.dom.events.Event> events, concerts, sports, theaters, comedies, festivals, musics;
    private static EventCatalog instance;

    private EventCatalog() {
        this.events = new HashMap<>();
        this.concerts = new HashMap<>();
        this.sports = new HashMap<>();
        this.theaters = new HashMap<>();
        this.comedies = new HashMap<>();
        this.festivals = new HashMap<>();
        this.musics = new HashMap<>();
    }

    public static EventCatalog getInstance() {
        if (instance == null) {
            instance = new EventCatalog();
        }
        return instance;
    }

    public void addEvent(String name, String location, String description, Date date, EventCategory eventCategory) {
        Event e = new Event(name, location, description, date, eventCategory);
        events.put(e.getId(), e);

        switch (eventCategory) {
            case CONCERT:
                concerts.put(e.getId(), e);
                break;
            case SPORTS:
                sports.put(e.getId(), e);
                break;
            case THEATER:
                theaters.put(e.getId(), e);
                break;
            case COMEDY:
                comedies.put(e.getId(), e);
                break;
            case FESTIVAL:
                festivals.put(e.getId(), e);
                break;
            case MUSIC:
                musics.put(e.getId(), e);
                break;
        
            default:
                break;
        }
    }

    public void removeEvent(int id) {
        events.remove(id);
    }

    public HashMap<Integer, Event> getEvents() {
        return events;
    }

    public Event getEventById(int id){
        return events.get(id);
    }

    public HashMap<Integer, Event> getEventsByCategory(EventCatalog eventCategory){
        switch (eventCategory) {
            case CONCERT:
                return concerts;
                break;
            case SPORTS:
                return sports;
                break;
            case THEATER:
                return theaters;
                break;
            case COMEDY:
                return comedies;
                break;
            case FESTIVAL:
                return festivals;
                break;
            case MUSIC:
                return musics;
                break;
        
            default:
                return null;
                break;
        }
    }

    public boolean makeUnavailableById(int id){
        Event e = events.get(id);

        if(e){
            e.setIsAvailable(false);

            //update the event in the category list as well
            events.remove(id);
            events.put(id, e);

            EventCategory eventCategory = e.getEventCategory();
            switch (eventCategory) {
                case CONCERT:
                    concerts.remove(id);
                    concerts.put(id, e);
                    break;
                case SPORTS:
                    sports.remove(id);
                    sports.put(id, e);
                    break;
                case THEATER:
                    theaters.remove(id);
                    theaters.put(id, e);
                    break;
                case COMEDY:
                    comedies.remove(id);
                    comedies.put(id, e);
                    break;
                case FESTIVAL:
                    festivals.remove(id);
                    festivals.put(id, e);
                    break;
                case MUSIC:
                    musics.remove(id);
                    musics.put(id, e);
                    break;
            
                default:
                    break;
            }
            return 0;
        }

        return 1; //error code returned if the event could not be found
    }

    public boolean changeDescById(int id, String desc){
        Event e = events.get(id);

        if(e){
            e.setDescription(desc);
            //update the event in the category list as well
            events.remove(id);
            events.put(id, e);

            EventCategory eventCategory = e.getEventCategory();
            switch (eventCategory) {
                case CONCERT:
                    concerts.remove(id);
                    concerts.put(id, e);
                    break;
                case SPORTS:
                    sports.remove(id);
                    sports.put(id, e);
                    break;
                case THEATER:
                    theaters.remove(id);
                    theaters.put(id, e);
                    break;
                case COMEDY:
                    comedies.remove(id);
                    comedies.put(id, e);
                    break;
                case FESTIVAL:
                    festivals.remove(id);
                    festivals.put(id, e);
                    break;
                case MUSIC:
                    musics.remove(id);
                    musics.put(id, e);
                    break;
            
                default:
                    break;
            }
            return 0;
        }

        return 1; //error code returned if the event could not be found
    }

    public boolean changeNameById(int id, String name){

        Event e = events.get(id);

        if(e){
            e.setName(name);
            //update the event in the category list as well
            events.remove(id);
            events.put(id, e);

            EventCategory eventCategory = e.getEventCategory();
            switch (eventCategory) {
                case CONCERT:
                    concerts.remove(id);
                    concerts.put(id, e);
                    break;
                case SPORTS:
                    sports.remove(id);
                    sports.put(id, e);
                    break;
                case THEATER:
                    theaters.remove(id);
                    theaters.put(id, e);
                    break;
                case COMEDY:
                    comedies.remove(id);
                    comedies.put(id, e);
                    break;
                case FESTIVAL:
                    festivals.remove(id);
                    festivals.put(id, e);
                    break;
                case MUSIC:
                    musics.remove(id);
                    musics.put(id, e);
                    break;
            
                default:
                    break;
            }
            return 0;
        }

        return 1; //error code returned if the event could not be found
    }

    public boolean changeDateById(int id, Date date){
        Event e = events.get(id);

        if(e){
            e.setDate(date);
            //update the event in the category list as well
            events.remove(id);
            events.put(id, e);

            EventCategory eventCategory = e.getEventCategory();
            switch (eventCategory) {
                case CONCERT:
                    concerts.remove(id);
                    concerts.put(id, e);
                    break;
                case SPORTS:
                    sports.remove(id);
                    sports.put(id, e);
                    break;
                case THEATER:
                    theaters.remove(id);
                    theaters.put(id, e);
                    break;
                case COMEDY:
                    comedies.remove(id);
                    comedies.put(id, e);
                    break;
                case FESTIVAL:
                    festivals.remove(id);
                    festivals.put(id, e);
                    break;
                case MUSIC:
                    musics.remove(id);
                    musics.put(id, e);
                    break;
            
                default:
                    break;
            }
            return 0;
        }

        return 1; //error code returned if the event could not be found
    }

    public boolean changeLocationById(int id, String location){
        Event e = events.get(id);

        if(e){
            e.setLocation(location);
            //update the event in the category list as well
            events.remove(id);
            events.put(id, e);

            EventCategory eventCategory = e.getEventCategory();
            switch (eventCategory) {
                case CONCERT:
                    concerts.remove(id);
                    concerts.put(id, e);
                    break;
                case SPORTS:
                    sports.remove(id);
                    sports.put(id, e);
                    break;
                case THEATER:
                    theaters.remove(id);
                    theaters.put(id, e);
                    break;
                case COMEDY:
                    comedies.remove(id);
                    comedies.put(id, e);
                    break;
                case FESTIVAL:
                    festivals.remove(id);
                    festivals.put(id, e);
                    break;
                case MUSIC:
                    musics.remove(id);
                    musics.put(id, e);
                    break;
            
                default:
                    break;
            }
            return 0;
        }

        return 1; //error code returned if the event could not be found
    }

    public boolean changeCategoryById(int id, EventCategory eventCategory){
        Event e = events.get(id);

        if(e){
            e.setEventCategory(eventCategory);
            //update the event in the category list as well
            events.remove(id);
            events.put(id, e);

            EventCategory oldEventCategory = e.getEventCategory();
            switch (oldEventCategory) {
                case CONCERT:
                    concerts.remove(id);
                    break;
                case SPORTS:
                    sports.remove(id);
                    break;
                case THEATER:
                    theaters.remove(id);
                    break;
                case COMEDY:
                    comedies.remove(id);
                    break;
                case FESTIVAL:
                    festivals.remove(id);
                    break;
                case MUSIC:
                    musics.remove(id);
                    break;
            
                default:
                    break;
            }

            switch (eventCategory) {
                case CONCERT:
                    concerts.put(id, e);
                    break;
                case SPORTS:
                    sports.put(id, e);
                    break;
                case THEATER:
                    theaters.put(id, e);
                    break;
                case COMEDY:
                    comedies.put(id, e);
                    break;
                case FESTIVAL:
                    festivals.put(id, e);
                    break;
                case MUSIC:
                    musics.put(id, e);
                    break;
            
                default:
                    break;
            }
            return 0;
        }

        return 1; //error code returned if the event could not be found
    }
}

public enum EventCategory{
    CONCERT,
    SPORTS,
    THEATER,
    COMEDY,
    FESTIVAL,
    MUSIC
}

package projet.ihm.model;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

public class Incident implements Serializable
{
    private static final String savePath = "./IncidentsSaveFile.txt";

    private Urgency      urgency;
    private Room         room;
    private String       title;
    private String       author;
    private String       description;
    private Building     building;
    private IncidentType type;
    private LocalDate    date;
    private LocalTime    time;

    public Incident (String title, String author, String description, IncidentType type)
    {
        this(title,
             author,
             description,
             type,
             Building.NotPrecised,
             Room.NotPrecised,
             Urgency.NotPrecised,
             LocalDate.now(),
             LocalTime.now());
    }

    public Incident (String title, String author, String description, IncidentType type, Building building, Room room, Urgency urgency,
                     LocalDate date, LocalTime time)
    {
        this.urgency = urgency;
        this.room = room;
        this.title = title;
        this.author = author;
        this.description = description;
        this.building = building;
        this.type = type;
        this.date = date;
        this.time = time;
    }

    public Room room () { return room; }

    public Urgency urgency () { return urgency; }

    public String title () { return title; }

    public String author () { return author; }

    public Building building () { return building; }

    public IncidentType type () { return type; }

    public String description () { return description; }

    public LocalDate date () { return date; }

    public LocalTime time () { return time; }

    public void addToSave ()
    {
        ArrayList<Incident> incidents = readFromSave();

        incidents.add(this);

        writeToSave(incidents);
    }

    public static void writeToSave (ArrayList<Incident> incidents)
    {
        //noinspection ConstantConditions
        File file = new File(savePath);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file)))
        {
            oos.writeObject(incidents);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static ArrayList<Incident> readFromSave ()
    {
        //noinspection ConstantConditions
        File file = new File(savePath);

        try (ObjectInputStream ooi = new ObjectInputStream(new FileInputStream(file)))
        {
            //noinspection unchecked
            return (ArrayList<Incident>) ooi.readObject();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public boolean equals (Object o)
    {
        if (o == null || !(o instanceof Incident)) return false;

        Incident incident = (Incident) o;
        return urgency == incident.urgency && room == incident.room && Objects.equals(title, incident.title) && Objects.equals(author,
                                                                                                                               incident.author) && Objects
                .equals(description, incident.description) && building == incident.building && type == incident.type;
    }

    @Override
    public int hashCode ()
    {
        return Objects.hash(urgency, room, title, author, description, building, type);
    }
}

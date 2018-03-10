package projet.ihm.model.incidents;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

import static projet.ihm.model.incidents.Status.TODO;

public class Incident implements Serializable
{
    //region --------------- Attributes ----------------------

    private static       double IDcount  = 0;
    private static final String savePath = "./IncidentsSaveFile.txt";

    private double       ID;
    private Urgency      urgency;
    private Room         room;
    private String       title;
    private String       author;
    private String       description;
    private Building     building;
    private IncidentType type;
    private LocalDate    date;
    private LocalTime    time;
    private Status       status;

    //endregion

    //region --------------- Constructors --------------------

    public Incident (String title, String author, String description, IncidentType type)
    {
        this(title, author, description, type, Building.NONE, Room.NONE, Urgency.NONE, LocalDate.now(), LocalTime.now(), TODO);
    }

    public Incident (String title, String author, String description, IncidentType type, Building building, Room room, Urgency urgency,
                     LocalDate date, LocalTime time, Status status)
    {
        this.ID = ++IDcount;
        this.urgency = urgency;
        this.room = room;
        this.title = title;
        this.author = author;
        this.description = description;
        this.building = building;
        this.type = type;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    //endregion

    //region --------------- Getters - Setters ---------------

    public double ID () { return this.ID; }

    public Room room () { return room; }

    public Urgency urgency () { return urgency; }

    public String title () { return title; }

    public String author () { return author; }

    public Building building () { return building; }

    public IncidentType type () { return type; }

    public String description () { return description; }

    public LocalDate date () { return date; }

    public LocalTime time () { return time; }

    public Status status () { return this.status; }

    public void setStatus (Status status) { this.status = status; }

    //endregion

    //region --------------- Read/Write Methods --------------

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

    //endregion

    //region --------------- Override Methods ----------------

    @Override
    public boolean equals (Object o)
    {
        return o != null && o instanceof Incident && this.ID == ((Incident) o).ID;
    }

    @Override
    public int hashCode ()
    {
        return Objects.hash(urgency, room, title, author, description, building, type, date, time);
    }

    //endregion
}

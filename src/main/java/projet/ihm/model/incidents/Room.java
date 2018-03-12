package projet.ihm.model.incidents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static projet.ihm.model.incidents.Building.*;

public enum Room
{
    NONE(null, "Non précisé"),
    P1(Parking),
    P2(Parking),
    P3(Parking),
    O111(BatO),
    O106(BatO),
    O109(BatO),
    O318(BatO),
    O234(BatO),
    E130(BatE),
    E123(BatE),
    E135(BatE),
    E145(BatE),
    E134(BatE);

    private Building building;
    private String   label;

    Room (Building building, String label)
    {
        this.building = building;
        this.label = label;
    }

    Room (Building building)
    {
        this.building = building;
        this.label = name();
    }

    public Building building () { return building; }

    public String label () { return label;}

    public static Room getFromLabel (String label)
    {
        for (Room room : values())
        {
            if (room.label.equals(label)) return room;
        }

        throw new IllegalArgumentException("Status not found");
    }

    public static ArrayList<String> labels ()
    {
        return Arrays.stream(Room.values()).map(Room::label).collect(Collectors.toCollection(ArrayList::new));
    }
}

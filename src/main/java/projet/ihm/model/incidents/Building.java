package projet.ihm.model.incidents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum Building
{
    NONE("Non précisé"),
    BatE,
    BatO,
    Parking;

    private String label;

    Building (String label)
    {
        this.label = label;
    }

    Building ()
    {
        this.label = name();
    }

    public String label ()
    {
        return label;
    }

    public static Building getFromLabel (String label)
    {
        for (Building building : values())
        {
            if (building.label.equals(label)) return building;
        }

        throw new IllegalArgumentException("Status not found");
    }

    public static ArrayList<String> labels ()
    {
        return Arrays.stream(Building.values()).map(Building::label).collect(Collectors.toCollection(ArrayList::new));
    }
}

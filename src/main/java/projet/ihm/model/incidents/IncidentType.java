package projet.ihm.model.incidents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum IncidentType
{
    CarreauCasse("Carreau Cassé"),
    VoitureMalGaree("Voiture mal garée"),
    VitreBrisee("Vitre brisée"),
    LampeCassee("Lampe cassée"),
    Fournitures("Manque de fournitures");

    private String label;

    IncidentType ()
    {
        this.label = name();
    }

    IncidentType (String label)
    {
        this.label = label;
    }

    public String label ()
    {
        return label;
    }

    public static IncidentType getFromLabel (String label)
    {
        for (IncidentType type : values())
        {
            if (type.label.equals(label)) return type;
        }

        throw new IllegalArgumentException("Status not found");
    }

    public static ArrayList<String> labels ()
    {
        return Arrays.stream(IncidentType.values()).map(IncidentType::label).collect(Collectors.toCollection(ArrayList::new));
    }
}

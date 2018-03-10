package projet.ihm.model.incidents;

public enum IncidentType
{
    CarreauCasse("Carreau Cassé"),
    VoitureMalGaree("Voiture mal garée"),
    VitreBrisee("Vitre brisée"),
    LampeCassee("Lampe cassée"),
    PlusDeFeutres("Plus de feutres");

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
}

package projet.ihm.model.incidents;

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
}

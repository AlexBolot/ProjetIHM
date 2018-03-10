package projet.ihm.model.incidents;

public enum Urgency
{
    NONE("Non précisé", 0),
    Mineure(1),
    Faible(2),
    Moderee("Modérée", 3),
    Grande(4),
    Majeure(5);

    private String label;
    private int    urgencyLevel;

    Urgency (String label, int urgencyLevel)
    {
        this.label = label;
        this.urgencyLevel = urgencyLevel;
    }

    Urgency (int urgencyLevel)
    {
        this.label = name();
        this.urgencyLevel = urgencyLevel;
    }

    public String label ()
    {
        return label;
    }

    public int urgencyLevel ()
    {
        return urgencyLevel;
    }

    public static Urgency getFromLabel (String label)
    {
        for (Urgency urgency : values())
        {
            if (urgency.label.equals(label)) return urgency;
        }

        throw new IllegalArgumentException("Urgency not found");
    }
}

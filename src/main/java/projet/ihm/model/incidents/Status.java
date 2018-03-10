package projet.ihm.model.incidents;

public enum Status
{
    TODO("À faire"),
    DOING("En cours"),
    DONE("Fait");

    private String label;

    Status (String label) { this.label = label; }

    public String label () { return label; }

    public static Status getFromLabel (String label)
    {
        for (Status status : values())
        {
            if (status.label.equals(label)) return status;
        }

        throw new IllegalArgumentException("Status not found");
    }
}

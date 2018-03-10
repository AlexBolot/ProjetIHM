package projet.ihm.model.incidents;

import org.jetbrains.annotations.Contract;

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

    @Contract (pure = true)
    public Building building () { return building; }

    @Contract (pure = true)
    public String label () { return label;}
}

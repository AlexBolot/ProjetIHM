package projet.ihm.model;

import org.jetbrains.annotations.Contract;

import static projet.ihm.model.Building.*;

public enum Room
{
    Non_Precisé(null),
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

    private Building value;

    Room (Building building)
    {
        value = building;
    }

    @Contract (pure = true)
    public Building value ()
    {
        return value;
    }
}

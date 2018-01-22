package projet.ihm.model;

import org.jetbrains.annotations.Contract;

import static projet.ihm.model.Building.BatimentEst;
import static projet.ihm.model.Building.BatimentOuest;

public enum Room
{
    NotPrecised(null),
    O111(BatimentOuest),
    O234(BatimentOuest),
    E123(BatimentEst),
    E345(BatimentEst),
    EM234(BatimentEst);

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

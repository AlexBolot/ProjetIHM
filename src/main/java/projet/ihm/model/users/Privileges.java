package projet.ihm.model.users;

import java.io.Serializable;
import java.util.HashSet;

public class Privileges implements Serializable
{
    private HashSet<Privilege> privileges;

    public Privileges () { privileges = new HashSet<>(); }

    public HashSet<Privilege> privileges () { return privileges; }

    public Privileges grant (Privilege privileges)
    {
        this.privileges.add(privileges);
        return this;
    }

    public boolean hasPrivilege (Privilege privilege)
    {
        return privileges.contains(privilege);
    }

    public enum Privilege
    {
        AddIncident,
        UpdateStatus,
        DeleteIncident
    }
}
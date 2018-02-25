package projet.ihm.model;

import java.util.ArrayList;
import java.util.List;

public class Account
{
    public static Account currentLoggedIn = null;

    public static final List<Account> accounts = new ArrayList<Account>()
    {{
        //mdp:cestpasfaux
        add(new Account("Charles Ranger", 1928682653));
        //mdp:lhermite
        add(new Account("Bernard", 56234934));
    }};

    private String name;
    private int    passwordHash;

    private Account (String name, int passwordHash)
    {
        this.name = name;
        this.passwordHash = passwordHash;
    }

    public String getName ()
    {
        return name;
    }

    public int getPasswordHash ()
    {
        return passwordHash;
    }
}

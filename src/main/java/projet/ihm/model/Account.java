package projet.ihm.model;

import java.util.ArrayList;
import java.util.List;

public class Account {

    public static final List<Account> accounts = new ArrayList<Account>()
                                                {{
                                                    add(new Account("Charles Ranger",1928682653));
                                                    add(new Account("Bernard", 56234934));
                                                }};

    private String name;
    private int passwordHash;

    public Account(String name, int passwordHash){
        this.name = name;
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return name;
    }

    public int getPasswordHash() {
        return passwordHash;
    }


}

package projet.ihm.model.users;

import projet.ihm.model.users.Privileges.Privilege;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import static projet.ihm.model.users.Privileges.Privilege.*;
import static projet.ihm.model.users.User.Preset.*;

public class User implements Serializable
{
    private static final String savePath = "./UsersSaveFile.txt";

    private static final HashMap<Preset, Privileges> sets = new HashMap<Preset, Privileges>()
    {{
        put(Admin, new Privileges().grant(AddIncident).grant(UpdateStatus).grant(DeleteIncident));
        put(Agent, new Privileges().grant(AddIncident).grant(UpdateStatus));
        put(SimpleUser, new Privileges().grant(AddIncident));
    }};

    public static User currentLoggedIn = null;

    private int        passwordHash;
    private String     name;
    private Privileges privileges;

    public User (String name, int passwordHash, Preset preset)
    {
        this(name, passwordHash, sets.get(preset));
    }

    public User (String name, int passwordHash, Privileges privileges)
    {
        this.name = name;
        this.passwordHash = passwordHash;
        this.privileges = privileges;
    }

    public String name () { return name; }

    public int passwordHash () { return passwordHash; }

    public Privileges privileges () { return privileges; }

    public boolean hasPrivilege (Privilege privilege)
    {
        return privileges.hasPrivilege(privilege);
    }

    public static void writeToSave (ArrayList<User> users)
    {
        File file = new File(savePath);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file)))
        {
            oos.writeObject(users);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static ArrayList<User> readFromSave ()
    {
        File file = new File(savePath);

        try (ObjectInputStream ooi = new ObjectInputStream(new FileInputStream(file)))
        {
            //noinspection unchecked
            return (ArrayList<User>) ooi.readObject();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public enum Preset
    {
        Admin,
        Agent,
        SimpleUser
    }
}

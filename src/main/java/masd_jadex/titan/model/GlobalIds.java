package masd_jadex.titan.model;

public class GlobalIds {
    static Integer counter = 0;

    public static synchronized Integer getNewId()
    {
            counter += 1;
            return Integer.valueOf(counter);
    }
}

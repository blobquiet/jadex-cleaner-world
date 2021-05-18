package masd_jadex.titan.model;

public class GlobalIds {
    static Integer counter = 0;

    static int getNewId()
    {
        synchronized (counter)
        {
            Integer x = counter;
            boolean isSame = x == counter;
            counter += 1;
            boolean stillSame = x == counter;
            return counter;
        }
    }
}

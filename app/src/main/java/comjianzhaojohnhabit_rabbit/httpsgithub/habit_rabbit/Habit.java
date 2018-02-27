package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

/**
 * Created by notachimo on 2/25/2018.
 */

public class Habit {
    //public enum Period {DAILY, WEEKLY, MONTHLY} was having trouble with enumeration
    public String description;
    public String period;
    public int timesPerPeriod;
    public int timesCompletedInPeriod;
    public int streak;

    public Habit(String compressedInfo){
        //when being made, we take the string
        //"name,period,timesPerPeriod,timesCompletedInPeriod,streak"
        String[] info = compressedInfo.split(",");
        this.description = info[0];
        this.period = info[1];
        this.timesPerPeriod = Integer.parseInt(info[2]);
        this.timesCompletedInPeriod = Integer.parseInt(info[3]);
        this.streak = Integer.parseInt(info[4]);

    }
}

package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import com.google.gson.annotations.SerializedName;

public class Habit {

    private int id;
    private String name;
    private String period;
    @SerializedName("times")
    private int timesPerPeriod;
    private boolean reminder;
    private transient int timesCompletedInPeriod;

    // temp constructor
    public Habit(int id, String name, String period, int times){
        this.id = id;
        this.name = name;
        this.period = period;
        this.timesPerPeriod = times;
        this.timesCompletedInPeriod = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setTimesPerPeriod(int timesPerPeriod) {
        this.timesPerPeriod = timesPerPeriod;
    }

    public void setTimesCompletedInPeriod(int timesCompletedInPeriod) {
        this.timesCompletedInPeriod = timesCompletedInPeriod;
    }

    public String getName() {
        return name;
    }

    public String getPeriod() {
        return period;
    }

    public int getTimesPerPeriod() {
        return timesPerPeriod;
    }

    public int getTimesCompletedInPeriod() {
        return timesCompletedInPeriod;
    }


    /*
    private static SharedPreferences.Editor editor;
    private static SharedPreferences sharedPref;
    private static Context sharedContext;
    public Habit(String habit_ID){
        //"name,period,timesPerPeriod,timesCompletedInPeriod,streak"
        this.id = habit_ID;
        this.name = sharedPref.getString(this.id + "_NameOfHabit", "error");
        this.period = sharedPref.getString(this.id + "_Period", "error");
        this.timesPerPeriod = sharedPref.getInt(this.id + "_TimesToDoPerPeriod", -1);
        this.timesCompletedInPeriod = sharedPref.getInt(this.id + "_TimesCompletedSoFar", -1);

    }


    public static String makeNewHabit() {
        String newID = UUID.randomUUID().toString();
        Set<String> habit_IDs = sharedPref.getStringSet("habit_IDs",new LinkedHashSet<String>());
        habit_IDs.add(newID);
        editor.putStringSet("habit_IDs", habit_IDs);
        editor.commit();
        HabitList.update();
        return newID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        editor.putString(id+"_NameOfHabit",name);
        editor.commit();
        this.name = name;
        HabitList.update();
    }
    public String getPeriod() {
        return period;
    }
    public void setPeriod(String period) {
        editor.putString(id+"_Period",period);
        editor.commit();
        this.period = period;
        HabitList.update();
    }

    public int getTimesPerPeriod() {
        return timesPerPeriod;
    }
    public void setTimesPerPeriod(int timesPerPeriod) {
        editor.putInt(id+"_TimesToDoPerPeriod",timesPerPeriod);
        editor.commit();
        this.timesPerPeriod = timesPerPeriod;
        HabitList.update();
    }

    public int getTimesCompletedInPeriod() {
        return timesCompletedInPeriod;
    }
    public void setTimesCompletedInPeriod(int timesCompletedInPeriod) {
        editor.putInt(id+"_TimesToDoPerPeriod",timesCompletedInPeriod);
        editor.commit();
        this.timesCompletedInPeriod = timesCompletedInPeriod;
        HabitList.update();
    }

    public void delete(){
        editor.remove(id + "_NameOfHabit");
        editor.remove(id + "_Period");
        editor.remove(id + "_TimesToDoPerPeriod");
        editor.remove(id + "_TimesCompletedSoFar");

        Set<String> habit_IDs = sharedPref.getStringSet("habit_IDs",new LinkedHashSet<String>());
        habit_IDs.remove(id);
        editor.putStringSet("habit_IDs", habit_IDs);

        editor.commit();
        HabitList.update();

    }

    public static void setSharedContext(Context inputContext){
        sharedContext = inputContext;
        sharedPref = sharedContext.getSharedPreferences("HabitInfo", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }
*/
    public String makeString(){
        return id + ',' + name + ',' + this.period;
    }

}
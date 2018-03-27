package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

public class Habit implements Parcelable{

    @SerializedName("habit_id")
    private int habitID;
    private String name;
    private String period;
    @SerializedName("times")
    private int timesPerPeriod;
    private String description;
    private boolean reminder;

    @SerializedName("start_date")
    private Date startDate;
    // times completed in this period
    private transient int streak;
    // history record of this habit
    private transient Hashtable<String, Integer> records;

    //one constructor
    public Habit () {
        habitID = -1;
        name = "";
        period = "day";
        timesPerPeriod = 1;
        description = "";
        reminder = false;
        startDate = new Date();
        streak = 0;
        records = new Hashtable<>();
    }
    //constructor
    public Habit(int id, String name, String period, int times){
        this.habitID = id;
        this.name = name;
        this.period = period;
        if(times==0){
            this.timesPerPeriod=1;
        }
        else{
            this.timesPerPeriod = times;
        }

        this.description = "";
        this.reminder = false;
        streak = 0;
        this.records = new Hashtable<>();
    }
    //constructor
    public Habit(int id, String name, String period, int times, String description, boolean reminder, Date startDate) {
        this.habitID = id;
        this.name = name;
        this.period = period;
        if(times==0){
            this.timesPerPeriod=1;
        }
        else{
            this.timesPerPeriod = times;
        }
        this.description = description;
        this.reminder = reminder;
        this.startDate = startDate;
        streak = 0;
        this.records = new Hashtable<>();
    }

    // following are what we need for implementing parcelabel------------------------------------------------------------------------------------------
    //constructor
    public Habit(Parcel in){
        habitID = Integer.valueOf(in.readString());
        name = in.readString();
        period = in.readString();
        timesPerPeriod=Integer.valueOf(in.readString());
        description = in.readString();
        reminder=false;
        startDate = new Date();
        streak = 0;
    }

    public static final Creator<Habit> CREATOR = new Creator<Habit>() {

        /**
         *Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by
         * @param in The Parcel to read the object's data from.
         * @return Returns a new instance of the Parcelable class.
         */
        @Override
        public Habit createFromParcel(Parcel in) {
            return new Habit(in);
        }

        /**
         *
         * @param size size of your array
         * @return a habit array of given size
         */
        @Override
        public Habit[] newArray(int size) {
            return new Habit[size];
        }
    };

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation
     * @return a bitmask indicating the set of special object types marshaled by this Parcelable object instance.Value is either 0 or CONTENTS_FILE_DESCRIPTOR.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     *Flatten this object in to a Parcel.
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
    Value is either 0 or PARCELABLE_WRITE_RETURN_VALUE.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getHabitID().toString());
        dest.writeString(getName());
        dest.writeString(getPeriod());
        dest.writeString(Integer.toString(getTimesPerPeriod()));
        dest.writeString(getDescription());
    }

    //-----------------------------------------------------------------------------------------
//following is all setter and getter
    public void setHabitID(Integer id) {
        this.habitID = id;
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

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Hashtable<String, Integer> getRecords() {
        return records;
    }

    public void setRecords(Hashtable<String, Integer> records) {
        this.records = records;
    }

    //updating the streak
    public void updateStreaks(Date date, int count) {
        if (this.records == null) {
            this.records = new Hashtable<>();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        records.merge(dateFormat.format(date), count, Integer::sum);

        Calendar today = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(date);
        if (period.equals("day") && (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) &&
                calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            streak += 1;
        }
        if (period.equals("week") && calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.WEEK_OF_YEAR) == today.get(Calendar.WEEK_OF_YEAR)) {
            streak += 1;
        }
        if (period.equals("month") && calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
            streak += 1;
        }
    }

//following are all setter and getter for the variable
    public Integer getHabitID() {
        return habitID;
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

    public boolean isReminder() {
        return reminder;
    }

    public Date getStartDate() {
        return startDate;
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

    //toString method
    public String makeString(Context mContext){
        return this.habitID + ", " + name + ',' + this.period + ',' + reminder + ',' + startDate.toString()
                + ',' + records.size() + ',' + SharedPref.getRecords(mContext).size();
    }
    //getter
    public int getStreak() {
        return streak;
    }

    //setter
    public void setStreak(int streak) {
        this.streak = streak;
    }

}
package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;


import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout; //variable for DrawerLayout
    private ActionBarDrawerToggle mToggle; //variable for the toogle
    public ImageButton streakButton;      // variable for button


    @Override
    /**
     * Called when the activity is starting.
     * generate the view for this page
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.homePage);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.closed);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        TextView username = header.findViewById(R.id.user);
        username.setText(HabitList.getUserName());

        Toast.makeText(MainActivity.this, "Thanks for using our app!", Toast.LENGTH_SHORT).show();
    }

    public static boolean isRabbitAlive() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int complete = 0;
        for (Habit x : HabitList.HABITS_list) {
            Hashtable<String, Integer> rc = x.getRecords();
            String period = x.getPeriod();
            Calendar startDate = Calendar.getInstance();
            startDate.clear();
            startDate.setTime(x.getStartDate());

            if (period.equals("week")) {
                Calendar yesterday = Calendar.getInstance();
                if(startDate.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                        startDate.get(Calendar.WEEK_OF_YEAR) == yesterday.get(Calendar.WEEK_OF_YEAR))
                    break;
                yesterday.add(Calendar.DATE, -7);
                yesterday.set(Calendar.DAY_OF_WEEK, 1);
                for (int i = 0; i < 7; i++) {
                    String dateString = dateFormat.format(yesterday.getTime());
                    if (rc.contains(dateString)) {
                        complete += rc.get(dateString);
                    }
                    yesterday.add(Calendar.DATE, 1);
                }
            } else if (period.equals("month")) {
                Calendar yesterday = Calendar.getInstance();
                if(startDate.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                        startDate.get(Calendar.MONTH) == yesterday.get(Calendar.MONTH))
                    break;
                yesterday.add(Calendar.MONTH, -1);
                yesterday.set(Calendar.DAY_OF_MONTH, 1);
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.setTime(yesterday.getTime());
                for (calendar.setTime(yesterday.getTime()); calendar.get(Calendar.MONTH) == yesterday.get(Calendar.MONTH); calendar.add(Calendar.DATE, 1)
                ){
                    String dateString = dateFormat.format(calendar.getTime());
                    if (rc.contains(dateString)) {
                        complete += rc.get(dateString);
                    }
                    yesterday.add(Calendar.DATE, 1);
                }
            } else {
                Calendar yesterday = Calendar.getInstance();
                if(startDate.get(Calendar.DATE) == yesterday.get(Calendar.DATE))
                    break;
                yesterday.add(Calendar.DATE, -1);
                String dateString = dateFormat.format(yesterday.getTime());
                if (rc.contains(dateString)) {
                    complete = rc.get(dateString);
                }
            }

            if (complete < x.getTimesPerPeriod()) {
                return false;
            }
        }

        return true;
    }


/*


    */
/**
     *Initialize the contents of the Activity's standard options menu
     *
     * @param menu-The options menu in which you place your items
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     *//*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    */
/**
     *This hook is called whenever an item in your options menu is selected.
     * You can use this method for any items for which you would like to do processing without those other facilities.
     *
     * @param item- The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     *//*

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // following switch statement indicate what happened when you click on the Item in the menu
        switch (item.getItemId()) {

            case R.id.nav_Profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case  R.id.nav_agenda:
                startActivity(new Intent(MainActivity.this, CalendarActivity.class));
                break;
            case  R.id.nav_logout:
                try{
                    // clear all the data when user logout the app
                    File autologin = getApplicationContext().getFileStreamPath("autionloginfile");
                    autologin.delete();
                    SharedPref.clearAll(MainActivity.this);
                    String fileName = SharedPref.FILE_NAME;
                    File file= new File(this.getFilesDir().getParent()+"/shared_prefs/"+fileName+".xml");
                    file.delete();
                    //open the login page
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }catch(Exception e){}
                break;
            case  R.id.nav_habits:
                startActivity(new Intent(MainActivity.this, HabitListActivity.class));
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
*/

    /**
     * same as above function but this one is for naviagation bar
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_Profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case  R.id.nav_agenda:
                startActivity(new Intent(MainActivity.this, CalendarActivity.class));
                break;
            case  R.id.nav_habits:
                startActivity(new Intent(MainActivity.this, HabitListActivity.class));
                break;
            case  R.id.nav_aboutus:
                startActivity(new Intent(MainActivity.this, AboutusActivity.class));
                break;

            case  R.id.nav_logout:
                try{
                    File autologin = getApplicationContext().getFileStreamPath("autionloginfile");
                    autologin.delete();
                    SharedPref.clearAll(MainActivity.this);
                    String fileName = SharedPref.FILE_NAME;
                    File file= new File(this.getFilesDir().getParent()+"/shared_prefs/"+fileName+".xml");
                    file.delete();
                    //open the login page
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }catch(Exception e){}
                break;
            default:
        }
        return true;

    }

    /**
     *open a new acitivity call RabbitReview
     * @param view- The view that was clicked.
     */
    public void openrabbit(View view){


       if(isRabbitAlive()){
            startActivity(new Intent(MainActivity.this, RabbitReview.class));
        }
        else {
            startActivity(new Intent(MainActivity.this, DeadRabbitActivity.class));
        }
    }






}

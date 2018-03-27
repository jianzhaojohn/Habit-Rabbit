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

import java.io.File;


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
    }

    /**
     *Initialize the contents of the Activity's standard options menu
     *
     * @param menu-The options menu in which you place your items
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    /**
     *This hook is called whenever an item in your options menu is selected.
     * You can use this method for any items for which you would like to do processing without those other facilities.
     *
     * @param item- The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // following switch statement indicate what happened when you click on the Item in the menu
        switch (item.getItemId()) {
            case Menu.FIRST:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
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
                    //opent he login page
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }catch(Exception e){}
                break;
            case  R.id.nav_habits:
                startActivity(new Intent(MainActivity.this, HabitListActivity.class));
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

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
            case  R.id.nav_logout:
                try{
                    File autologin = getApplicationContext().getFileStreamPath("autionloginfile");
                    autologin.delete();
                    SharedPref.clearAll(MainActivity.this);
                    String fileName = SharedPref.FILE_NAME;
                    File file= new File(this.getFilesDir().getParent()+"/shared_prefs/"+fileName+".xml");
                    file.delete();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }catch(Exception e){}
                break;
            case R.id.nav_Friends:
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
        startActivity(new Intent(MainActivity.this, RabbitReview.class));
    }


    //called when streakButton was clicked
    public void openstreak(View view){
        streakButton = (ImageButton) findViewById(R.id.streakbutton);
        streakButton.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                Intent showGraph = new Intent(MainActivity.this, StreakGraph.class);
                startActivity(showGraph);
            }
        });
    }



}

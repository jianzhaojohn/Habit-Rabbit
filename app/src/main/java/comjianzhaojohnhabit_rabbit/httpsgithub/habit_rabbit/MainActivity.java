package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.ClipData;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.navigation_menu, menu);
       // menu.add(Menu.NONE, Menu.FIRST, 0, "Logout");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
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
                   // getApplicationContext().deleteFile("autionloginfile");
                    File autologin = getApplicationContext().getFileStreamPath("autionloginfile");
                    autologin.delete();
                    // clear and delete data file, then logout
                    SharedPref.clearAll(MainActivity.this);
                    String fileName = SharedPref.FILE_NAME;
                    File file= new File(MainActivity.this.getFilesDir().getParent()+"/shared_prefs/"+fileName+".xml");
                    file.delete();

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
                    // getApplicationContext().deleteFile("autionloginfile");
                    File autologin = getApplicationContext().getFileStreamPath("autionloginfile");
                    autologin.delete();

                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }catch(Exception e){}
                break;

            case R.id.nav_Friends:
                // jump to friends
                break;
            default:
        }
        return true;

    }

    public void openrabbit(View view){
        startActivity(new Intent(MainActivity.this, RabbitReview.class));
    }


}

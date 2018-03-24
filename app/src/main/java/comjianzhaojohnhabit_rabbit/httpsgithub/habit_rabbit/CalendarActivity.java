package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;

public class CalendarActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //   Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case Menu.FIRST:
                startActivity(new Intent(CalendarActivity.this, LoginActivity.class));
                break;
            case R.id.nav_Profile:
                startActivity(new Intent(CalendarActivity.this, ProfileActivity.class));
                break;
            case  R.id.nav_agenda:
               break;
            case  R.id.nav_logout:
                try{
                    // getApplicationContext().deleteFile("autionloginfile");
                    File autologin = getApplicationContext().getFileStreamPath("autionloginfile");
                    autologin.delete();
                    // clear and delete data file, then logout
                    SharedPref.clearAll(CalendarActivity.this);
                    String fileName = SharedPref.FILE_NAME;
                    File file= new File(this.getFilesDir().getParent()+"/shared_prefs/"+fileName+".xml");
                    file.delete();

                    startActivity(new Intent(CalendarActivity.this, LoginActivity.class));
                }catch(Exception e){}
                break;
            case  R.id.nav_habits:
                startActivity(new Intent(CalendarActivity.this, HabitListActivity.class));
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Fragment fragment = new Agenda_Fragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();

    }
}
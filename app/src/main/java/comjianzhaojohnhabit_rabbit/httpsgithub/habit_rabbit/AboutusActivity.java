package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

public class AboutusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus2);
    }
    public void github(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jianzhaojohn/Habit-Rabbit"));
        startActivity(browserIntent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //   Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     *
     * @param item-The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_Profile:
                startActivity(new Intent(AboutusActivity.this, ProfileActivity.class));
                break;
            case R.id.nav_agenda:
                startActivity(new Intent(AboutusActivity.this, CalendarActivity.class));
                break;
            case R.id.nav_habits:
                startActivity(new Intent(AboutusActivity.this, HabitListActivity.class));
                break;

            case R.id.nav_logout:
                try {
                    // getApplicationContext().deleteFile("autionloginfile");
                    File autologin = getApplicationContext().getFileStreamPath("autionloginfile");
                    autologin.delete();
                    // clear and delete data file, then logout
                    SharedPref.clearAll(AboutusActivity.this);
                    String fileName = SharedPref.FILE_NAME;
                    File file = new File(this.getFilesDir().getParent() + "/shared_prefs/" + fileName + ".xml");
                    file.delete();

                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                }
                break;

            default:
        }
        return super.onOptionsItemSelected(item);
    }

}


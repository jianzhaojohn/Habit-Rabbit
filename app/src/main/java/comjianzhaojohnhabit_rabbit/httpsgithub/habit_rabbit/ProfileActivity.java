package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class ProfileActivity extends AppCompatActivity {
    public Button editproflebutton;

    /**
     * Called when the activity is starting.
     * generate the view for this page
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    /**
     *Initialize the contents of the Activity's standard options menu
     *
     * @param menu-The options menu in which you place your items
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //   Inflate the menu; this adds items to the action bar if it is present.
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
        switch (item.getItemId()) {

            case R.id.nav_Profile:
                startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                break;
            case  R.id.nav_agenda:
                startActivity(new Intent(ProfileActivity.this, CalendarActivity.class));
                break;
            case R.id.nav_bug:
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "jianzhaojohn@buffalo.edu" });
                Email.putExtra(Intent.EXTRA_SUBJECT, "[App: Habit Rabbit] - Feedback");
                Email.putExtra(Intent.EXTRA_TEXT, "To HabitRabbit Dev Team:" + "");
                startActivity(Intent.createChooser(Email, "Send Feedback:"));
                return true;
            case  R.id.nav_logout:
                try{
                    // getApplicationContext().deleteFile("autionloginfile");
                    File autologin = getApplicationContext().getFileStreamPath("autionloginfile");
                    autologin.delete();
                    // clear and delete data file, then logout
                    SharedPref.clearAll(ProfileActivity.this);
                    String fileName = SharedPref.FILE_NAME;
                    File file= new File(this.getFilesDir().getParent()+"/shared_prefs/"+fileName+".xml");
                    file.delete();

                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }catch(Exception e){}
                break;
            case  R.id.nav_habits:
                startActivity(new Intent(ProfileActivity.this, HabitListActivity.class));
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    //on clickListener, this is one way android can implement it
    public void Set_onClick(View view) {
        Intent intent =  new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    //another clickListener
    public void editprofile(View view){
        editproflebutton = (Button) findViewById(R.id.EditProfileButton);
        editproflebutton.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                Intent showEditProfile = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(showEditProfile);
            }
        });
    }
}

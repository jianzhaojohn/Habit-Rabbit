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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


    }
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
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                break;
            case R.id.nav_Profile:
                startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                break;
            case  R.id.nav_agenda:
                startActivity(new Intent(ProfileActivity.this, CalendarActivity.class));
                break;
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

                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                }catch(Exception e){}
                break;
            case  R.id.nav_habits:
                startActivity(new Intent(ProfileActivity.this, HabitListActivity.class));
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public void Set_onClick(View view) {
        Intent intent =  new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
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

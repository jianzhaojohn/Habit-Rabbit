package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {
    public Button editproflebutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


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

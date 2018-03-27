package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.*;
import android.widget.ImageView;

public class RabbitReview extends AppCompatActivity {

    Animation animShake;

    /**
     * Called when the activity is starting.
     * generate the view for this page
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("HabitInfo", Context.MODE_PRIVATE);
        if(preferences.getBoolean("RabbitAlive", true)) {
            setContentView(R.layout.activity_rabbit_view_);
        }
        else{
            setContentView(R.layout.activity_deadrabbit);
        }

    }

    //shake the rabbit when you click opn the rabbit
    public void shake(View view) {
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        image.startAnimation(animation);
    }

}

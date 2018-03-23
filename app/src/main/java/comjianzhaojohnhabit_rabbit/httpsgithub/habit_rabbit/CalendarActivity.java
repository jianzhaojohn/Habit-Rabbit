package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity {

   // private CalendarView mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
       // mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        //int tv=findViewById(R.id.editText);


        Fragment fragment = new Agenda_Fragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

    }


    // jump to habits list view
    public void gotoHabit(View view) {
        Intent intent = new Intent(CalendarActivity.this, HabitListActivity.class);
        CalendarActivity.this.startActivity(intent);
    }

}

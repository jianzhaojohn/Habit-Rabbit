package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

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
        //transaction.addToBackStack(null);
        transaction.commit();

    }
    public void complete(View view){
       // boolean checked = ((CheckBox) view).isChecked();
        Button cb=(Button)view.findViewById(R.id.cbox);
        ColorDrawable buttonColor = (ColorDrawable) cb.getBackground();
        int colorId = buttonColor.getColor();
        TextView name=(TextView)view.findViewById(R.id.tv_event_name);
        ArrayList<Habit> total=new ArrayList<>();
        total.addAll(Agenda_Fragment.recentList);
        total.addAll(Agenda_Fragment.todayList);
        total.addAll(Agenda_Fragment.weekList);
        total.addAll(Agenda_Fragment.monthList);
        if(colorId==-65536){

           cb.setBackgroundColor(2122099706);
           TextView tc=(TextView)view.findViewById(R.id.complete_time);
           tc.setText(""+(Integer.valueOf(tc.getText().toString())+1));
            for(int i=0; i<total.size();i++){
                if(total.get(i).getName().equals(name.getText().toString())){
                    total.get(i).setStreak(total.get(i).getStreak()+1);
                    break;
                }
            }
        }
        else{
            cb.setBackgroundColor(-65536);
            TextView tc=(TextView)view.findViewById(R.id.complete_time);
            tc.setText(""+(Integer.valueOf(tc.getText().toString())-1));
            for(int i=0; i<total.size();i++){
                if(total.get(i).getName().equals(name.getText().toString())){
                    total.get(i).setStreak(total.get(i).getStreak()-1);
                    break;
                }
            }
        }



    }
}
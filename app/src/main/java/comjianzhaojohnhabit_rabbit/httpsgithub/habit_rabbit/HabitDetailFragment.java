package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

/**
 * A fragment representing a single Habit detail screen.
 * This fragment is either contained in a {@link HabitListActivity}
 * in two-pane mode (on tablets) or a {@link HabitDetailActivity}
 * on handsets.
 */
public class HabitDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The habit this fragment is presenting.
     */
    private Habit mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HabitDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
//            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            mItem = HabitList.HABITS_list.get(Integer.parseInt(getArguments().getString(ARG_ITEM_ID)));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.habit_detail, container, false);

        // Show habit details
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.title_txt)).setText(mItem.getName());
            ((TextView) rootView.findViewById(R.id.times_txt)).setText(mItem.getTimesPerPeriod() + "");
            Spinner spinner = rootView.findViewById(R.id.period_spinner);
            spinner.setSelection(getIdx(spinner, mItem.getPeriod()));
            ((Switch) rootView.findViewById(R.id.reminder_switch)).setChecked(mItem.isReminder());
            ((TextView) rootView.findViewById(R.id.detail_txt)).setText(mItem.getDescription());
        }

        // show graph
        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);

        List<DataPoint> points = getPoints(mItem);
        DataPoint[] pointArray = points.toArray(new DataPoint[points.size()]);
        LineGraphSeries<DataPoint> lineSeries = new LineGraphSeries<>(pointArray);
        graph.addSeries(lineSeries);
        PointsGraphSeries<DataPoint> pointSeries = new PointsGraphSeries<>(pointArray);
        graph.addSeries(pointSeries);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
//        graph.getViewport().setBackgroundColor(0x0077cc);

        graph.getViewport().setXAxisBoundsManual(true);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        graph.getViewport().setMinX(calendar.getTime().getTime()); // a week ago
        graph.getViewport().setMaxX(new Date().getTime()); // today

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(mItem.getTimesPerPeriod() +1);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space
        // as we use dates as labels, the human rounding to nice readable numbers is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.getGridLabelRenderer().setNumVerticalLabels(mItem.getTimesPerPeriod()+2);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Dates");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Times Completed per Day");

        return rootView;
    }

    public int getIdx(Spinner spinner, String period) {
        int count = spinner.getCount();
        for (int i = 0; i < count; i++) {
            if (spinner.getItemAtPosition(i).toString().equals(period)) {
                return i; // Found!
            }
        }

        return count - 1; // Not found! default
    }

    public List<DataPoint> getPoints(Habit mHabit) {
        ArrayList<DataPoint> points = new ArrayList<>();
        Hashtable<String, Integer> streaks = mHabit.getStreaks();
        Date startDate = mHabit.getStartDate();

        Calendar date = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        for (date.setTime(startDate); date.before(today); date.add(Calendar.DATE, 1)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(date.getTime());
            points.add(new DataPoint(date.getTime(), streaks.getOrDefault(dateString, 0)));
        }
        return points;
    }
}
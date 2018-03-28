package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.SimpleDateFormat;
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

    /**
     * Called when the activity is starting.
     * generate the view for this page
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = HabitList.Habit_table.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        }
    }

    /**
     *
     * Called to have the fragment instantiate its user interface view. This is optional, and non-graphical fragments can return null
     * (which is the default implementation). This will be called between onCreate(Bundle) and onActivityCreated(Bundle).
     *
     * @param inflater-The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container-If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState- If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.habit_detail, container, false);
        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        Log.e("DetailFragment", "CreatedView");
        // Show habit details
        if (mItem != null) {
            Log.e("DetailFragment_mItem", mItem.getName());
            ((TextView) rootView.findViewById(R.id.title_txt)).setText(mItem.getName());
            ((TextView) rootView.findViewById(R.id.times_txt)).setText(mItem.getTimesPerPeriod() + "");
            Spinner spinner = rootView.findViewById(R.id.period_spinner);
            spinner.setSelection(getIdx(spinner, mItem.getPeriod()));
            ((Switch) rootView.findViewById(R.id.reminder_switch)).setChecked(mItem.isReminder());
            ((TextView) rootView.findViewById(R.id.detail_txt)).setText(mItem.getDescription());

            drawLineGraph(graph,mItem);
        }

        return rootView;
    }

    // show habit history in graph as line
    private void drawLineGraph(GraphView graph, Habit habit) {
        String period = habit.getPeriod();
        // draw expected points
        List<DataPoint> expectedPoints = getExpectedPoints(habit);
        DataPoint[] expArray = expectedPoints.toArray(new DataPoint[expectedPoints.size()]);
        LineGraphSeries<DataPoint> expLineSeries = new LineGraphSeries<>(expArray);
        expLineSeries.setColor(Color.LTGRAY);
        expLineSeries.setThickness(1);
        graph.addSeries(expLineSeries);
        PointsGraphSeries<DataPoint> expPointSeries = new PointsGraphSeries<>(expArray);
        graph.addSeries(expPointSeries);
        expPointSeries.setColor(Color.LTGRAY);
        expPointSeries.setCustomShape(new PointsGraphSeries.CustomShape() {
            @Override
            public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(7);
                canvas.drawCircle(x, y, 20, paint);
            }
        });

        // draw actual points
        List<DataPoint> points = getPoints(habit);
        DataPoint[] pointArray = points.toArray(new DataPoint[points.size()]);
        LineGraphSeries<DataPoint> lineSeries = new LineGraphSeries<>(pointArray);
        graph.addSeries(lineSeries);
        lineSeries.setDrawDataPoints(true);
        lineSeries.setDataPointsRadius(18);

        // graph settings
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
//        graph.getViewport().setBackgroundColor(Color.LTGRAY);

        graph.getViewport().setXAxisBoundsManual(true);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        graph.getViewport().setMaxX(calendar.getTime().getTime()); // tomorrow
        if (period.equals("week"))
            calendar.add(Calendar.DATE, -22);
        else if (period.equals("month"))
            calendar.add(Calendar.MONTH, -4);
        else calendar.add(Calendar.DATE, -7);
        graph.getViewport().setMinX(calendar.getTime().getTime()); // a week ago

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
//        graph.getViewport().setMaxY(habit.getTimesPerPeriod() +1);

        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getViewport().setDrawBorder(true);
        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space
        // as we use dates as labels, the human rounding to nice readable numbers is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
//        graph.getGridLabelRenderer().setNumVerticalLabels(habit.getTimesPerPeriod()+2);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Dates");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Times Completed per "+period.toUpperCase());
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
        Hashtable<String, Integer> mHabitRecords = mHabit.getRecords();
        Date startDate = mHabit.getStartDate();
        String mHabitPeriod = mHabit.getPeriod();

        Calendar today = Calendar.getInstance();
        Calendar currentDate = Calendar.getInstance();
        Calendar currentPeriod = Calendar.getInstance();
        currentPeriod.clear();
        currentPeriod.setTime(startDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int count = 0;

        if (mHabitPeriod.equals("week")) {
            for (currentDate.setTime(startDate); currentDate.before(today); currentDate.add(Calendar.DATE, 1)) {
                String dateString = dateFormat.format(currentDate.getTime());
                if (currentDate.get(Calendar.YEAR) == currentPeriod.get(Calendar.YEAR) &&
                        currentDate.get(Calendar.WEEK_OF_YEAR) == currentPeriod.get(Calendar.WEEK_OF_YEAR)){
                    if (mHabitRecords.containsKey(dateString)) {
                        count += mHabitRecords.get(dateString);
                    }
                } else {
                    points.add(new DataPoint(currentPeriod.getTime(), count));
                    currentPeriod.add(Calendar.DATE, 7);
                    if (mHabitRecords.containsKey(dateString)) {
                        count = mHabitRecords.get(dateString);
                    } else {
                        count = 0;
                    }
                }
            }
            points.add(new DataPoint(currentPeriod.getTime(), count));
            return points;
        }

        if (mHabitPeriod.equals("month")) {
            for (currentDate.setTime(startDate); currentDate.before(today); currentDate.add(Calendar.DATE, 1)) {
                String dateString = dateFormat.format(currentDate.getTime());
                if (currentDate.get(Calendar.YEAR) == currentPeriod.get(Calendar.YEAR) &&
                        currentDate.get(Calendar.MONTH) == currentPeriod.get(Calendar.MONTH)){
                    if (mHabitRecords.containsKey(dateString)) {
                        count += mHabitRecords.get(dateString);
                    }
                } else {
                    points.add(new DataPoint(currentPeriod.getTime(), count));
                    currentPeriod.add(Calendar.DATE, 7);
                    if (mHabitRecords.containsKey(dateString)) {
                        count = mHabitRecords.get(dateString);
                    } else {
                        count = 0;
                    }
                }
            }
            points.add(new DataPoint(currentPeriod.getTime(), count));
            return points;
        }

        // default period is day
        for (currentDate.setTime(startDate); currentDate.before(today); currentDate.add(Calendar.DATE, 1)) {
            String dateString = dateFormat.format(currentDate.getTime());
            if (mHabitRecords.containsKey(dateString)) {
                count = mHabitRecords.get(dateString);
            } else {
                count = 0;
            }
            points.add(new DataPoint(currentDate.getTime(), count));
        }
        return points;
    }

    public List<DataPoint> getExpectedPoints(Habit mHabit) {
        ArrayList<DataPoint> points = new ArrayList<>();
        Date startDate = mHabit.getStartDate();
        int count = mHabit.getTimesPerPeriod();

        Calendar date = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);

        if (mHabit.getPeriod().equals("week")) {
            for (date.setTime(startDate); date.before(tomorrow); date.add(Calendar.DATE, 7)) {
                points.add(new DataPoint(date.getTime(), count));
            }
        } else if(mHabit.getPeriod().equals("month")) {
            for (date.setTime(startDate); date.before(tomorrow); date.add(Calendar.MONTH, 1)) {
                points.add(new DataPoint(date.getTime(), count));
            }
        } else {
            for (date.setTime(startDate); date.before(tomorrow); date.add(Calendar.DATE, 1)) {
                points.add(new DataPoint(date.getTime(), count));
            }
        }

        return points;
    }
}
package comjianzhaojohnhabit_rabbit.httpsgithub.habit_rabbit;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yun on 2018/3/6 0006.
 */

public class HabitRequest extends StringRequest {
    private static final String HABIT_REQUEST_URL = "https://habit-rabbit.000webhostapp.com/habit.php";
    private Map<String, String> params;

    public HabitRequest(String username,int habit_id, Response.Listener<String> listener) {
        super(Method.POST, HABIT_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);

    }


}

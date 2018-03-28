<?php
    $con = mysqli_connect("localhost", "id4815804_rabbit_makers", "HabitRabbit", "id4815804_habit_rabbit");
    $username = $_POST["username"];
    $password = $_POST["password"];

    $response = array();
    $habit_ids = array();
    $habits = array();
    $records = array();
    $response["success"] = false;
    $response["username"] = $username;
    $response["habit_ids"] = $habit_ids;
    $response["habits"] = $habits;
    $response["records"] = $records;

    $statement = mysqli_prepare($con, "SELECT * FROM user WHERE username = ?");
    mysqli_stmt_bind_param($statement, "s", $username);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $user, $psw);

    While(mysqli_stmt_fetch($statement)){
        if (password_verify($password, $psw)) {
            // fetch habits
            $stmt = mysqli_prepare($con, "SELECT habit_id,habit,period,times,description,reminder,start_time FROM habits WHERE email = ? ORDER BY habit_id");
            mysqli_stmt_bind_param($stmt, "s", $username);
            mysqli_stmt_execute($stmt);
            mysqli_stmt_bind_result($stmt, $habit_id, $habit, $period, $times, $description, $reminder, $start_time);
            while (mysqli_stmt_fetch($stmt)) {
                $habits[] = ["habit_id"=>$habit_id, "name"=>$habit, "period"=>$period, "times"=>$times,"description"=>$description, "reminder"=>$reminder, "start_date"=>$start_time];
                $habit_ids[] = $habit_id;
            }

            // fetch records SELECT task,date, COUNT(*) FROM `record` GROUP BY task,date
            //$stmt1 = mysqli_prepare($con, "SELECT task, date, COUNT(*) FROM record, habits WHERE email = ? AND task = habit_id GROUP BY task, date ORDER BY task");
            $stmt1 = mysqli_prepare($con, "SELECT task, time, COUNT(*) FROM record, habits WHERE email = ? AND task = habit_id GROUP BY task, time ORDER BY task");
            mysqli_stmt_bind_param($stmt1, "s", $username);
            mysqli_stmt_execute($stmt1);
            mysqli_stmt_bind_result($stmt1, $task, $date, $count);
            while (mysqli_stmt_fetch($stmt1)) {
                $records[] = ["habit_id"=>$task, "date"=>$date, "count"=>$count];
            }

            $response["success"] = true;
            $response["username"] = $user;
            $response["habit_ids"] = $habit_ids;
            $response["habits"] = $habits;
            $response["records"] = $records;
        }
    }
    //mysqli_stmt_close($statement);
    
    echo json_encode($response);
?>
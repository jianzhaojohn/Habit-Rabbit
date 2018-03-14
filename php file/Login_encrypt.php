<?php
    $con = mysqli_connect("localhost", "id4815804_rabbit_makers", "HabitRabbit", "id4815804_habit_rabbit");
    $username = $_POST["username"];
    $password = $_POST["password"];

    $response = array();
    $habit_ids = array();
    $habits = array();
    $response["success"] = false;
    $response["habit_ids"] = $habit_ids;
    $response["habits"] = $habits;

    $statement = mysqli_prepare($con, "SELECT * FROM user WHERE username = ?");
    mysqli_stmt_bind_param($statement, "s", $username);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $user, $psw);

    While(mysqli_stmt_fetch($statement)){
        if (password_verify($password, $psw)) {
            // fetch habits
            $stmt = mysqli_prepare($con, "SELECT habit_id,habit,period,times FROM habits WHERE email = ?");
            mysqli_stmt_bind_param($stmt, "s", $username);
            mysqli_stmt_execute($stmt);
            mysqli_stmt_bind_result($stmt, $habit_id, $habit, $period, $times);
            while (mysqli_stmt_fetch($stmt)) {
                $habits[] = ["id"=>$habit_id, "name"=>$habit, "period"=>$period, "times"=>$times];
                $habit_ids[] = $habit_id;
            }

            $response["success"] = true;
            $response["username"] = $user;
            $response["habit_ids"] = $habit_ids;
            $response["habits"] = $habits;
        }
    }
    mysqli_stmt_close($statement);
    
    echo json_encode($response);
?>
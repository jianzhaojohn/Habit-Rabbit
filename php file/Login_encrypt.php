<?php
    $con = mysqli_connect("localhost", "id4815804_rabbit_makers", "HabitRabbit", "id4815804_habit_rabbit");
    $username = $_POST["username"];
    $password = $_POST["password"];

    $response = array();
    $habits = array();
    $response["success"] = false;
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
                $habits[] = ["habit_id"=>$habit_id, "habit_name"=>$habit, "period"=>$period, "times"=>$times];
            }

            $response["success"] = true;
            $response["username"] = $user;
            $response["habits"] = $habits;
        }
    }
    mysqli_stmt_close($statement);
    
    echo json_encode($response);
?>
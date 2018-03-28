<?php
    $con = mysqli_connect("localhost", "id4815804_rabbit_makers", "HabitRabbit", "id4815804_habit_rabbit");

    $username = $_POST["username"];
    $habit = $_POST["habit"];
    $description = $_POST["description"];
    $period = $_POST["period"];
    $times = $_POST["times"];
    $reminder = $_POST["reminder"];

    function habitNotExist() {
        global $con, $username, $habit;
        $stmt = mysqli_prepare($con, "SELECT * FROM habits WHERE email = ? AND habit = ?");
        mysqli_stmt_bind_param($stmt, "ss", $username, $habit);
        mysqli_stmt_execute($stmt);
        mysqli_stmt_store_result($stmt);
        $count = mysqli_stmt_num_rows($stmt);
        mysqli_stmt_close($stmt);

        return ($count < 1);
    }

    $response = array();
    $response["success"] = false;

    if (habitNotExist()) {
        /* insert new habit */
        $stmt = mysqli_prepare($con, "INSERT INTO habits (email, habit, description, period, times, reminder) VALUES (?, ?, ?, ?, ?, ?)");
        mysqli_stmt_bind_param($stmt, "ssssii", $username, $habit, $description, $period, $times, $reminder);
        mysqli_stmt_execute($stmt);
        mysqli_stmt_close($stmt);

        $response["habit_id"] = mysqli_insert_id($con);
        $response["success"] = true;
    }

    echo json_encode($response);
?>
<?php
    $con = mysqli_connect("localhost", "id4815804_rabbit_makers", "HabitRabbit", "id4815804_habit_rabbit");

    $habit_id = $_POST["habit_id"];
    $username = $_POST["username"];
    $habit = $_POST["habit"];
    $description = $_POST["description"];
    $period = $_POST["period"];
    $times = $_POST["times"];
    $reminder = $_POST["reminder"];

    $response = array();
    $response["success"] = false;
    $response["habit_id"] = $habit_id;

    if (habit_id === "") {
        /* insert new habit */
        $stmt = mysqli_prepare($con, "INSERT INTO habits (email, habit, description, period, times, reminder) VALUES (?, ?, ?, ?, ?, ?)");
        mysqli_stmt_bind_param($stmt, "ssssii", $username, $habit, $description, $period, $times, $reminder);
        mysqli_stmt_execute($stmt);
        mysqli_stmt_close($stmt);

        $response["habit_id"] = mysqli_insert_id($con);
        $response["success"] = true;

    } else {
        /* update habit */
        $stmt = mysqli_prepare($con, "UPDATE habits (habit_id, email, habit, description, period, times, reminder) VALUES (?, ?, ?, ?, ?, ?, ?)");
        mysqli_stmt_bind_param($stmt, "issssii", $habit_id, $username, $habit, $description, $period, $times, $reminder);
        mysqli_stmt_execute($stmt);
        mysqli_stmt_close($stmt);

        $response["success"] = true;
    }

    echo json_encode($response);
?>
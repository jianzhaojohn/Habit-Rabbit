<?php
    $con = mysqli_connect("localhost", "id4815804_rabbit_makers", "HabitRabbit", "id4815804_habit_rabbit");

    $username = $_POST["username"];
    $habit_id = $_POST["habit_id"];
    $date = $_POST["date"];

// ???
    function habitExist() {
        global $con, $username, $habit;
        $stmt = mysqli_prepare($con, "SELECT * FROM habits WHERE email = ? AND habit_id = ?");
        mysqli_stmt_bind_param($stmt, "si", $username, $habit_id);
        mysqli_stmt_execute($stmt);
        mysqli_stmt_store_result($stmt);
        $count = mysqli_stmt_num_rows($stmt);
        mysqli_stmt_close($stmt);

        return ($count > 0);
    }

    $response = array();
    $response["success"] = false;

    //if (habitExist()) {
        $stmt = mysqli_prepare($con, "INSERT INTO record (task, time) VALUES (?, ?)");
        mysqli_stmt_bind_param($stmt, "is", $habit_id, $date);
        mysqli_stmt_execute($stmt);
        mysqli_stmt_close($stmt);

        $response["success"] = true;
    //}

    echo json_encode($response);
?>
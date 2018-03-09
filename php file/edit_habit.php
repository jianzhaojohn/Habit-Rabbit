<?php
    $con = mysqli_connect("localhost", "id4815804_rabbit_makers", "HabitRabbit", "id4815804_habit_rabbit");

    $habit_id = $_POST["habit_id"];
    $username = $_POST["username"];
    $habit = $_POST["habit"];
    $description = $_POST["description"];
    $period = $_POST["period"];
    $times = $_POST["times"];
    $reminder = $_POST["reminder"];

    function habitExist() {
            global $con, $username, $habit;
            $stmt = mysqli_prepare($con, "SELECT * FROM habits WHERE email = ? AND habit_id = ?");
            mysqli_stmt_bind_param($stmt, "si", $username, $habit_id);
            mysqli_stmt_execute($stmt);
            mysqli_stmt_store_result($stmt);
            $count = mysqli_stmt_num_rows($stmt);
            mysqli_stmt_close($stmt);

            return ($count >= 1);
        }

    $response = array();
    $response["success"] = false;

    //if (habitExist()) {
        /* update habit */
        $stmt = mysqli_prepare($con, "UPDATE habits SET habit = ?, description = ?, period = ?, times = ?, reminder = ? WHERE habit_id = ? AND email = ?");
        mysqli_stmt_bind_param($stmt, "sssiiis", $habit, $description, $period, $times, $reminder, $habit_id, $username);
        mysqli_stmt_execute($stmt);
        mysqli_stmt_close($stmt);

        $response["success"] = true;
    //}

    echo json_encode($response);
?>
<?php
    $con = mysqli_connect("localhost", "id4815804_rabbit_makers", "HabitRabbit", "id4815804_habit_rabbit");

    $habit_id = $_POST["habit_id"];
    $username = $_POST["username"];

    $response = array();
    $response["success"] = false;
    //$response["habit_id"] = $habit_id;

    $stmt = mysqli_prepare($con, "DELETE FROM habits WHERE email = ? AND habit_id = ?");
    mysqli_stmt_bind_param($stmt, "si", $username, $habit_id);
    mysqli_stmt_execute($stmt);

    $response["success"] = true;

    echo json_encode($response);
?>
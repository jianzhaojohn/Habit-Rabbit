<?php

    $con = mysqli_connect("localhost", "id4815804_rabbit_makers", "HabitRabbit", "id4815804_habit_rabbit");

    $username = $_POST["username"];
    $password = $_POST["password"];

    function registerUser() {
        global $con, $username, $password;
        $passwordHash = password_hash($password, PASSWORD_DEFAULT);
        $statement = mysqli_prepare($con, "INSERT INTO user (username, password) VALUES (?, ?)");
        mysqli_stmt_bind_param($statement, "ss", $username, $passwordHash);
        mysqli_stmt_execute($statement);
        mysqli_stmt_close($statement);
    }

    function usernameAvailable() {
        global $con, $username;
        $statement = mysqli_prepare($con, "SELECT * FROM user WHERE username = ?");
        mysqli_stmt_bind_param($statement, "s", $username);
        mysqli_stmt_execute($statement);
        mysqli_stmt_store_result($statement);
        $count = mysqli_stmt_num_rows($statement);
        mysqli_stmt_close($statement);

        return ($count < 1);
    }

    $response = array();
    $response["success"] = false;

    if (usernameAvailable()) {
        registerUser();
        $response["success"] = true;
        $response["username"] = $username;
    }

    echo json_encode($response);
?>
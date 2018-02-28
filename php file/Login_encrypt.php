<?php
    require("password.php");
    
    $con = mysqli_connect("localhost", "id4815804_rabbit_makers", "HabitRabbit", "id4815804_habit_rabbit");
    
    $username = $_POST["username"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM user WHERE username = ?");
    mysqli_stmt_bind_param($statement, "s", $username);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $username, $psd);
    
    $response = array();
    $response["success"] = false;
    
    While(mysqli_stmt_fetch($statement)){
        if (password_verify($password, $psd) {
            $response["success"] = true;
            $response["username"] = $username;
        }        
    }
    
    echo json_encode($response);
?>
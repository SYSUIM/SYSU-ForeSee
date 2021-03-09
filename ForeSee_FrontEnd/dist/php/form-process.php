<?php

$errorMSG = "";

// NAME
if (empty($_POST["name"])) {
    $errorMSG = "Name is required ";
} else {
    $name = $_POST["name"];
}

// EMAIL
if (empty($_POST["email"])) {
    $errorMSG .= "Email is required ";
} else {
    $email = $_POST["email"];
}

// SUBJECT
if (empty($_POST["subject"])) {
    $errorMSG .= "Subject is required ";
} else {
    $subject = $_POST["subject"];
}

// PHONE
if (empty($_POST["phone"])) {
    $errorMSG .= "Phone is required ";
} else {
    $phone = $_POST["phone"];
}

// MESSAGE
if (empty($_POST["message"])) {
    $errorMSG .= "Message is required ";
} else {
    $message = $_POST["message"];
}

/* Contact Form Setup Begin */
    $send_name      = "Rometheme";      // Replace your name
    $send_title     = "Rometheme Send Mail";        // Replace email sent title
    $send_address   = "rudhisasmito@gmail.com "; // Replace your email address
    
    $smtp_address   = "rudhisasmitomail@gmail.com";     // Replace your email address
    $smtp_password  = "rudhisasmitomail1234";               // Replace your email password
    $smtp_server    = "smtp.gmail.com"; // Replace your email server address

    
    /* Contact Form Setup End */

    date_default_timezone_set('Etc/UTC');

    require '../inc/phpmailer/PHPMailerAutoload.php';

    $mail = new phpmailer(true);
    try{
        // $mail->SMTPDebug = 2;
        $mail->isSMTP();
        $mail->Host = $smtp_server;
        $mail->SMTPAuth = true;
        $mail->Username = $smtp_address;
        $mail->Password = $smtp_password;
        $mail->SMTPSecure = 'tls';
        $mail->Port = 587;

        // Recipients
        $mail->setFrom($smtp_address, $send_title);
        $mail->addAddress($send_address);
        $mail->addReplyTo($send_address);

        // Content
        $mail->isHTML(true);
        $mail->Subject = $send_title;

        $Body = "";
        $Body .= "Name: ";
        $Body .= $name;
        $Body .= "<br>";
        $Body .= "Email: ";
        $Body .= $email;
        $Body .= "<br>";
        $Body .= "Subject: ";
        $Body .= $subject;
        $Body .= "<br>";
        $Body .= "Phone: ";
        $Body .= $phone;
        $Body .= "<br>";
        $Body .= "Message: ";
        $Body .= $message;
        $Body .= "<br>";

        $mail->Body = $Body;

        $mail->send();
        echo 'Message has been send!';

    } catch (Exception $e){
        // echo 'Message could not be send. Error: ', $mail->ErrorInfo;
        echo 'Message could not be send. Error: ';
    }

?>
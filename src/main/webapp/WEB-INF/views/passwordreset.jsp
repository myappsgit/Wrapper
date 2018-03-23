<%@page import="java.util.Base64"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Password Reset Page</title>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
<script type="text/javascript">

    	function send(){
    		var newPass = document.getElementById("newPassword"); 
	     	var confirmPass = document.getElementById("confirmPassword");
	     	console.log(newPass.value);
	    	var new_Password = btoa(newPass.value);
	    	var confirm_Password = btoa(confirmPass.value);
	    	var email = document.getElementById("email").value;
	    	var token = document.getElementById("token").value;
	    	
	    	var passwordChange = {
	    			newPassword : new_Password, 
	    			confirmPassword : confirm_Password
	    	}
    		$.ajax({
    			type:"POST",
    			dataType : "json",
			    ContentType: "application/json",
     		   	url :"/solutions/servlet/" + email + "/" + token + "/",
     		   beforeSend: function(xhr) {
     		        xhr.setRequestHeader('X-Requested-With', "");
     		       xhr.setRequestHeader("Content-Type", "application/json" );
     		    },
     		   	data :JSON.stringify(passwordChange)
    		});
    	}
    	
</script>
</head>
<body>


	<form id="passwordChange" method="post" action="/solutions/servlet/">
		New password : <input type="password" id=newPassword><br><br>
		Confirm password : <input type="password" id="confirmPassword"><br><br>
		<input type="button" onclick="send()" Value="Change Password">
		<input type="hidden" id="email" value = "<%= request.getParameter("emailId") %>">
		<input type="hidden" id="token" value = "<%= request.getParameter("token") %>">
	</form>
</body>
</html>
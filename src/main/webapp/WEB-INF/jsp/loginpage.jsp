<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv='expires' content='0'>
	<meta http-equiv='pragma' content='no-cache'>
	<meta http-equiv='cache-control' content='no-cache, no-store, must-revalidate'>
	<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" />
	<link type="text/css" rel="stylesheet" href="css/bootstrap.css">
	<link type="text/css" rel="stylesheet"	href="css/bootstrap-responsive.css">
   	<link rel="icon" href="/img/favicon.ico" type="image/x-icon">
	<link rel="shortcut icon" href="/img/favicon.ico" type="image/x-icon">

	<title>Вход в систему</title>
<style>
body {
	background-color: #F5F5F5;
	padding-bottom: 40px;
	padding-top: 40px;
}

.form-signin {
	background-color: #FFFFFF;
	border: 1px solid #E5E5E5;
	border-radius: 5px 5px 5px 5px;
	box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
	margin: 0 auto 20px;
	max-width: 300px;
	padding: 19px 29px 29px;
}

.form-signin .form-signin-heading,.form-signin .checkbox {
	margin-bottom: 10px;
}

.form-signin input[type="text"],.form-signin input[type="password"] {
	font-size: 16px;
	height: auto;
	margin-bottom: 15px;
	padding: 7px 9px;
}
</style>

</head>
<body>
	<div class="container">
		<form class="form-signin" action='j_spring_security_check'
			method='POST'>
			<h2 class="form-signin-heading">Вход в систему</h2>
			<input name='j_username' type="text" placeholder="Логин" class="input-block-level">
			<input name='j_password' type="password" placeholder="Пароль" class="input-block-level">
			<button type="submit" class="btn btn-large btn-primary">Войти</button>
			<h4 class="text-error">
				<c:if test="${param.error != null}">
					Неверное имя пользователя или пароль
				</c:if>
			</h4>
		</form>

	</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ko">
<head>
  <title>Login page</title>
  <%@ include file="./shared/head.jsp" %>
</head>
<body>
<%@ include file="./shared/header.jsp" %>

<div class="container">
  <h1>login</h1>

  <form method="post" action="/loginProcess.do" name="contact">
    <div class="panel-body">
      <div class="form-group">
        <label for="username">아이디</label>
        <input type="text" name="id" id="username" class="form-control" placeholder="이메일을 입력하세요.">
      </div>

      <div class="form-group">
        <label for="password">비밀번호</label>
        <input type="password" name="pw" id="password" class="form-control" placeholder="패스워드를 입력하세요.">
      </div>
    </div>
    <div>
      <input type="hidden" name="_csrf" value="${_csrf.token}">
    </div>

    <div class="clearfix">
      <button type="submit" name="submit" class="btn btn-primary pull-right">로그인</button>
    </div>
  </form>
</div>
</body>
</html>
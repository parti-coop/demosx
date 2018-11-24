<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>로그인 - Democracy</title>
  <%@ include file="./shared/head.jsp" %>

  <!-- form validation -->
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/parsley.js/2.8.1/parsley.min.js"></script>
  <script type="text/javascript" src="<c:url value="/js/parsley-ko.js"/>"></script>
</head>
<body>
<%@ include file="./shared/header.jsp" %>

<div class="sign-container">
  <h3 class="demo-detail-title">로그인</h3>
  <form method="post" action="<c:url value="/loginProcess.do"/>" id="form-login">
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <div class="form-group form-group--demo">
      <label class="demo-form-label" for="inputEmail">아이디</label>
      <input type="email" class="form-control demo-input" name="id" id="inputEmail" placeholder="이메일"
             data-parsley-required="true" data-parsley-whitespace="trim">
    </div>

    <div class="form-group form-group--demo">
      <label class="demo-form-label" for="inputPassword">비밀번호</label>
      <input type="password" class="form-control demo-input" name="pw" id="inputPassword"
             placeholder="6자리 이상 비밀전호를 설정해 주세요."
             data-parsley-required="true">
    </div>
    <c:if test="${not empty loginError}">
      <div class="has-error">
        <p class="help-block help-block-error">아이디 및 비밀번호를 확인해 주세요.</p>
      </div>
    </c:if>
    <p class="form-help-text form-help-text--blue">
      <a class="blue-link" href="<c:url value="/find-password.do"/>">비밀번호를 잊어버리셨나요?</a>
    </p>
    <div class="sing-action">
      <button type="submit" class="btn demo-btn demo-btn--primary btn-sign">로그인</button>
      <a href="<c:url value="/join.do"/>" class="btn d-btn white-btn btn-sign next-btn">회원가입</a>
    </div>
  </form>
</div>

<%@ include file="./shared/footer.jsp" %>

<script>
  $(function () {
    $('#form-login').parsley(parsleyConfig);
  });
</script>
</body>
</html>
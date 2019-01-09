<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>비밀번호 재설정 - Democracy</title>
  <%@ include file="./shared/head.jsp" %>

  <!-- jquery serialize object -->
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/jquery-serialize-object/2.5.0/jquery.serialize-object.min.js"></script>
</head>
<body>
<%@ include file="./shared/header.jsp" %>

<div class="sign-container">
  <h3 class="demo-detail-title">비밀번호 재설정</h3>
  <form method="post" id="je-reset-password">
    <input type="hidden" name="token" value="${token}">
    <div class="form-group form-group--demo">
      <label class="demo-form-label" for="inputEmail">이메일</label>
      <input type="email" class="form-control demo-input" name="email" id="inputEmail" placeholder="이메일을 입력해 주세요."
             data-parsley-required="true" data-parsley-type="email"
             data-parsley-error-message="이메일을 적어주세요.">
    </div>
    <div class="form-group form-group--demo">
      <label class="demo-form-label" for="inputPassword">새 비밀번호</label>
      <input type="password" class="form-control demo-input" name="password" id="inputPassword"
             placeholder="6자리 이상 새 비밀전호를 설정해 주세요."
             data-parsley-required="true" data-parsley-minlength="6">
    </div>
    <div class="form-group form-group--demo">
      <label class="demo-form-label" for="inputPasswordConfirm">새 비밀번호 확인</label>
      <input type="password" class="form-control demo-input" id="inputPasswordConfirm" placeholder="비밀번호를 확인해 주세요."
             data-parsley-required="true" data-parsley-minlength="6" data-parsley-equalto="#inputPassword">
    </div>
    <button type="submit" class="btn demo-btn demo-btn--primary btn-sign">비밀번호 재설정</button>
    <div class="sing-action">
      <a href="<c:url value="/login.do"/>" class="btn demo-btn demo-btn--primary btn-sign">로그인</a>
      <a href="<c:url value="/join.do"/>" class="btn d-btn white-btn btn-sign next-btn">회원가입</a>
    </div>
  </form>
</div>

<%@ include file="./shared/footer.jsp" %>

<script>
  $(function () {
    var $resetPassword = $('#je-reset-password');
    $resetPassword.parsley(parsleyConfig);
    $resetPassword.on('submit', function (event) {
      event.preventDefault();

      var data = $resetPassword.serializeObject();

      $.ajax({
        headers: { 'X-CSRF-TOKEN': '${_csrf.token}' },
        url: '/ajax/reset-password',
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify(data),
        success: function (data) {
          alert(data.msg);
          window.location.href = '/login.do';
        },
        error: function (e) {
          alert('패스워드 설정을 할 수 없습니다.');
        }
      });
    });
  });
</script>
</body>
</html>
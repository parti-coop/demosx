<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>비밀번호찾기 - Democracy</title>
  <%@ include file="./shared/head.jsp" %>
</head>
<body>
<%@ include file="./shared/header.jsp" %>

<div class="sign-container">
  <h3 class="demo-detail-title">비밀번호찾기</h3>
  <form>
    <div class="form-group form-group--demo">
      <label class="demo-form-label" for="inputEmail">이메일로 비밀번호 재설정 링크 전달</label>
      <input type="email" class="form-control demo-input" name="id" id="inputEmail" placeholder="이메일을 입력해 주세요."
             data-parsley-required="true" data-parsley-type="email"
             data-parsley-error-message="<i class='xi-error-o xi-x'></i> 이메일을 적어주세요.">
    </div>
    <button type="submit" class="btn demo-btn demo-btn--primary btn-sign">비밀번호 재설정</button>
    <div class="sing-action">
      <button type="submit" class="btn demo-btn demo-btn--primary btn-sign">로그인</button>
      <a href="<c:url value="/join.do"/>" class="btn d-btn white-btn btn-sign next-btn">회원가입</a>
    </div>
  </form>
</div>

<%@ include file="./shared/footer.jsp" %>

<script>
  $(function () {
    $('form').parsley(parsleyConfig);
  });
</script>
</body>
</html>
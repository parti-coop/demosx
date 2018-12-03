<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>회원가입 - Democracy</title>
  <%@ include file="./shared/head.jsp" %>
</head>
<body>
<%@ include file="./shared/header.jsp" %>

<div class="sign-container">
  <h3 class="demo-detail-title">회원가입</h3>
  <c:if test="${not joinSuccess}">
    <form:form commandName="createDto">
      <spring:bind path="email">
        <div class="form-group form-group--demo${status.error ? ' has-error' : ''}">
          <label class="demo-form-label" for="email">이메일 (로그인 아이디)</label>
          <form:input path="email" type="email" class="form-control demo-input" placeholder="이메일"
                      data-parsley-required="true" data-parsley-type="email" data-parsley-maxlength="100"/>
          <form:errors path="email" cssClass="demo-help-error"/>
          <!-- <p class="demo-help-error"><i class="xi-error-o xi-x"></i> 이메일을 적어주세요.</p> -->
        </div>
      </spring:bind>
      <div class="form-group form-group--demo">
        <label class="demo-form-label" for="name">이름</label>
        <form:input path="name" type="text" class="form-control demo-input" placeholder="이름"
                    data-parsley-required="true" data-parsley-maxlength="30"/>
        <form:errors path="name" cssClass="demo-help-error"/>
        <!-- <p class="demo-help-error"><i class="xi-error-o xi-x"></i> 이름을 적어주세요.</p> -->
      </div>
      <div class="form-group form-group--demo">
        <label class="demo-form-label" for="password">비밀번호</label>
        <form:input path="password" type="password" class="form-control demo-input" placeholder="6자리 이상 비밀전호를 설정해 주세요."
                    data-parsley-required="true" data-parsley-minlength="6"/>
        <form:errors path="password" cssClass="demo-help-error"/>
        <!-- <p class="demo-help-error"><i class="xi-error-o xi-x"></i> 비밀번호를 적어주세요.</p> -->
      </div>
      <div class="form-group form-group--demo">
        <label class="demo-form-label" for="inputPasswordConfirm">비밀번호확인</label>
        <input type="password" class="form-control demo-input" id="inputPasswordConfirm"
               placeholder="비밀번호를 확인해 주세요."
               data-parsley-required="true" data-parsley-minlength="6" data-parsley-equalto="#password">
        <!-- <p class="demo-help-error"><i class="xi-error-o xi-x"></i> 비밀번호를 적어주세요.</p> -->
      </div>
      <button type="submit" class="btn demo-btn demo-btn--primary btn-sign">가입하기</button>
      <div class="sing-action">
        <p class="form-help-text">이미 가입하셨나요?</p>
        <a href="<c:url value="/login.do"/>" class="btn demo-btn demo-btn--primary  btn-sign">로그인하기</a>
      </div>
    </form:form>
  </c:if>
  <c:if test="${joinSuccess}">
    <h3>회원가입하였습니다.</h3>
    <div class="sing-action">
      <a href="<c:url value="/login.do"/>" class="btn demo-btn demo-btn--primary  btn-sign">로그인하기</a>
    </div>
  </c:if>
</div>

<%@ include file="./shared/footer.jsp" %>

<script>
  $(function () {
    $('#createDto').parsley(parsleyConfig);
  });
</script>
</body>
</html>
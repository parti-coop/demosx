<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:if test="${empty loginUser}">
  <div class="demo-side">
    <div class="side-box">
      <p class="side-help-text">로그인 하시면, 다양한 제안과 토론에 참여가 가능합니다.</p>
      <div class="domo-box-buttons">
        <a href="" class="btn demo-btn demo-btn--primary btn-side show-login-modal">로그인하기<i class="xi-angle-right"></i></a>
      </div>
    </div>
  </div>
</c:if>
<c:if test="${not empty loginUser}">
  <div class="demo-side">
    <div class="side-box">
      <div class="profile-circle profile-circle--m" style="background-image: url(${loginUser.viewPhoto()})">
        <p class="alt-text">${loginUser.name}프로필</p>
      </div>
      <h5 class="profile-name">${loginUser.name}</h5>
      <div class="domo-box-buttons">
        <a href="<c:url value="/mypage/proposal.do"/>" class="btn demo-btn demo-btn--primary btn-side">나의 제안 목록 <i
            class="xi-angle-right"></i></a>
        <a href="<c:url value="/mypage/vote.do"/>" class="btn demo-btn demo-btn--primary btn-side">나의 투표 목록 <i
            class="xi-angle-right"></i></a>
      </div>
    </div>
  </div>
</c:if>

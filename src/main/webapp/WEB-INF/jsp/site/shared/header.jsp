<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<nav class="navbar" id="header">
  <div class="container-fluid">
    <div class="navbar-header visible-xs">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
              data-target="#header-menu" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="<c:url value="/index.do"/>">
        <img src="<c:url value="/images/logo.png"/>" alt="logo">
      </a>
    </div>

    <div class="collapse navbar-collapse" id="header-menu">
      <ul class="nav navbar-nav">
        <li><a class="btn font-nanumsquare" href="<c:url value="/find-event.do"/>">이벤트 발견하기</a></li>
        <li><a class="btn font-nanumsquare" href="/member/event.do">이벤트 시작하기</a></li>
      </ul>
      <a class="nav-logo hidden-xs" href="<c:url value="/index.do"/>">
        <img src="<c:url value="/images/logo.png"/>" alt="logo">
      </a>
      <ul class="nav navbar-nav navbar-right">
        <sec:authorize access="isAnonymous()">
          <li><a class="btn font-nanumsquare modal-login-btn" href="#">로그인</a></li>
          <li><a class="btn font-nanumsquare" href="<c:url value="/join.do"/>">회원가입</a></li>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
          <li><a class="btn font-nanumsquare" href="/member/mypage.do">마이페이지</a></li>
          <li>
            <form:form action="/logout.do" method="post">
              <button type="submit" class="btn font-nanumsquare">로그아웃</button>
            </form:form>
          </li>
        </sec:authorize>
      </ul>
    </div>
  </div>
</nav>
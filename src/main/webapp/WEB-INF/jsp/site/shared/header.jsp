<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div class="nav-login-bar">
  <div class="container">
    <ul class="nav-login-ul list-inline text-right">
      <c:if test="${empty loginUser}">
        <li class="nav-login-li"><a href="<c:url value="/login.do"/>" class="nav-login-li__link show-login-modal">로그인</a></li>
        <li class="nav-login-li"><span class="li-middle-line">|</span></li>
        <li class="nav-login-li"><a href="<c:url value="/join.do"/>" class="nav-login-li__link">회원가입</a></li>
      </c:if>
      <c:if test="${not empty loginUser}">
        <c:if test="${loginUser.isAdmin() or loginUser.isManager()}">
          <li class="nav-login-li"><a href="<c:url value="/admin/index.do"/>" class="nav-login-li__link">관리자페이지</a></li>
          <li class="nav-login-li"><span class="li-middle-line">|</span></li>
        </c:if>
        <li class="nav-login-li"><a href="<c:url value="/mypage/info.do"/>" class="nav-login-li__link">마이페이지</a></li>
        <li class="nav-login-li"><span class="li-middle-line">|</span></li>
        <li class="nav-login-li"><a href="#" class="nav-login-li__link logout-link">로그아웃</a></li>
        <form:form action="/logout.do" method="post" class="hidden" id="form-logout">
        </form:form>
        <script>
          $(function () {
            $('.logout-link').click(function (event) {
              event.preventDefault();
              $('#form-logout').submit();
            });
          });
        </script>
      </c:if>
    </ul>
  </div>
</div>
<nav class="navbar navbar-default navbar-default--demo">
  <div class="container">
    <div class="navbar-header navbar-header--demo">
      <button type="button" class="navbar-toggle navbar-toggle--demo collapsed" data-toggle="collapse"
              data-target="#demo-navbar-collapse" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <i class="xi-bars xi-2x demo-toggle demo-toggle--open"></i>
        <i class="xi-close xi-2x demo-toggle demo-toggle--close"></i>
      </button>
      <a class="navbar-brand navbar-brand--demo" href="<c:url value="/index.do"/>">민주주의 서울 오픈소스</a>
    </div>

    <div class="demo-collapse collapse navbar-collapse" id="demo-navbar-collapse">
      <a class="navbar-brand navbar-brand--demo navbar-brand--demo--collapse" href="#">민주주의 서울 오픈소스</a>
      <ul class="nav navbar-nav navbar-right demo-nav">
        <li class="demo-nav-li"><a href="<c:url value="/intro.do"/>">오픈소스
          <div class="nav-li-active-bar"></div>
        </a></li>
        <li class="li-middle"><span class="li-middle-line">|</span></li>
        <li class="dropdown">
          <a href="#" class="dropdown-toggle hidden-xs" data-toggle="dropdown" role="button" aria-haspopup="true"
             aria-expanded="false">시민제안
            <div class="nav-li-active-bar"></div>
          </a>
          <a href="#" class="dropdown-toggle visible-xs">시민제안<div class="nav-li-active-bar"></div></a>
          <ul class="dropdown-menu">
            <li><a href="<c:url value="/proposal-list.do"/>"><i class="xi-angle-right-min"></i> 제안</a></li>
            <li><a href="<c:url value="/debate-list.do"/>"><i class="xi-angle-right-min"></i> 토론</a></li>
            <li><a href="<c:url value="/action-list.do"/>"><i class="xi-angle-right-min"></i> 실행</a></li>
          </ul>
        </li>
        <li class="li-middle"><span class="li-middle-line">|</span></li>
        <li class="demo-nav-li"><a href="<c:url value="/org-debate-list.do"/>">기관제안
          <div class="nav-li-active-bar"></div>
        </a></li>
      </ul>

      <div class="nav-login-m">
        <!-- mobile login START -->
        <ul class="nav-login-m-ul clearfix">
          <c:if test="${empty loginUser}">
            <li class="nav-login-m-li">
              <a href="<c:url value="/login.do"/>" class="nav-login-m-li__link">로그인</a>
            </li>
            <li class="nav-login-m-li">
              <a href="<c:url value="/join.do"/>" class="nav-login-m-li__link nav-login-m-li__link--last">회원가입</a>
            </li>
          </c:if>
          <c:if test="${not empty loginUser}">
            <li class="nav-login-m-li">
              <a href="<c:url value="/mypage/info.do"/>" class="nav-login-m-li__link">마이페이지</a>
            </li>
            <li class="nav-login-m-li">
              <a href="#" class="nav-login-m-li__link nav-login-m-li__link--last logout-link">로그아웃</a>
            </li>
          </c:if>
        </ul>
      </div><!-- mobile login ENDs -->

    </div>
  </div>
</nav>
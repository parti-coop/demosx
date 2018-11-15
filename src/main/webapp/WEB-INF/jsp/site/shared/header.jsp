<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div class="nav-login-bar">
  <div class="container">
    <ul class="nav-login-ul list-inline text-right">
      <li class="nav-login-li"><a href="" class="nav-login-li__link">로그인</a></li>
      <li class="nav-login-li"><span class="li-middle-line">|</span></li>
      <li class="nav-login-li"><a href="" class="nav-login-li__link">회원가입</a></li>
    </ul>
  </div>
</div>
<nav class="navbar navbar-default navbar-default--demo">
  <div class="container">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand navbar-brand--demo" href="<c:url value="/index.do"/>">민주주의 서울 오픈소스</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav navbar-right demo-nav">
        <li class="demo-nav-li active"><a href="#">오픈소스<div class="nav-li-active-bar"></div></a></li>
        <li class="li-middle"><span class="li-middle-line">|</span></li>
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">시민제안<div class="nav-li-active-bar"></div></a>
          <ul class="dropdown-menu">
            <li><a href="<c:url value="/proposal-list.do"/>">제안</a></li>
            <li><a href="<c:url value="/debate-list.do"/>">토론</a></li>
            <li><a href="<c:url value="/action-list.do"/>">실행</a></li>
          </ul>
        </li>
        <li class="li-middle"><span class="li-middle-line">|</span></li>
        <li class="demo-nav-li"><a href="<c:url value="/org-debate-list.do"/>">기관제안<div class="nav-li-active-bar"></div></a></li>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav><!-- nav end  -->

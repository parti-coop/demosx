<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header class="main-header">
  <!-- Logo -->
  <a href="<c:url value="/admin/index.do"/>" class="logo">
    <!-- mini logo for sidebar mini 50x50 pixels -->
    <span class="logo-mini"><b>C</b>TR</span>
    <!-- logo for regular state and mobile devices -->
    <span class="logo-lg"><b>C</b>ENTRAL</span>
  </a>
  <nav class="navbar navbar-static-top">
    <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
      <span class="sr-only">Toggle navigation</span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
    </a>

    <div class="navbar-custom-menu">
      <ul class="nav navbar-nav">
        <li>
          <a href="/index.do" target="_blank">사이트로 이동</a>
        </li>
        <li class="dropdown user user-menu">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown">
            <img src="<c:url value="/images/noavatar.png"/>" class="user-image" alt="User Image">
            <span class="hidden-xs">${loginUser.loginid}</span>
          </a>
          <ul class="dropdown-menu">
            <li class="user-header">
              <img src="<c:url value="/images/noavatar.png"/>" class="img-circle" alt="User Image">
              <p>${loginUser.loginid}</p>
            </li>
            <li class="user-footer">
              <div class="pull-right">
                <form:form action="/logout.do" method="post" cssClass="navbar-form">
                  <button type="submit" class="btn btn-default btn-flat">Logout</button>
                </form:form>
              </div>
            </li>
          </ul>
        </li>
      </ul>
    </div>
  </nav>
</header>

<aside class="main-sidebar">
  <section class="sidebar">
    <ul class="sidebar-menu" data-widget="tree">
      <li class="header">메인 관리</li>
      <li><a href="<c:url value="/admin/options/slider.do"/>"><i class="fa fa-picture-o"></i> <span>메인슬라이더</span></a></li>
      <li><a href="<c:url value="/admin/options/recommend.do"/>"><i class="fa fa-thumbs-up"></i> <span>추천 이벤트</span></a></li>

      <li class="header">User</li>
      <li><a href="<c:url value="/admin/users/list.do"/>"><i class="fa fa-user"></i> <span>회원 관리</span></a></li>

      <li class="header">Event</li>
      <li><a href="<c:url value="/admin/events/list.do"/>"><i class="fa fa-calendar-check-o"></i> <span>이벤트 관리</span></a></li>

      <li class="header">Board</li>
      <li><a href="<c:url value="/admin/posts/notice/list.do"/>"><i class="fa fa-bell"></i> <span>공지사항</span></a></li>
      <li><a href="<c:url value="/admin/posts/faq/list.do"/>"><i class="fa fa-comment"></i> <span>FAQ</span></a></li>
      <li><a href="<c:url value="/admin/post-inquire/list.do"/>"><i class="fa fa-question"></i> <span>문의하기</span></a></li>

      <li class="header">Etc</li>
      <li><a href="<c:url value="/admin/options/agreement-terms.do"/>"><i class="fa fa-info"></i> <span>이용약관</span></a></li>
      <li><a href="<c:url value="/admin/options/agreement-privacy.do"/>"><i class="fa fa-info"></i> <span>개인정보보호정책</span></a></li>

      <li class="header">Stats</li>
      <li><a href="https://adminlte.io/docs"><i class="fa fa-bar-chart"></i> <span>통계</span></a></li>
    </ul>
  </section>
</aside>
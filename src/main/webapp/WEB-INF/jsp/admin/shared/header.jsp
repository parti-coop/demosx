<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header class="main-header">
  <!-- Logo -->
  <a href="<c:url value="/admin/index.do"/>" class="logo">
    <!-- mini logo for sidebar mini 50x50 pixels -->
    <span class="logo-mini"><b>D</b></span>
    <!-- logo for regular state and mobile devices -->
    <span class="logo-lg"><b>D</b>EMOCRACY</span>
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
            <span class="hidden-xs">${loginUser.email}</span>
          </a>
          <ul class="dropdown-menu">
            <li class="user-header">
              <img src="<c:url value="/images/noavatar.png"/>" class="img-circle" alt="User Image">
              <p>${loginUser.email}</p>
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

<c:if test="${loginUser.isAdmin()}">
  <aside class="main-sidebar">
    <section class="sidebar">
      <ul class="sidebar-menu" data-widget="tree">
        <li class="header">시민제안</li>
        <li>
          <a href="<c:url value="/admin/issue/proposal.do"/>">
            <i class="fa fa-picture-o"></i>
            <span>제안 관리</span>
          </a>
        </li>
        <li>
          <a href="<c:url value="/admin/issue/debate.do"/>">
            <i class="fa fa-picture-o"></i>
            <span>토론 관리</span>
          </a>
        </li>
        <li>
          <a href="<c:url value="/admin/issue/action.do"/>">
            <i class="fa fa-picture-o"></i>
            <span>실행 관리</span>
          </a>
        </li>

        <li class="header">기관제안</li>
        <li>
          <a href="<c:url value="/admin/issue/org-debate.do"/>">
            <i class="fa fa-picture-o"></i>
            <span>기관제안 관리</span>
          </a>
        </li>

        <li class="header">회원</li>
        <li>
          <a href="<c:url value="/admin/users/list.do"/>">
            <i class="fa fa-user"></i>
            <span>회원 관리</span>
          </a>
        </li>
        <li>
          <a href="<c:url value="/admin/users/manager.do"/>">
            <i class="fa fa-user"></i>
            <span>담당자 관리</span>
          </a>
        </li>

        <li class="header">게시판</li>
        <li>
          <a href="<c:url value="/admin/post/notice.do"/>">
            <i class="fa fa-calendar-check-o"></i>
            <span>공지사항 관리</span>
          </a>
        </li>

        <li class="header">카테고리</li>
        <li>
          <a href="<c:url value="/admin/category/list.do"/>">
            <i class="fa fa-calendar-check-o"></i>
            <span>카테고리 관리</span>
          </a>
        </li>

        <li class="header">Stats</li>
        <li>
          <a href="<c:url value="/admin/stats/issue.do"/>">
            <i class="fa fa-bar-chart"></i>
            <span>게시물 통계</span>
          </a>
        </li>
        <li>
          <a href="<c:url value="/admin/stats/opinion.do"/>">
            <i class="fa fa-bar-chart"></i>
            <span>댓글 통계</span>
          </a>
        </li>
      </ul>
    </section>
  </aside>
</c:if>
<c:if test="${loginUser.isManager()}">
  <aside class="main-sidebar">
    <section class="sidebar">
      <ul class="sidebar-menu" data-widget="tree">
        <li class="header">시민제안</li>
        <li>
          <a href="<c:url value="/admin/issue/proposal.do"/>">
            <i class="fa fa-picture-o"></i>
            <span>제안 관리</span>
          </a>
        </li>
      </ul>
    </section>
  </aside>
</c:if>
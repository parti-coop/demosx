<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>${action.title} - 민주주의 서울</title>
  <%@ include file="../shared/head.jsp" %>
</head>
<body class="home">
<%@ include file="../shared/header.jsp" %>

<div class="container">
  <h3 class="demo-detail-title">실행상세</h3>

  <div class="clearfix">
    <div class="demo-content">
      <div class="title-box">
        <h2 class="detail-title">${action.title}</h2>

        <div class="title-info clearfix">
          <div class="sns-group sns-group--left">
            <button href="" class="sns-btn" type="button"><i class="xi-facebook">
              <p class="alt-text">facebook</p>
            </i></button>
            <button href="" class="sns-btn" type="button"><i class="xi-kakaotalk">
              <p class="alt-text">kakaotalk</p>
            </i></button>
            <button href="" class="sns-btn" type="button"><i class="xi-twitter">
              <p class="alt-text">twitter</p>
            </i></button>
            <button href="" class="sns-btn" type="button"><i class="xi-blogger">
              <p class="alt-text">blogger</p>
            </i></button>
          </div>

        </div>
      </div>

      <div class="contents-box">
        <div class="contents-box__contents">${action.content}</div>

        <c:set var="issues" value="${action.viewProposals()}"/>
        <c:if test="${not empty issues}">
          <div class="relative-links">
            <h5 class="relative-title">연관제안</h5>
            <ul class="relative-link-ul">
              <c:forEach var="issue" items="${issues}">
                <li class="relative-link-li">
                  <a class="relative-link" href="<c:url value="/proposal.do?id=${issue.id}"/>">- ${issue.title}</a>
                </li>
              </c:forEach>
            </ul>
          </div>
        </c:if>

        <c:set var="issues" value="${action.viewDebates()}"/>
        <c:if test="${not empty issues}">
          <div class="relative-links">
            <h5 class="relative-title">연관토론</h5>
            <ul class="relative-link-ul">
              <c:forEach var="issue" items="${issues}">
                <li class="relative-link-li">
                  <a class="relative-link" href="<c:url value="/debate.do?id=${issue.id}"/>">- ${issue.title}</a>
                </li>
              </c:forEach>
            </ul>
          </div>
        </c:if>

        <c:if test="${not empty action.files}">
          <div class="attached-file-box">
            <h5 class="attached-file-box-title">첨부파일</h5>
            <ul class="attached-file-box-ul">
              <c:forEach var="file" items="${action.files}">
                <li>
                  <a href="${file.url}" download="${file.name}" target="_blank"><i class="xi-file"></i> ${file.name}</a>
                </li>
              </c:forEach>
            </ul>
          </div>
        </c:if>
      </div>

      <div class="to-list-btns text-center">
        <a class="d-btn white-btn" href="<c:url value="/action-list.do"/>">목록</a>
      </div>
    </div>

    <%@ include file="../shared/side.jsp" %>
  </div>
</div>

<%@ include file="../shared/footer.jsp" %>
</body>
</html>

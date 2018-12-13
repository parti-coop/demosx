<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>나의 투표 활동 - 민주주의 서울</title>
  <%@ include file="../shared/head.jsp" %>
</head>
<body class="home">
<%@ include file="../shared/header.jsp" %>

<div class="container">
  <h3 class="my-page-title">마이페이지</h3>
  <div class="m-tabs-mobile">
    <form>
      <div class="sorting-tab__select">
        <select class="form-control" name="mypage-link">
          <option value="/mypage/info.do">내 정보 수정</option>
          <option value="/mypage/proposal.do">나의 제안 활동</option>
          <option value="#" selected>나의 투표 활동</option>
          <option value="/mypage/opinion.do">나의 의견 활동</option>
        </select>
      </div>
    </form>
  </div>
  <ul class="my-page-tabs clearfix">
    <li class="my-page-tab"><a href="<c:url value="/mypage/info.do"/>" class="my-page-tab__link">내 정보 수정</a></li>
    <li class="my-page-tab"><a href="<c:url value="/mypage/proposal.do"/>" class="my-page-tab__link">나의 제안 활동</a></li>
    <li class="my-page-tab active"><a href="<c:url value="/mypage/vote.do"/>" class="my-page-tab__link">나의 투표 활동</a>
    </li>
    <li class="my-page-tab"><a href="<c:url value="/mypage/opinion.do"/>" class="my-page-tab__link">나의 의견 활동</a></li>
  </ul>

  <div class="clearfix">
    <div class="demo-content">
      <div class="row demo-search-top">
        <div class="col-sm-4">
          <p class="search-result-text">총 <strong>${opinions.totalElements}</strong>건의 활동이 있습니다.</p>
        </div>

        <div class="col-sm-8">
          <form>
            <div class="search-form-group">
              <label class="demo-form-label sr-only" for="inputSearch">검색</label>
              <input type="text" class="form-control demo-input" name="search" id="inputSearch"
                     autocomplete="off" placeholder="의견 내용을 검색해보세요." value="${search}">
              <button type="submit" class="search-submit-btn">
                <i class="xi-search">
                  <p class="sr-only">돋보기</p>
                </i>
              </button>
            </div>
          </form>
        </div>
      </div>

      <div class="demo-table">
        <div class="demo-row demo-row--th clearfix">
          <div class="demo-th">
            <p class="">제목 / 의견</p>
          </div>
          <div class="demo-th" style="width: 140px;">
            <p>투표여부</p>
          </div>
        </div>

        <c:forEach var="opinion" items="${opinions.content}">
          <div class="demo-row demo-row--vote clearfix">
            <div class="demo-td demo-td--text-left">
              <c:if test="${not opinion.issue.status.isOpen()}">
                <span class="block-list-li__ask-link">삭제된 제안입니다.</span>
              </c:if>
              <c:if test="${opinion.issue.status.isOpen()}">
                <c:choose>
                  <c:when test="${opinion.issue.type eq 'P'}">
                    <a href="<c:url value="/proposal.do?id=${opinion.issue.id}"/>"
                       class="block-list-li__ask-link">${opinion.issue.title}</a>
                  </c:when>
                  <c:when test="${opinion.issue.type eq 'D' and opinion.issue.group eq 'USER'}">
                    <a href="<c:url value="/debate.do?id=${opinion.issue.id}"/>"
                       class="block-list-li__ask-link">${opinion.issue.title}</a>
                  </c:when>
                  <c:when test="${opinion.issue.type eq 'D' and opinion.issue.group eq 'ORG'}">
                    <a href="<c:url value="/debate.do?id=${opinion.issue.id}"/>"
                       class="block-list-li__ask-link">${opinion.issue.title}</a>
                  </c:when>
                </c:choose>
              </c:if>
              <div class="reply-wrapper">
                <i class="xi-arrow-right reply-icon"></i>
                <ul class="my-sugest-ul">
                  <li class="my-sugest-li">
                    <p class="my-sugest-li__title">
                      <c:choose>
                        <c:when test="${opinion.vote eq 'YES'}">
                          <span class="my-sugest-li__title__status--agree">(찬성)</span>
                        </c:when>
                        <c:when test="${opinion.vote eq 'NO'}">
                          <span class="my-sugest-li__title__status--reject">(반대)</span>
                        </c:when>
                        <c:when test="${opinion.vote eq 'ETC'}">
                          <span class="my-sugest-li__title__status--etc">(기타)</span>
                        </c:when>
                      </c:choose>
                        ${opinion.contentWithBr()}</p>
                    <div class="demo-title-post-info">
                      <p class="td-post-info-p"><i class="xi-thumbs-up"></i> 공감 <strong>${opinion.likeCount}</strong>회
                      </p>
                      <p class="td-post-info-p"><i class="xi-clock"></i>${opinion.createdDate.toLocalDate()}</p>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
            <div class="demo-td demo-td--status hidden-xs">
              <c:choose>
                <c:when test="${opinion.vote eq 'YES'}"><p class="status-btn status-btn--agree">찬성</p></c:when>
                <c:when test="${opinion.vote eq 'NO'}"><p class="status-btn status-btn--reject">반대</p></c:when>
                <c:when test="${opinion.vote eq 'ETC'}"><p class="status-btn status-btn--etc">기타</p></c:when>
              </c:choose>
            </div>
          </div>
        </c:forEach>
      </div>

      <jsp:include page="../shared/pagination.jsp">
        <jsp:param name="totalPages" value="${opinions.totalPages}"/>
        <jsp:param name="current" value="${opinions.number + 1}"/>
      </jsp:include>
    </div>

    <%@include file="../shared/side.jsp" %>
  </div>
</div>
<script>
  $(function () {
    $('select[name=mypage-link]').change(function () {
      window.location.href = $(this).val();
    });
  });
</script>

<%@ include file="../shared/footer.jsp" %>
</body>
</html>

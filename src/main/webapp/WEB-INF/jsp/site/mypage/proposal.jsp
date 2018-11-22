<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>나의 제안 활동 - 민주주의 서울</title>
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
          <option value="#" selected>나의 제안 활동</option>
          <option value="/mypage/vote.do">나의 투표 활동</option>
          <option value="/mypage/opinion.do">나의 의견 활동</option>
        </select>
      </div>
    </form>
  </div>
  <ul class="my-page-tabs clearfix">
    <li class="my-page-tab"><a href="<c:url value="/mypage/info.do"/>" class="my-page-tab__link">내 정보 수정</a></li>
    <li class="my-page-tab active"><a href="<c:url value="/mypage/proposal.do"/>" class="my-page-tab__link">나의 제안 활동</a>
    </li>
    <li class="my-page-tab"><a href="<c:url value="/mypage/vote.do"/>" class="my-page-tab__link">나의 투표 활동</a></li>
    <li class="my-page-tab"><a href="<c:url value="/mypage/opinion.do"/>" class="my-page-tab__link">나의 의견 활동</a></li>
  </ul>

  <div class="clearfix">
    <div class="demo-content">
      <div class="row demo-search-top">
        <div class="col-sm-4">
          <p class="search-result-text">총 <strong>${proposals.totalElements}</strong>건의 활동이 있습니다.</p>
        </div>

        <div class="col-sm-8">
          <form>
            <div class="search-form-group">
              <label class="demo-form-label sr-only" for="inputSearch">검색</label>
              <input type="text" class="form-control demo-input" name="search" id="inputSearch"
                     autocomplete="off" placeholder="제목을 검색해보세요." value="${search}">
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
          <div class="demo-sm-l demo-th">
            <p class="">제목</p>
          </div>
          <div class="demo-sm-s demo-th">
            <p>진행현황</p>
          </div>
          <div class="demo-sm-s demo-th">
            <p class="">등록일</p>
          </div>
        </div>

        <c:forEach var="proposal" items="${proposals.content}">
          <div class="demo-row clearfix">
            <div class="demo-sm-l demo-td">
              <p class="demo-td__title">
                <a href="<c:url value="/proposal.do?id=${proposal.id}"/>"><span class="demo-td__title__status">(${proposal.process.msg})</span>${proposal.title}</a>
              </p>
              <div class="demo-title-post-info">
                <p class="td-post-info-p"><i class="xi-message"></i> 댓글 <strong>${proposal.stats.opinionCount}</strong>개</p>
                <p class="td-post-info-p"><i class="xi-thumbs-up"></i> 공감 <strong>${proposal.stats.likeCount}</strong>개</p>
                <p class="td-post-info-p"><i class="xi-eye-o"></i> 조회 <strong>${proposal.stats.viewCount}</strong>회</p>
                <p class="td-post-info-p td-post-info-p--date"><i class="xi-clock"></i> ${proposal.createdDate.toLocalDate()}</p>
              </div>
            </div>
            <div class="demo-sm-s demo-td hidden-xs">
              <p class="demo-td__status">${proposal.process.msg}</p>
            </div>
            <div class="demo-sm-s demo-td hidden-xs">
              <p class="">${proposal.createdDate.toLocalDate()}</p>
            </div>
          </div>
        </c:forEach>
      </div>

      <jsp:include page="../shared/pagination.jsp">
        <jsp:param name="totalPages" value="${proposals.totalPages}"/>
        <jsp:param name="current" value="${proposals.number + 1}"/>
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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>토론 - 민주주의 서울</title>
  <%@ include file="../shared/head.jsp" %>
</head>
<body class="home">
<%@ include file="../shared/header.jsp" %>

<div class="container">
  <div class="top-row clearfix">
    <div class="top-left">
      <h3 class="top-row__title">토론</h3>
    </div>
  </div>

  <div class="sorting-tab sorting-tab--discussion clearfix">
    <form name="debate-search">
      <div class="sorting-tab__select">
        <select class="form-control" name="process">
          <option value="">전체보기</option>
          <option value="INIT" <c:if test="${process eq 'INIT'}">selected</c:if>>진행예정</option>
          <option value="PROGRESS" <c:if test="${process eq 'PROGRESS'}">selected</c:if>>진행중</option>
          <option value="COMPLETE" <c:if test="${process eq 'COMPLETE'}">selected</c:if>>진행완료</option>
        </select>
      </div>
      <div class="sorting-tab__select sorting-tab__select--last">
        <select class="form-control" name="category">
          <option value="">분류</option>
          <c:forEach var="item" items="${categories}">
            <option value="${item.name}" <c:if test="${category eq item.name}">selected</c:if>>${item.name}</option>
          </c:forEach>
        </select>
      </div>
      <div class="sorting-right-form">
        <div class="sorting-right-form__group">
          <label class="demo-form-label sr-only" for="inputSearch">이름</label>
          <input type="text" class="form-control demo-input" name="search" id="inputSearch" placeholder="제목을 검색해보세요."
                 value="${search}">
          <button type="submit" class="search-submit-btn">
            <i class="xi-search"><span class="sr-only">돋보기</span></i>
          </button>
        </div>
      </div>
    </form>
  </div>
  <div class="discussion-mention">
    <p class="discussion-mention-p">“<span>5000</span>명"이 참여한 토론은<br class="visible-xs"/> 시장이 직접 답변합니다.</p>
  </div>

  <div class="l-img-card-wrapper">
    <c:forEach var="item" items="${page.content}">
      <div class="l-img-card">
        <a href="<c:url value="/debate.do?id=${item.id}"/>" class="l-img-card__link">
          <div class="l-img-card__img bg-img" style="background-image: url(${item.thumbnail})">
            <p class="sr-only">${item.title} 썸네일</p>
          </div>
          <div class="l-img-card__contents">
            <p class="l-img-card__status">${item.opinionType.msg}-${item.process.msg}</p>
            <h5 class="demo-card__title">${item.title}</h5>
            <p class="demo-card__desc">${item.excerpt}</p>
            <div class="demo-card__info demo-card__info--discussion">
              <p class="demo-card__info__p">
                <i class="xi-thumbs-up"></i> 참여자 <strong>${item.stats.applicantCount}</strong>명
              </p>
              <c:if test="${item.opinionType == 'PROPOSAL'}">
                <p class="demo-card__info__p">
                  <i class="xi-message"></i> 댓글 <strong>${item.stats.opinionCount}</strong>개
                </p>
              </c:if>
              <p class="demo-card__info__p demo-card__info__p--m-block">
                <i class="xi-clock"></i> ${item.startDate} ~ ${item.endDate}
              </p>
            </div>
          </div>
        </a>
      </div>
    </c:forEach>
  </div>

  <jsp:include page="../shared/pagination.jsp">
    <jsp:param name="totalPages" value="${page.totalPages}"/>
    <jsp:param name="current" value="${page.number + 1}"/>
  </jsp:include>
</div>

<script>
  $(function () {
    var $debateSearch = $('form[name=debate-search]');
    $('select[name=process]').change(function () {
      $debateSearch.submit();
    });
    $('select[name=category]').change(function () {
      $debateSearch.submit();
    });
  });
</script>

<%@ include file="../shared/footer.jsp" %>
</body>
</html>

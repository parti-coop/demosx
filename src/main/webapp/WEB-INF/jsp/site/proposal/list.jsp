<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>제안 - 민주주의 서울</title>
  <%@ include file="../shared/head.jsp" %>
</head>
<body class="home">
<%@ include file="../shared/header.jsp" %>

<div class="container">
  <div class="top-row clearfix">
    <div class="top-left">
      <h3 class="top-row__title">제안</h3>
    </div>
    <div class="top-right hidden-xs">
      <a href="" class="btn demo-btn demo-btn--primary d-btn-lg btn-block">제안하기 <i class="xi-angle-right"></i></a>
    </div>
    <div class="m-bottom-btn-group clearfix">
      <a href="" class="btn d-btn btn-blue btn-block">
        제안하기
      </a>
    </div>
  </div>

  <div class="sorting-tab sorting-tab--suggest clearfix">
    <form name="search">
      <ul class="sorting-tab__ul clearfix hidden-sm hidden-xs">
        <li class="sorting-tab__li <c:if test="${sort eq 'latest'}">active</c:if>">
          <a href="<c:url value="/proposal-list.do?sort=latest&category=${category}&search=${search}"/>" class="sorting-tab__li__link">최신순</a>
        </li>
        <li class="sorting-tab__li <c:if test="${sort eq 'like'}">active</c:if>">
          <a href="<c:url value="/proposal-list.do?sort=like&category=${category}&search=${search}"/>" class="sorting-tab__li__link">공감순</a>
        </li>
        <li class="sorting-tab__li <c:if test="${sort eq 'opinion'}">active</c:if>">
          <a href="<c:url value="/proposal-list.do?sort=opinion&category=${category}&search=${search}"/>" class="sorting-tab__li__link">의견 많은 순</a>
        </li>
      </ul>
      <div class="sorting-tab-select-row">
        <div class="demo-col-6 visible-sm visible-xs">
          <div class="m-tabs-mobile">
            <div class="sorting-tab__select">
              <select class="form-control" name="sort">
                <option value="latest" <c:if test="${sort eq 'latest'}">selected</c:if>>최신순</option>
                <option value="like" <c:if test="${sort eq 'like'}">selected</c:if>>공감순</option>
                <option value="opinion" <c:if test="${sort eq 'opinion'}">selected</c:if>>의견 많은 순</option>
              </select>
            </div>
          </div>
        </div>
        <div class="demo-col-6">
          <div class="sorting-tab__select">
            <select class="form-control" name="category">
              <option value="">분류</option>
              <c:forEach var="item" items="${categories}">
                <option value="${item.name}" <c:if test="${category eq item.name}">selected</c:if>>${item.name}</option>
              </c:forEach>
            </select>
          </div>
        </div>
      </div>

      <div class="sorting-right-form">
        <div class="sorting-right-form__group">
          <label class="demo-form-label sr-only" for="inputSearch">이름</label>
          <input type="text" class="form-control demo-input" id="inputSearch" name="search" placeholder="제목을 검색해보세요."
                 value="${search}">
          <button type="submit" class="search-submit-btn">
            <i class="xi-search"><span class="sr-only">돋보기</span></i>
          </button>
        </div>
      </div>
    </form>
  </div>

  <div class="card-wrapper">
    <div class="row">
      <c:forEach var="proposal" items="${page.content}">
        <div class="col-sm-6 demo-card-wrapper">
          <div class="demo-card">
            <a href="<c:url value="/proposal.do?id=${proposal.id}"/>" class="demo-card__link">
              <div class="demo-card__author">
                <div class="profile-circle profile-circle--title"
                     style="background-image: url(${proposal.createdBy.viewPhoto()})">
                  <p class="alt-text">${proposal.createdBy.name}프로필</p>
                </div>
                <p class="title-author__name">${proposal.createdBy.name}</p>
                <p class="title-author__date"><i class="xi-time"></i> ${proposal.createdDate.toLocalDate()}</p>
              </div>
              <div class="demo-card__contents">
                <h5 class="demo-card__title">${proposal.title}</h5>
                <p class="demo-card__desc">${proposal.excerpt}</p>
              </div>

              <div class="demo-card__info">
                <p class="demo-card__info__p"><i class="xi-thumbs-up"></i> 공감
                  <strong>${proposal.stats.likeCount}</strong>개</p>
                <p class="demo-card__info__p"><i class="xi-message"></i> 댓글
                  <strong>${proposal.stats.opinionCount}</strong>개</p>
              </div>

              <div class="demo-progress">
                <div class="progress-container">
                  <div class="progress-thumb-wrapper" style="margin-left: ${proposal.stats.likePercentBy500()}%;">
                    <img class="progress-thumb-img" src="<c:url value="/images/progress-thumb.png"/>">
                  </div>
                  <div class="progress-bg">
                    <div class="progress-fill-bar" style="width: ${proposal.stats.likePercentBy500()}%;"></div>
                    <div class="progress-step-1" style="left: 10%">
                      <div class="progress-step-1-text">50명</div>
                    </div>
                    <div class="progress-step-1-line" style="left: 10%;"></div>
                    <p class="progress-step-2">500명</p>
                  </div>
                </div>
              </div>
            </a>
          </div>
        </div>
      </c:forEach>
    </div>
  </div>

  <jsp:include page="../shared/pagination.jsp">
    <jsp:param name="totalPages" value="${page.totalPages}"/>
    <jsp:param name="current" value="${page.number + 1}"/>
  </jsp:include>
</div>

<script>
  $(function () {
    $('select[name=category]').change(function () {
      $('form[name=search]').submit();
    });
    $('select[name=sort]').change(function () {
      $('form[name=search]').submit();
    });
  });
</script>

<%@ include file="../shared/footer.jsp" %>
</body>
</html>

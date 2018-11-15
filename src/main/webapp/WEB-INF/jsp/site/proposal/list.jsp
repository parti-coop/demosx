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
  <div class="main-top-search clearfix">
    <div class="main-top-search__left">
      <form action="<c:url value="/proposal-list.do"/>">
        <div class="search-form-group search-form-group--lg">
          <label class="demo-form-label sr-only" for="inputSearch">이름</label>
          <input type="text" class="form-control demo-input" name="search" id="inputSearch" autocomplete="off"
                 placeholder="제목을 검색해보세요.">
          <button type="submit" class="search-submit-btn">
            <i class="xi-search"><p class="sr-only">돋보기</p></i>
          </button>
        </div>
      </form>
    </div>
    <div class="main-top-search__right">
      <a href="" class="btn demo-btn demo-btn--primary d-btn-lg btn-block">시민제안<i class="xi-angle-right"></i></a>
    </div>
  </div>
  <div class="row demo-search-top">
    <div class="col-sm-6 col-sm-offset-6">

    </div>
  </div>
  <div class="main-card-wrapper">
    <div class="row">
      <c:forEach var="proposal" items="${page.content}">
        <div class="col-sm-6 demo-card-wrapper">
          <div class="demo-card">
            <div class="demo-card__author">
              <div class="profile-circle profile-circle--title" style="background-image: url(${proposal.createdBy.viewPhoto()})">
                <p class="alt-text">${proposal.createdBy.name}사진</p>
              </div>
              <p class="title-author__name">${proposal.createdBy.name}</p>
              <p class="title-author__date"><i class="xi-time"></i> ${proposal.createdDate.toLocalDate()}</p>
            </div>
            <h5 class="demo-card__title"><a href="/proposal.do?id=${proposal.id}">${proposal.title}</a></h5>
            <p class="demo-card__desc">${proposal.excerpt}</p>
            <div class="demo-card__info">
              <p class="demo-card__info__p"><i class="xi-thumbs-up"></i> 공감 <strong>${proposal.stats.viewLikeCount()}</strong>개</p>
              <p class="demo-card__info__p"><i class="xi-message"></i> 댓글 <strong>${proposal.stats.viewOpinionCount()}</strong>개</p>
            </div>

            <div class="demo-progress">
              <div class="progress-container">
                <div class="progress-thumb-wrapper" style="margin-left: ${proposal.stats.likePercentBy500()}%;">
                  <img class="progress-thumb-img" src="/images/progress-thumb.png">
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
          </div>
        </div>
      </c:forEach>
      <div class="col-sm-6 demo-card-wrapper">
        <div class="demo-card">
          <div class="demo-card__author">
            <div class="profile-circle profile-circle--title" style="background-image: url('./img/hong-people.png')">
              <p class="alt-text">홍길동프로필</p>
            </div>
            <p class="title-author__name">홍길동</p>
            <p class="title-author__date"><i class="xi-time"></i> 2018. 10. 22</p>
          </div>
          <h5 class="demo-card__title">
            노인분들을 위해 무료로 따릉이를<br/>
            이용할 수 있는 따릉이 복지카드를 만들어 주세요!(최대 2줄)
          </h5>
          <p class="demo-card__desc">노년층을 위한 대중교통 무료 정책처럼 따릉이를 이용하고 싶지만 그 방법이 어려워 ... (최대 2줄)</p>
          <div class="demo-card__info">
            <p class="demo-card__info__p"><i class="xi-thumbs-up"></i> 공감 <strong>15</strong>개</p>
            <p class="demo-card__info__p"><i class="xi-message"></i> 댓글 <strong>15</strong>개</p>
          </div>

          <div class="demo-progress">
            <div class="progress-container">
              <div class="progress-thumb-wrapper" style="margin-left: 2%;">
                <img class="progress-thumb-img" src="./img/progress-thumb.png">
              </div>
              <div class="progress-bg">
                <div class="progress-fill-bar" style="width: 2%;"></div>
                <div class="progress-step-1" style="left: 10%">
                  <div class="progress-step-1-text">50명</div>
                </div>
                <div class="progress-step-1-line" style="left: 10%;"></div>
                <p class="progress-step-2">500명</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="col-sm-6 demo-card-wrapper">
        <div class="demo-card">
          <div class="demo-card__author">
            <div class="profile-circle profile-circle--title" style="background-image: url('./img/hong-people.png')">
              <p class="alt-text">홍길동프로필</p>
            </div>
            <p class="title-author__name">홍길동</p>
            <p class="title-author__date"><i class="xi-time"></i> 2018. 10. 22</p>
          </div>
          <h5 class="demo-card__title">
            노인분들을 위해 무료로 따릉이를<br/>
            이용할 수 있는 따릉이 복지카드를 만들어 주세요!(최대 2줄)
          </h5>
          <p class="demo-card__desc">노년층을 위한 대중교통 무료 정책처럼 따릉이를 이용하고 싶지만 그 방법이 어려워 ... (최대 2줄)</p>
          <div class="demo-card__info">
            <p class="demo-card__info__p"><i class="xi-thumbs-up"></i> 공감 <strong>15</strong>개</p>
            <p class="demo-card__info__p"><i class="xi-message"></i> 댓글 <strong>15</strong>개</p>
          </div>

          <div class="demo-progress">
            <div class="progress-container">
              <div class="progress-thumb-wrapper" style="margin-left: 2%;">
                <img class="progress-thumb-img" src="./img/progress-thumb.png">
              </div>
              <div class="progress-bg">
                <div class="progress-fill-bar" style="width: 2%;"></div>
                <div class="progress-step-1" style="left: 10%">
                  <div class="progress-step-1-text">50명</div>
                </div>
                <div class="progress-step-1-line" style="left: 10%;"></div>
                <p class="progress-step-2">500명</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div><!-- card .row END -->
  </div><!-- .main-card-wrapper END -->

  <jsp:include page="../shared/pagination.jsp">
    <jsp:param name="totalPages" value="${page.totalPages}"/>
    <jsp:param name="current" value="${page.number + 1}"/>
  </jsp:include>
</div>

<%@ include file="../shared/footer.jsp" %>
</body>
</html>

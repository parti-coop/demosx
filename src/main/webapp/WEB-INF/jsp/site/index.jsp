<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>민주주의 서울</title>
  <%@ include file="./shared/head.jsp" %>

  <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/Swiper/4.4.2/css/swiper.min.css"/>
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Swiper/4.4.2/js/swiper.min.js"></script>
</head>
<body class="home header-no-margin">
<%@ include file="./shared/header.jsp" %>

<div class="container">

  <div class="main-slide-wrapper">
    <div class="swiper-container">
      <div class="swiper-wrapper">
        <div class="swiper-slide">
          <img src="/images/main-slider1.jpg" class="hidden-xs" alt="메인 슬라이더1">
          <img src="/images/mobile-slider1.jpg" class="visible-xs" alt="메인 슬라이더1">
          <div class="slide-text-wrapper">
            <p>민주주의 ㅇㅇ은<br>시민과 기관이 함께 정책에 대해<br class="visible-xs"> 의견을 나누고<br class="hidden-xs">함께 실행하는<br
                class="visible-xs"> 시민참여 플랫폼입니다.</p>
            <a href="<c:url value="/intro.do"/>" class="btn demo-btn demo-btn--primary">자세히보기</a>
          </div>
        </div>
        <div class="swiper-slide">
          <img src="/images/main-slider2.jpg" class="hidden-xs" alt="메인 슬라이더2">
          <img src="/images/mobile-slider2.jpg" class="visible-xs" alt="메인 슬라이더2">
          <div class="slide2-text-wrapper">
            <p>민주주의 ㅇㅇ은<br>시민과 기관이 함께 정책에 대해<br class="visible-xs"> 의견을 나누고<br class="hidden-xs">함께 실행하는<br
                class="visible-xs"> 시민참여 플랫폼입니다.</p>
            <a href="<c:url value="/intro.do"/>" class="btn demo-btn demo-btn--primary">자세히보기</a>
          </div>
        </div>
      </div>

      <div class="swiper-pagination">
        <div class="swiper-pagination-wrapper"></div>
      </div>
      <div class="swiper-button-prev"></div>
      <div class="swiper-button-next"></div>
    </div>
  </div>

  <div class="top-search clearfix">
    <div class="top-search__left">
      <form action="<c:url value="/proposal-list.do"/>">
        <div class="search-form-group search-form-group--lg">
          <label class="demo-form-label sr-only" for="inputSearch">이름</label>
          <input type="text" class="form-control demo-input" name="search" id="inputSearch" autocomplete="off"
                 placeholder="어떤 생각이나 주제에 관심이 있으시나요?">
          <button type="submit" class="search-submit-btn">
            <i class="xi-search"><p class="sr-only">돋보기</p></i>
          </button>
        </div>
      </form>
    </div>
    <div class="top-search__right">
      <a href="<c:url value="/new-proposal.do"/>" class="btn demo-btn demo-btn--primary d-btn-lg btn-block">시민제안<i
          class="xi-angle-right"></i></a>
    </div>
  </div>
  <div class="main-card-wrapper">
    <div class="row">
      <c:forEach var="item" items="${best.content}">
        <c:set var="proposal" value="${item}" scope="request"/>
        <div class="col-sm-6 demo-card-wrapper">
          <jsp:include page="./proposal/card.jsp">
            <jsp:param name="best" value="true"/>
          </jsp:include>
        </div>
      </c:forEach>
      <c:forEach var="item" items="${favorite.content}">
        <c:set var="proposal" value="${item}" scope="request"/>
        <div class="col-sm-6 demo-card-wrapper">
          <jsp:include page="./proposal/card.jsp"/>
        </div>
      </c:forEach>
      <c:forEach var="item" items="${latest.content}">
        <c:set var="proposal" value="${item}" scope="request"/>
        <div class="col-sm-6 demo-card-wrapper">
          <jsp:include page="./proposal/card.jsp"/>
        </div>
      </c:forEach>
    </div>
  </div>

  <div class="show-more-container text-center">
    <a class="white-btn d-btn btn-more" href="<c:url value="/proposal-list.do"/>">더보기<i
        class="xi-angle-down-min"></i></a>
  </div>
</div>

<script>
  $(function () {
    new Swiper('.swiper-container', {
      // Optional parameters
      //direction: 'vertical',
      loop: true,
      autoplay: {
        delay: 4000
      },
      pagination: {
        el: '.swiper-pagination',
        clickable: true
      },
      navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev'
      }
    });
  });
</script>

<%@ include file="./shared/footer.jsp" %>
</body>
</html>

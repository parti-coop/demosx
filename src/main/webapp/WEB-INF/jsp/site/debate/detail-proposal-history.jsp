<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>${debate.title} - 민주주의 서울</title>
  <%@ include file="../shared/head.jsp" %>
</head>
<body class="home">
<%@ include file="../shared/header.jsp" %>

<div class="container" id="discuss-top-info">
  <h3 class="demo-detail-title">토론보기</h3>

  <div class="img-box-top clearfix">
    <div class="img-box-top__img">
      <img class="img-block" src="${debate.thumbnail}"/>
    </div>

    <div class="img-box-top__contents">
      <h2 class="detail-title">${debate.title}</h2>

      <div class="sns-group sns-group--img-box-top clearfix">
        <button class="sns-btn sns-btn--trigger collapsed" type="button" data-toggle="collapse"
                data-target="#sns-collapse"
                aria-expanded="false" aria-controls="sns-collapse"><i class="xi-share-alt">
          <p class="alt-text">share-open</p>
        </i></button>
        <div class="collapse collapse-sns" id="sns-collapse">
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

      <p class="img-box-top__date">투표기간 : <br class="visible-xs"/>${debate.startDate} ~ ${debate.endDate}</p>

      <div class="demo-progress">
        <div class="progress-info clearfix">
          <div class="progress-info__left">
            <p class="progress-info__count">
              <i class="xi-user-plus"></i> 참여자 <strong>${debate.stats.applicantCount}</strong>명 •
              <i class="xi-message"></i> 댓글 <strong>${debate.stats.opinionCount}</strong>개
            </p>
          </div>
        </div>
      </div>

      <div class="discuss-btn-group clearfix">
        <button type="button" class="btn d-btn btn-white btn-block suggest-click-btn">의견남기기 <i
            class="xi-angle-right"></i></button>
      </div>
    </div>
  </div>
</div>

<div class="middle-nav-tab" id="middle-nav-tab">
  <div class="container">
    <div class="middle-nav clearfix">
      <ul class="sorting-tab__ul clearfix">
        <li class="sorting-tab__li">
          <a href="<c:url value="/debate.do?id=${debate.id}#middle-nav-tab"/>" class="sorting-tab__li__link">내용</a>
        </li>
        <li class="sorting-tab__li active">
          <a href="#middle-nav-tab" class="sorting-tab__li__link">히스토리</a>
        </li>
      </ul>
    </div>

  </div>
</div>

<div class="middle-nav-tab middle-nav-tab--scroll" id="middle-nav-tab-scroll">
  <div class="container">
    <div class="middle-nav clearfix">
      <ul class="sorting-tab__ul clearfix">
        <li class="sorting-tab__li">
          <a href="<c:url value="/debate.do?id=${debate.id}#middle-nav-tab"/>" class="sorting-tab__li__link">내용</a>
        </li>
        <li class="sorting-tab__li active">
          <a href="#middle-nav-tab" class="sorting-tab__li__link">히스토리</a>
        </li>
      </ul>
      <div class="middle-btn-group">
        <button type="button" class="btn d-btn btn-white suggest-click-btn">
          의견남기기
        </button>
      </div>
    </div>
  </div>
</div>

<div class="scroll-bottom-btn-group clearfix" id="bottom-discuss-btn">
  <button type="button" class="btn d-btn btn-white btn-block suggest-click-btn">
    의견남기기
  </button>
</div>

<
<div class="container discussion-contents-container" id="suggest-scroll-position">
  <div class="clearfix">
    <div class="demo-content">
      <button class="btn d-btn btn-white btn-block suggest-click-btn" data-toggle="modal" data-target="#myModal">
        새로운 히스토리 작성 <i class="xi-angle-right"></i>
      </button>

      <div class="demo-comments-container">
        <c:if test="${empty histories}">
          <h5 class="discuss-title">작성된 히스토리가 없습니다.</h5>
        </c:if>
        <c:if test="${not empty histories}">
          <ul class="demo-comments">
            <c:forEach var="history" items="${histories}">
              <li class="comment-li">
                <div class="profile-circle profile-circle--comment"
                     style="background-image: url(${history.createdBy.viewPhoto()})">
                  <p class="alt-text">${history.createdBy.name}프로필</p>
                </div>
                <div class="comment-content">
                  <p class="comment-name">${history.createdBy.name}</p>
                  <div class="comment-info clearfix">
                    <div class="comment-date-wrapper">
                      <p class="comment-time"><i class="xi-time"></i> ${history.createdDate.toLocalDate()}</p>
                    </div>
                    <div class="comment-likes-count">
                      <p class="comment-thumbs-count"><i class="xi-thumbs-up"></i> 공감 <strong>12</strong>개</p>
                    </div>
                  </div>
                  <p class="comment-content-text">${history.content}</p>
                </div>
              </li>
            </c:forEach>
          </ul>
        </c:if>
      </div>
    </div>

    <%@ include file="../shared/side.jsp" %>
  </div><!-- demo-row end  -->
</div>

<script>
  $(function () {

    $('.suggest-click-btn').click(function () {
      $('html, body').animate({
        scrollTop: $('#suggest-scroll-position').offset().top - 80
      }, 300);
    });

    function listener() {
      var elementOffset = $('#middle-nav-tab').offset().top;
      console.log('top_height', elementOffset);
      if ($(window).scrollTop() < elementOffset) {
        $('#middle-nav-tab-scroll').css({ display: 'none' });
        $('#bottom-discuss-btn').css({ display: 'none' });
        if ($(window).width() < 768) {
          $('#bottom-discuss-btn').css({ display: 'none' });
          $('#bottom-height-div').css({ display: 'none' });
        }
      } else {
        $('#middle-nav-tab-scroll').css({ display: 'block' });
        if ($(window).width() < 768) {
          $('#bottom-discuss-btn').css({ display: 'block' });
          $('#bottom-height-div').css({ display: 'block' });
        } else {
          $('#bottom-discuss-btn').css({ display: 'none' });
          $('#bottom-height-div').css({ display: 'none' });
        }

      }
    }

    listener();
    $(window).scroll(listener);
    $(window).resize(listener);
  });
</script>

<%@ include file="../shared/footer.jsp" %>
</body>
</html>

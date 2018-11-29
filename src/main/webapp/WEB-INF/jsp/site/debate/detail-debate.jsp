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
        <div class="progress-container">
          <div class="progress-bg progress-bg--discussion">
            <div class="progress-fill-bar progress-fill-bar--blue"
                 style="width: ${debate.stats.yesPercentToDouble()}%;"></div>
            <div class="progress-fill-bar progress-fill-bar--grey"
                 style="width: ${debate.stats.etcPercentToDouble()}%;"></div>
            <div class="progress-fill-bar progress-fill-bar--red"
                 style="width: ${debate.stats.noPercentToDouble()}%;"></div>
          </div>
        </div>
        <div class="progress-info clearfix">
          <div class="progress-info__left">
            <p class="progress-info__count">
              <i class="xi-user-plus"></i> 참여자 <strong>${debate.stats.applicantCount}</strong>명
            </p>
          </div>
          <div class="progress-info__right">
            <p class="progress-info__text">
              <sapn class="progress-blue">찬성(${debate.stats.yesPercent()}%)</sapn>
              <span class="progress-grey"> • 기타(${debate.stats.etcPercent()}%) • </span>
              <span class="progress-red">반대(${debate.stats.noPercent()}%)</span>
            </p>
          </div>
        </div>
      </div>

      <div class="discuss-btn-group clearfix">
        <div class="middle-btn-wrapper">
          <button type="button" class="btn d-btn d-btn--discuss btn-blue show-opinion-modal" data-vote="YES">
            찬성합니다
          </button>
        </div>
        <div class="middle-btn-wrapper">
          <button type="button" class="btn d-btn d-btn--discuss btn-grey show-opinion-modal" data-vote="ETC">
            기타의견입니다
          </button>
        </div>
        <div class="middle-btn-wrapper">
          <button type="button" class="btn d-btn d-btn--discuss btn-red show-opinion-modal" data-vote="NO">
            반대합니다
          </button>
        </div>
      </div>
    </div>
  </div>
</div>

<div class="middle-nav-tab" id="middle-nav-tab">
  <div class="container">
    <div class="middle-nav clearfix">
      <ul class="sorting-tab__ul clearfix">
        <li class="sorting-tab__li active"><a href="#middle-nav-tab" class="sorting-tab__li__link">내용</a></li>
        <li class="sorting-tab__li">
          <a href="<c:url value="/debate-history.do?id=${debate.id}#middle-nav-tab"/>" class="sorting-tab__li__link">히스토리</a>
        </li>
      </ul>
    </div>

  </div>
</div>

<div class="middle-nav-tab middle-nav-tab--scroll" id="middle-nav-tab-scroll">
  <div class="container">
    <div class="middle-nav clearfix">
      <ul class="sorting-tab__ul clearfix">
        <li class="sorting-tab__li active"><a href="#middle-nav-tab" class="sorting-tab__li__link">내용</a></li>
        <li class="sorting-tab__li">
          <a href="<c:url value="/debate-history.do?id=${debate.id}#middle-nav-tab"/>" class="sorting-tab__li__link">히스토리</a>
        </li>
      </ul>
      <div class="middle-btn-group">
        <button type="button" class="btn d-btn btn-blue show-opinion-modal" data-vote="YES">찬성합니다</button>
        <button type="button" class="btn d-btn btn-grey show-opinion-modal" data-vote="ETC">기타의견입니다</button>
        <button type="button" class="btn d-btn btn-red show-opinion-modal" data-vote="NO">반대합니다</button>
      </div>
    </div>
  </div>
</div>

<div class="scroll-bottom-btn-group clearfix" id="bottom-discuss-btn">
  <div class="middle-btn-wrapper middle-btn-wrapper--ok">
    <button type="button" class="btn d-btn btn-blue show-opinion-modal" data-vote="YES">찬성합니다</button>
  </div>
  <div class="middle-btn-wrapper middle-btn-wrapper--etc">
    <button type="button" class="btn d-btn btn-grey show-opinion-modal" data-vote="ETC">기타의견입니다</button>
  </div>
  <div class="middle-btn-wrapper middle-btn-wrapper--no">
    <button type="button" class="btn d-btn btn-red show-opinion-modal" data-vote="NO">반대합니다</button>
  </div>
</div>

<div class="container discussion-contents-container">
  <div class="clearfix">
    <div class="demo-content">
      <div class="contents-box contents-box--discuss">
        <h5 class="discuss-title">${debate.title}</h5>
        <div class="contents-box__contents">${debate.content}</div>

        <c:set var="issues" value="${devate.viewProposals()}"/>
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

        <c:if test="${not empty debate.files}">
          <div class="attached-file-box">
            <h5 class="attached-file-box-title">첨부파일</h5>
            <ul class="attached-file-box-ul">
              <c:forEach var="file" items="${debate.files}">
                <li>
                  <a href="${file.url}" download="${file.name}" target="_blank"><i class="xi-file"></i> ${file.name}</a>
                </li>
              </c:forEach>
            </ul>
          </div>
        </c:if>
      </div>

      <div class="discuss-status-box">
        <p class="discuss-status-box__title"><strong>투표현황</strong> (참여자: <strong>${debate.stats.applicantCount}</strong>명)</p>
        <div class="demo-progress">
          <div class="progress-container">
            <div class="progress-bg progress-bg--discussion">
              <div class="progress-fill-bar progress-fill-bar--blue" style="width: ${debate.stats.yesPercentToDouble()}%;"></div>
              <div class="progress-fill-bar progress-fill-bar--grey" style="width: ${debate.stats.etcPercentToDouble()}%;"></div>
              <div class="progress-fill-bar progress-fill-bar--red" style="width: ${debate.stats.noPercentToDouble()}%;"></div>
            </div>
          </div>
          <div class="discuss-progress-info clearfix">
            <div class="progress-info__right">
              <p class="progress-info__text">
                <sapn class="progress-blue">찬성 ${debate.stats.yesCount}표(${debate.stats.yesPercent()}%)</sapn>
                <span class="progress-grey"> • 기타 ${debate.stats.etcCount}표(${debate.stats.etcPercent()}%) • </span>
                <span class="progress-red">반대 ${debate.stats.noCount}표(${debate.stats.noPercent()}%)</span>
              </p>
            </div>
          </div>
        </div>

        <div class="discuss-status-box__buttons">
          <button type="button" class="btn d-btn d-btn--discuss btn-blue show-opinion-modal" data-vote="YES">
            찬성합니다
          </button>
          <button type="button" class="btn d-btn d-btn--discuss btn-grey show-opinion-modal" data-vote="ETC">
            기타의견입니다
          </button>
          <button type="button" class="btn d-btn d-btn--discuss btn-red show-opinion-modal" data-vote="NO">
            반대합니다
          </button>
        </div>
      </div>

      <jsp:include page="../opinion/debate.jsp">
        <jsp:param name="id" value="${debate.id}"/>
        <jsp:param name="closed" value="${debate.process eq 'INIT' or debate.process eq 'COMPLETE'}"/>
      </jsp:include>

    </div>

    <%@ include file="../shared/side.jsp" %>
  </div>
</div>

<script>
  $(function () {
    function listener() {
      var elementOffset = $('#middle-nav-tab').offset().top;
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

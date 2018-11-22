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
                 style="width: ${debate.stats.yesPercent()}%;"></div>
            <div class="progress-fill-bar progress-fill-bar--grey"
                 style="width: ${debate.stats.etcPercent()}%;"></div>
            <div class="progress-fill-bar progress-fill-bar--red" style="width: ${debate.stats.noPercent()}%;"></div>
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
          <button type="button" class="btn d-btn d-btn--discuss btn-blue" data-toggle="modal" data-target="#myModal">
            찬성합니다
          </button>
        </div>
        <div class="middle-btn-wrapper">
          <button type="button" class="btn d-btn d-btn--discuss btn-grey" data-toggle="modal" data-target="#myModal">
            기타의견입니다
          </button>
        </div>
        <div class="middle-btn-wrapper">
          <button type="button" class="btn d-btn d-btn--discuss btn-red" data-toggle="modal" data-target="#myModal">
            반대합니다
          </button>
        </div>
      </div>

      <!-- modal START-->
      <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-body">
              <form class="demo-form">
                <div class="form-input-container form-input-container--history">
                  <div class="form-group form-group--demo">
                    <label class="demo-form-label" for="inputContent">의견</label>
                    <textarea class="form-control" id="inputContent" rows="10"></textarea>
                  </div>
                </div>
                <div class="form-action text-right">
                  <div class="btn-group clearfix">
                    <button class="btn demo-submit-btn cancel-btn" data-dismiss="modal"
                            aria-label="Close" href="" role="button">취소
                    </button>
                    <button type="submit" class="demo-submit-btn demo-submit-btn--submit">저장하기</button>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
      <!-- modal END -->
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
        <button type="button" class="btn d-btn btn-blue" data-toggle="modal" data-target="#myModal">찬성합니다</button>
        <button type="button" class="btn d-btn btn-grey" data-toggle="modal" data-target="#myModal">기타의견입니다</button>
        <button type="button" class="btn d-btn btn-red" data-toggle="modal" data-target="#myModal">반대합니다</button>
      </div>
    </div>
  </div>
</div>

<div class="scroll-bottom-btn-group clearfix" id="bottom-discuss-btn">
  <div class="middle-btn-wrapper middle-btn-wrapper--ok">
    <button type="button" class="btn d-btn btn-blue" data-toggle="modal" data-target="#myModal">
      찬성합니다
    </button>
  </div>
  <div class="middle-btn-wrapper middle-btn-wrapper--etc">
    <button type="button" class="btn d-btn btn-grey" data-toggle="modal" data-target="#myModal">
      기타의견입니다
    </button>
  </div>
  <div class="middle-btn-wrapper middle-btn-wrapper--no">
    <button type="button" class="btn d-btn btn-red" data-toggle="modal" data-target="#myModal">
      반대합니다
    </button>
  </div>
</div>

<div class="container discussion-contents-container">
  <div class="demo-row clearfix">
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
        <p class="discuss-status-box__title"><strong>투표현황</strong> (참여자: <strong>253</strong>명)</p>
        <div class="demo-progress">
          <div class="progress-container">
            <div class="progress-bg progress-bg--discussion">
              <div class="progress-fill-bar progress-fill-bar--blue" style="width: 30%;"></div>
              <div class="progress-fill-bar progress-fill-bar--grey" style="width: 20%;"></div>
              <div class="progress-fill-bar progress-fill-bar--red" style="width: 50%;"></div>
            </div>
          </div>
          <div class="discuss-progress-info clearfix">
            <div class="progress-info__blue" style="width: 30%;">
              <p>찬성</p>
              <p>183표(35%)</p>
            </div>
            <div class="progress-info__grey" style="width: 20%;">
              <p>기타</p>
              <p>35표(15%)</p>
            </div>
            <div class="progress-info__red" style="width: 50%;">
              <p>반대</p>
              <p>210표(55%)</p>
            </div>
          </div>
        </div>

        <div class="discuss-status-box__buttons">
          <button type="button" class="btn d-btn d-btn--discuss btn-blue" data-toggle="modal" data-target="#myModal">
            찬성합니다
          </button>
          <button type="button" class="btn d-btn d-btn--discuss btn-grey" data-toggle="modal" data-target="#myModal">
            기타의견입니다
          </button>
          <button type="button" class="btn d-btn d-btn--discuss btn-red" data-toggle="modal" data-target="#myModal">
            반대합니다
          </button>
        </div>

      </div>

      <div class="demo-comments-container">
        <div class="demo-comments-top">
          <div class="clearfix">
            <div class="demo-comments-top__tabs demo-comments-top__tabs--discuss">
              <ul class="comment-sorting-ul clearfix">
                <li class="comment-sorting-li active"><a class="sorting-link" href="">최신순</a></li>
                <li class="comment-sorting-li"><a class="sorting-link" href="">과거순</a></li>
                <li class="comment-sorting-li"><a class="sorting-link" href="">공감순</a></li>
              </ul>
            </div>
          </div>
        </div>

        <ul class="demo-comments">
          <li class="comment-li">
            <div class="profile-circle profile-circle--comment" style="background-image: url('./img/hong-people.png')">
              <p class="alt-text">홍길동프로필</p>
            </div>
            <div class="comment-content">
              <p class="comment-name">홍기동</p>
              <div class="comment-info clearfix">
                <div class="comment-date-wrapper">
                  <p class="comment-time"><i class="xi-time"></i> 2018. 10. 22</p>
                </div>
                <div class="comment-likes-count">
                  <p class="comment-thumbs-count"><i class="xi-thumbs-up"></i> 공감 <strong>12</strong>개</p>
                </div>
              </div>
              <div class="comment-vote-result">
                <div class="comment-text-wrapper">
                  <p class="comment-content-text">
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod
                    tempor
                    incididunt ut labore et dolore magna aliqua. Quis ipsum suspendisse
                    ultrices
                    gravida. Risus commodo viverra maecenas accumsan lacus vel facilisis.
                  </p>
                </div>
                <div class="comment-status-wrapper">
                  <p class="status-btn status-btn--agree">찬성</p>
                </div>
              </div>
            </div>
          </li>

          <li class="comment-li">
            <div class="profile-circle profile-circle--comment" style="background-image: url('./img/hong-people.png')">
              <p class="alt-text">홍길동프로필</p>
            </div>
            <div class="comment-content">
              <p class="comment-name">홍기동</p>
              <div class="comment-info clearfix">
                <div class="comment-date-wrapper">
                  <p class="comment-time"><i class="xi-time"></i> 2018. 10. 22</p>
                </div>
                <div class="comment-likes-count">
                  <p class="comment-thumbs-count"><i class="xi-thumbs-up"></i> 공감 <strong>12</strong>개</p>
                </div>
              </div>
              <div class="comment-vote-result">
                <div class="comment-text-wrapper">
                  <p class="comment-content-text">
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod
                    tempor
                    incididunt ut labore et dolore magna aliqua. Quis ipsum suspendisse
                    ultrices
                    gravida. Risus commodo viverra maecenas accumsan lacus vel facilisis.
                  </p>
                </div>
                <div class="comment-status-wrapper">
                  <p class="status-btn status-btn--reject">반대</p>
                </div>
              </div>
            </div>
          </li>

          <li class="comment-li">
            <div class="profile-circle profile-circle--comment" style="background-image: url('./img/hong-people.png')">
              <p class="alt-text">홍길동프로필</p>
            </div>
            <div class="comment-content">
              <p class="comment-name">홍기동</p>
              <div class="comment-info clearfix">
                <div class="comment-date-wrapper">
                  <p class="comment-time"><i class="xi-time"></i> 2018. 10. 22</p>
                </div>
                <div class="comment-likes-count">
                  <p class="comment-thumbs-count"><i class="xi-thumbs-up"></i> 공감 <strong>12</strong>개</p>
                </div>
              </div>
              <div class="comment-vote-result">
                <div class="comment-text-wrapper">
                  <p class="comment-content-text">
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod
                    tempor
                    incididunt ut labore et dolore magna aliqua. Quis ipsum suspendisse
                    ultrices
                    gravida. Risus commodo viverra maecenas accumsan lacus vel facilisis.
                  </p>
                </div>
                <div class="comment-status-wrapper">
                  <p class="status-btn status-btn--etc">기타</p>
                </div>
              </div>
            </div>
          </li>


        </ul>
        <div class="comment-end-line"></div>
        <div class="show-more-container text-center">
          <button class="white-btn d-btn btn-more" type="button">더보기<i class="xi-angle-down-min"></i></button>
        </div>

      </div>
    </div>

    <%@ include file="../shared/side.jsp" %>
  </div>
</div>

<script>
  $(function () {
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

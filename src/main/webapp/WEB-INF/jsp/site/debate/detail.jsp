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
        <button class="sns-btn sns-btn--trigger collapsed" type="button" data-toggle="collapse" data-target="#sns-collapse"
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
            <div class="progress-fill-bar progress-fill-bar--blue" style="width: 30%;"></div>
            <div class="progress-fill-bar progress-fill-bar--grey" style="width: 20%;"></div>
            <div class="progress-fill-bar progress-fill-bar--red" style="width: 50%;"></div>
          </div>
        </div>
        <div class="progress-info clearfix">
          <div class="progress-info__left">
            <p class="progress-info__count"><i class="xi-user-plus"></i> 참여자 <strong>${debate.stats.applicantCount}</strong>명</p>
          </div>
          <div class="progress-info__right">
            <p class="progress-info__text">
              <sapn class="progress-blue">찬성(35%)</sapn>
              <span class="progress-grey"> • 기타(15%) • </span>
              <span class="progress-red">반대(55%)</span>
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
      </div>
    </div>
  </div>
</div>

<div class="middle-nav-tab" id="middle-nav-tab">
  <div class="container">
    <div class="middle-nav clearfix">
      <ul class="sorting-tab__ul clearfix">
        <li class="sorting-tab__li"><a href="#" class="sorting-tab__li__link">내용</a></li>
        <li class="sorting-tab__li active"><a href="#" class="sorting-tab__li__link">히스토리</a></li>
      </ul>
    </div>

  </div>
</div>

<div class="middle-nav-tab middle-nav-tab--scroll" id="middle-nav-tab-scroll">
  <div class="container">
    <div class="middle-nav clearfix">
      <ul class="sorting-tab__ul clearfix">
        <li class="sorting-tab__li"><a href="#" class="sorting-tab__li__link">내용</a></li>
        <li class="sorting-tab__li active"><a href="#" class="sorting-tab__li__link">히스토리</a></li>
      </ul>
      <div class="middle-btn-group">
        <button type="button" class="btn d-btn btn-blue" data-toggle="modal" data-target="#myModal">
          찬성합니다
        </button>
        <button type="button" class="btn d-btn btn-grey" data-toggle="modal" data-target="#myModal">
          기타의견입니다
        </button>
        <button type="button" class="btn d-btn btn-red" data-toggle="modal" data-target="#myModal">
          반대합니다
        </button>
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
        <h5 class="discuss-title">타이틀</h5>
        <div class="contents-box__contents">
          <p>지난기간 서울시의 재생사업이 다세대/빌라촌 실 거주환경에 전혀 도움이 되지 않았다는 점을 다시한번 검토했으면 합니다.
            빌거라고 들어보셨는지요? 같은 지역내에 초등학교가 빌라촌과 아파트촌에 한개씩 두개가 있는경우, 지역 학부모들이 빌라촌 지역의 초등학교에 대해 어떠한 평가를 내리는지
            아시나요? 아파트단지에서는 안전을 위해 차량진입을 막기위해 노력하지만, 빌라촌 지역에서 문을 나서자마자 뛰어나가는 아기들의 안전에 대해 서울시가 고려 해 본적
            한번이라도 있는지요?
            몇몇 아파트 단지에서는 차가 들어가네 못들어가네 다투는동안, 다세대/빌라 지역에서 자녀를 키우는 사람들은 문앞에 나서는 순간 차들이 지나다니는 도로입니다. 아이들과
            문 밖에 나서는 순간순간이 긴장의 연속이며, 유모차를 끌기 위해서는 주차된 차들 사이를 곡예하듯 지나가야 합니다. 차들이 모두 지나가기를 기다렸다가 이동해야 하는
            상황도 일상이지요.
            실거주환경에 도움이 전혀 못되는 재생사업으로 시간을 허비하는 동안, 거주환경의 커다란 차별을 방관하는 동안, 서울시 내의 살기좋은 아파트단지의 매매가가 높이
            뛰었다고 결론을 내려도 좋을상황입니다.

            각설하고, 강남/강북을 따지지 말고, 주거지역에서의 차도와 인도가 구분되어있는가 여부에 따른 서울시 빌라촌/다세대 밀집지역의 전면 개조가 시급합니다. "주거지역"
            이라고 허가를 내린 지역이라면, 안전하게 삶을 영위해 나가야 할 권리또한 시정부가 책임을 져야하지 않을까요?
            빌라촌/다세대 지역에서 차도가 형성되어있다면, 유모차가 지나갈 폭의 인도또한 필수입니다. 만일 이 기준에 미달하는 지역이 있고, 인도를 확보하지 못한다면 과감하게
            지역 전면개발에 대해 고려해야 하는 정책이 따라야 한다고 생각합니다.</p>
        </div>

        <div class="relative-links">
          <h5 class="relative-title">연관제안</h5>
          <ul class="relative-link-ul">
            <li class="relative-link-li"><a class="relative-link" href="">- 자전거를 타고 다니게 해주세요.</a></li>
            <li class="relative-link-li"><a class="relative-link" href="">- 노인분들 무료로 따릉이 탈 수 있게해주세요.</a></li>
          </ul>
        </div>


        <div class="attached-file-box">
          <h5 class="attached-file-box-title">첨부파일</h5>
          <ul class="attached-file-box-ul">
            <li><a href="" target="_blank"><i class="xi-file"></i> 첨부파일 이름.pdf</a></li>
            <li><a href="" target="_blank"><i class="xi-file"></i> 첨부파일 이름.pdf</a></li>
          </ul>
        </div>
      </div>

      <div class="discuss-status-box">
        <p class="discuss-status-box__title"><strong>투표현황</strong> (참여자: <strong>${debate.stats.applicantCount}</strong>명)</p>
        <div class="demo-progress">
          <div class="progress-container">
            <div class="progress-bg progress-bg--discussion">
              <div class="progress-fill-bar progress-fill-bar--blue" style="width: ${debate.stats.yesPercent()}%;"></div>
              <div class="progress-fill-bar progress-fill-bar--grey" style="width: ${debate.stats.etcPercent()}%;"></div>
              <div class="progress-fill-bar progress-fill-bar--red" style="width: ${debate.stats.noPercent()}%;"></div>
            </div>
          </div>
          <div class="discuss-progress-info clearfix">
            <div class="progress-info__blue" style="width: ${debate.stats.yesPercent()}%;">
              <p>찬성</p>
              <p>${debate.stats.yesCount}표(${debate.stats.yesPercent()}%)</p>
            </div>
            <div class="progress-info__grey" style="width: ${debate.stats.etcPercent()}%;">
              <p>기타</p>
              <p>${debate.stats.etcCount}표(${debate.stats.etcPercent()}%)</p>
            </div>
            <div class="progress-info__red" style="width: ${debate.stats.noPercent()}%;">
              <p>반대</p>
              <p>${debate.stats.noCount}표(${debate.stats.noPercent()}%)</p>
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
              <p class="comment-content-text">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
                incididunt ut labore et dolore magna aliqua. Quis ipsum suspendisse ultrices
                gravida. Risus commodo viverra maecenas accumsan lacus vel facilisis.
              </p>
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
              <p class="comment-content-text">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
                incididunt ut labore et dolore magna aliqua. Quis ipsum suspendisse ultrices
                gravida. Risus commodo viverra maecenas accumsan lacus vel facilisis.
              </p>
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
              <p class="comment-content-text">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
                incididunt ut labore et dolore magna aliqua. Quis ipsum suspendisse ultrices
                gravida. Risus commodo viverra maecenas accumsan lacus vel facilisis.
              </p>
            </div>
          </li>

        </ul>
        <div class="comment-end-line"></div>
        <div class="show-more-container text-center">
          <button class="white-btn d-btn btn-more" type="button">더보기<i class="xi-angle-down-min"></i></button>
        </div>

      </div>
    </div>


    <div class="demo-side">
      <div class="side-box">
        <p class="side-help-text">로그인 하시면, 다양한 제안과 토론에 참여가 가능합니다.</p>

        <div class="domo-box-buttons">
          <a href="" class="btn demo-btn demo-btn--primary btn-side">로그인하기<i class="xi-angle-right"></i></a>
        </div>

      </div>

    </div>
  </div><!-- demo-row end  -->
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

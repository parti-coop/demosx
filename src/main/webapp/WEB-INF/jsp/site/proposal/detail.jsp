<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>${proposal.title} - 민주주의 서울</title>
  <%@ include file="../shared/head.jsp" %>
</head>
<body>

<%@ include file="../shared/header.jsp" %>

<div class="container">
  <h3 class="demo-detail-title">제안보기</h3>
  <div class="clearfix">
    <div class="demo-content">
      <div class="title-box">
        <div class="title-row clearfix">
          <div class="detail-title-container">
            <h2 class="detail-title">${proposal.title}</h2>
          </div>
          <div class="thumbs-ups">
            <div class="thumbs-up-btn ${proposal.process.isInit() ? '' : 'active'}">공감<i class="xi-thumbs-up"></i></div>
            <div class="answer-status ${proposal.process.isComplete() ? 'active' : ''}">부서답변</div>
          </div>
        </div>

        <div class="title-info clearfix">
          <div class="title-author">
            <div class="profile-circle profile-circle--title"
                 style="background-image: url(${proposal.createdBy.viewPhoto()})">
              <p class="alt-text">${proposal.createdBy.name}사진</p>
            </div>
            <p class="title-author__name">${proposal.createdBy.name}</p>
            <p class="title-author__date"><i class="xi-time"></i> ${proposal.createdDate.toLocalDate()}</p>
          </div>
          <div class="sns-group">
            <button class="sns-btn sns-btn--trigger collapsed" type="button" data-toggle="collapse"
                    data-target="#sns-collapse" aria-expanded="false" aria-controls="sns-collapse"><i
                class="xi-share-alt">
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
        </div>
      </div>

      <div class="contents-box">
        <c:if test="${proposal.createdBy.id eq loginUser.id}">
          <div class="clearfix">
            <div class="pull-right">
              <a href="<c:url value="/edit-proposal.do?id=${proposal.id}"/>" class="btn btn-default btn-sm">수정하기</a>
              <button type="button" class="btn btn-default btn-sm" id="delete-proposal-btn">삭제하기</button>
            </div>
          </div>
        </c:if>
        <div class="contents-box__contents">${proposal.contentWithBr()}</div>

        <button class="content-thumbs-up-btn${proposal.liked eq true ? ' active' : ''}" id="proposal-like-btn">
          <i class="xi-thumbs-up"></i> 공감 <strong>${proposal.stats.likeCount}</strong>개
        </button>

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
      </div>

      <div class="admin-feedbacks">
        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
          <c:if test="${not empty proposal.adminComment}">
            <div class="panel panel-default panel-demo">
              <div class="panel-heading panel-heading--demo" role="tab" id="headingOne">
                <h4 class="panel-title">
                  <a class="demo-collapse-btn" role="button" data-toggle="collapse" data-parent="#accordion"
                     href="#collapseOne" aria-expanded="false" aria-controls="collapseOne">
                    <div class="feedback-title-wrapper clearfix">
                      <p class="feedback-name">관리자 답변</p>
                      <p class="feedback-date"><i class="xi-time"></i> ${proposal.adminCommentDate.toLocalDate()}</p>
                    </div>
                  </a>
                </h4>
              </div>
              <div id="collapseOne" class="panel-collapse collapse collapse-demo" role="tabpanel"
                   aria-labelledby="headingOne">
                <div class="panel-body">${proposal.adminComment}</div>
              </div>
              <div class="feedback-arrow-group">
                <i class="xi-angle-down">
                  <p class="sr-only">아래 화살표</p>
                </i>
                <i class="xi-angle-up">
                  <p class="sr-only">아래 화살표</p>
                </i>
              </div>
            </div>
          </c:if>
          <c:if test="${proposal.process.isComplete()}">
            <div class="panel panel-default panel-demo">
              <div class="panel-heading panel-heading--demo" role="tab" id="headingTwo">
                <h4 class="panel-title">
                  <a class="demo-collapse-btn" role="button" data-toggle="collapse" data-parent="#accordion"
                     href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                    <div class="feedback-title-wrapper clearfix">
                      <div class="profile-circle profile-circle--admin-feedback"
                           style="background-image: url(${proposal.manager.viewPhoto()})">
                        <p class="alt-text">${proposal.manager.name}사진</p>
                      </div>
                      <p class="feedback-name">${proposal.manager.name}</p>
                      <p class="feedback-date"><i class="xi-time"></i> ${proposal.managerCommentDate.toLocalDate()}</p>
                    </div>
                  </a>
                </h4>
              </div>
              <div id="collapseTwo" class="panel-collapse collapse collapse-demo" role="tabpanel"
                   aria-labelledby="headingTwo">
                <div class="panel-body">${proposal.managerComment}</div>
              </div>
              <div class="feedback-arrow-group">
                <i class="xi-angle-down">
                  <p class="sr-only">아래 화살표</p>
                </i>
                <i class="xi-angle-up">
                  <p class="sr-only">아래 화살표</p>
                </i>
              </div>
            </div>
          </c:if>
        </div>
      </div>

      <%@include file="../opinion/proposal.jsp" %>
    </div>

    <%@include file="../shared/side.jsp" %>
  </div><!-- demo-row end  -->
</div>

<c:if test="${proposal.createdBy.id eq loginUser.id}">
  <script>
    $(function () {
      $('#delete-proposal-btn').click(function () {
        if (!window.confirm('제안을 삭제하시겠습니까?')) return;

        $.ajax({
          headers: { 'X-CSRF-TOKEN': '${_csrf.token}' },
          url: '/ajax/mypage/proposals/${proposal.id}',
          type: 'DELETE',
          contentType: 'application/json',
          dataType: 'json',
          success: function (data) {
            alert(data.msg);
            window.location.href = '/mypage/proposal.do';
          },
          error: function (error) {
            if (error.status === 400) {
              if (error.responseJSON.fieldErrors) {
                var msg = error.responseJSON.fieldErrors.map(function (item) {
                  return item.fieldError;
                }).join('/n');
                alert(msg);
              } else alert(error.responseJSON.msg);
            } else if (error.status === 403) {
              alert('로그인이 필요합니다.');
              window.location.reload();
            }
          }
        });
      });
    });
  </script>
</c:if>
<script>
  $(function () {
    $('#proposal-like-btn').click(function () {
      var hasLike = $(this).hasClass('active');
      var that = $(this);
      $.ajax({
        headers: { 'X-CSRF-TOKEN': '${_csrf.token}' },
        url: '/ajax/mypage/proposals/${proposal.id}/' + (hasLike ? 'deselectLike' : 'selectLike'),
        type: 'PUT',
        contentType: 'application/json',
        dataType: 'json',
        success: function (data) {
          alert(data.msg);
          var count = +$('strong', that).text();
          console.log(count);
          if(hasLike) {
            that.removeClass('active');
            if(count !== 0) $('strong', that).text(count - 1);
          }
          else {
            that.addClass('active');
            $('strong', that).text(count + 1);
          }
        },
        error: function (error) {
          if (error.status === 400) {
            console.log(hasLike);
            if(hasLike) that.removeClass('active');
            else that.addClass('active');
            if (error.responseJSON.fieldErrors) {
              var msg = error.responseJSON.fieldErrors.map(function (item) {
                return item.fieldError;
              }).join('/n');
              alert(msg);
            } else alert(error.responseJSON.msg);
          } else if (error.status === 403) {
            alert('로그인이 필요합니다.');
            window.location.reload();
          }
        }
      });
    });
  });
</script>

<%@ include file="../shared/footer.jsp" %>

</body>
</html>

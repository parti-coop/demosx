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

  <div class="demo-row clearfix">
    <div class="demo-content">
      <div class="title-box">
        <div class="title-row clearfix">
          <div class="thumbs-ups">
            <div class="thumbs-up-btn" style="background-image: url(/images/thumbs-up-bg.png)">공감<i class="xi-thumbs-up"></i>
            </div>
            <div class="answer-status" style="background-image: url(/images/thumbs-up-bg-2.png)">부서답변</div>
          </div>
          <div class="detail-title-container">
            <h2 class="detail-title">${proposal.title}</h2>
          </div>
        </div>

        <div class="title-info clearfix">
          <div class="title-author">
            <div class="profile-circle profile-circle--title" style="background-image: url(${proposal.createdBy.viewPhoto()})"><p
                class="alt-text">${proposal.createdBy.name}사진</p></div>
            <p class="title-author__name">${proposal.createdBy.name}</p>
            <p class="title-author__date"><i class="xi-time"></i> ${proposal.createdDate.toLocalDate()}</p>
          </div>
          <div class="sns-group">
            <button href="" class="sns-btn" type="button"><i class="xi-facebook"><p class="alt-text">facebook</p></i>
            </button>
            <button href="" class="sns-btn" type="button"><i class="xi-kakaotalk"><p class="alt-text">kakaotalk</p></i>
            </button>
            <button href="" class="sns-btn" type="button"><i class="xi-twitter"><p class="alt-text">twitter</p></i>
            </button>
            <button href="" class="sns-btn" type="button"><i class="xi-blogger"><p class="alt-text">blogger</p></i>
            </button>
          </div>

        </div>
      </div>

      <div class="content-box">
        <div class="content-raw">
          ${proposal.content}
        </div>

        <div class="content-thumbs-up-count"><i class="xi-thumbs-up"></i> 공감 <strong>${proposal.stats.viewLikeCount()}</strong>개</div>

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
          <div class="panel panel-default panel-demo">
            <div class="panel-heading panel-heading--demo" role="tab" id="headingOne">
              <h4 class="panel-title">
                <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne"
                   aria-expanded="true" aria-controls="collapseOne">
                  <div class="feedback-title-wrapper clearfix">
                    <div class="profile-circle profile-circle--admin-feedback"
                         style="background-image: url('./img/hong-people.png')"><p class="alt-text">홍길동프로필</p></div>
                    <p class="feedback-name">관리자</p>
                    <p class="feedback-date"><i class="xi-time"></i> 2018. 10. 22</p>
                  </div>
                </a>
              </h4>
            </div>
            <div id="collapseOne" class="panel-collapse collapse collapse-demo in " role="tabpanel"
                 aria-labelledby="headingOne">
              <div class="panel-body">
                Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. 3 wolf
                moon officia aute, non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod.
                Brunch 3 wolf moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla assumenda
                shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente ea
                proident. Ad vegan excepteur butcher vice lomo. Leggings occaecat craft beer farm-to-table, raw denim
                aesthetic synth nesciunt you probably haven't heard of them accusamus labore sustainable VHS.
              </div>
            </div>
            <div class="feedback-arrow-group">
              <i class="xi-angle-down"><p class="sr-only">아래 화살표</p></i>
              <i class="xi-angle-up"><p class="sr-only">아래 화살표</p></i>
            </div>
          </div>
          <div class="panel panel-default panel-demo">
            <div class="panel-heading panel-heading--demo" role="tab" id="headingTwo">
              <h4 class="panel-title">
                <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo"
                   aria-expanded="false" aria-controls="collapseTwo">
                  <div class="feedback-title-wrapper clearfix">
                    <div class="profile-circle profile-circle--admin-feedback"
                         style="background-image: url('./img/hong-people.png')"><p class="alt-text">홍길동프로필</p></div>
                    <p class="feedback-name">관리자</p>
                    <p class="feedback-date"><i class="xi-time"></i> 2018. 10. 22</p>
                  </div>
                </a>
              </h4>
            </div>
            <div id="collapseTwo" class="panel-collapse collapse collapse-demo" role="tabpanel"
                 aria-labelledby="headingTwo">
              <div class="panel-body">
                Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid. 3 wolf
                moon officia aute, non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod.
                Brunch 3 wolf moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla assumenda
                shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente ea
                proident. Ad vegan excepteur butcher vice lomo. Leggings occaecat craft beer farm-to-table, raw denim
                aesthetic synth nesciunt you probably haven't heard of them accusamus labore sustainable VHS.
              </div>
            </div>
            <div class="feedback-arrow-group">
              <i class="xi-angle-down"><p class="sr-only">아래 화살표</p></i>
              <i class="xi-angle-up"><p class="sr-only">아래 화살표</p></i>
            </div>
          </div>
        </div>
      </div>

      <jsp:include page="../opinion/proposal.jsp">
        <jsp:param name="id" value="${proposal.id}"/>
      </jsp:include>
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
</div><!-- constainer end  -->

<%@ include file="../shared/footer.jsp" %>

</body>
</html>

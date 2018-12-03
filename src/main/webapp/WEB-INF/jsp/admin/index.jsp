<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>알림판</title>
  <%@ include file="./shared/head.jsp" %>
</head>
<body class="hold-transition skin-black-light fixed sidebar-mini">

<div class="wrapper">

  <%@ include file="./shared/header.jsp" %>

    <div class="content-wrapper">
      <section class="content-header">
        <h1>알림판</h1>
      </section>

      <section class="content">
        <div class="row">
          <div class="col-md-3 col-sm-6 col-xs-12">
            <div class="info-box">
              <span class="info-box-icon bg-aqua"><i class="fa fa-users" aria-hidden="true"></i></span>

              <div class="info-box-content">
                <span class="info-box-text">회원</span>
                <span class="info-box-number">${userCount}</span>
              </div>
            </div>
          </div>
          <div class="col-md-3 col-sm-6 col-xs-12">
            <div class="info-box">
              <span class="info-box-icon bg-aqua"><i class="fa fa-comment" aria-hidden="true"></i></span>

              <div class="info-box-content">
                <span class="info-box-text">전체 댓글</span>
                <span class="info-box-number">${opinionCount}</span>
              </div>
            </div>
          </div>

          <div class="clearfix visible-sm-block"></div>

          <div class="col-md-3 col-sm-6 col-xs-12">
            <div class="info-box">
              <span class="info-box-icon bg-aqua"><i class="fa fa-thumbs-up" aria-hidden="true"></i></span>

              <div class="info-box-content">
                <span class="info-box-text">전체 댓글 공감</span>
                <span class="info-box-number">${opinionCount}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="row">
          <div class="col-md-3 col-sm-6 col-xs-12">
            <div class="info-box">
              <span class="info-box-icon bg-green"><i class="fa fa-paper-plane" aria-hidden="true"></i></span>

              <div class="info-box-content">
                <span class="info-box-text">제안</span>
                <span class="info-box-number">${proposalCount}</span>
              </div>
            </div>
          </div>
          <div class="col-md-3 col-sm-6 col-xs-12">
            <div class="info-box">
              <span class="info-box-icon bg-green"><i class="fa fa-comment" aria-hidden="true"></i></span>

              <div class="info-box-content">
                <span class="info-box-text">제안 댓글</span>
                <span class="info-box-number">${proposalOpinionCount}</span>
              </div>
            </div>
          </div>
          <div class="col-md-3 col-sm-6 col-xs-12">
            <div class="info-box">
              <span class="info-box-icon bg-green"><i class="fa fa-thumbs-up" aria-hidden="true"></i></span>

              <div class="info-box-content">
                <span class="info-box-text">제안 공감</span>
                <span class="info-box-number">${issueLikeCount}</span>
              </div>
            </div>
          </div>

          <div class="clearfix visible-sm-block"></div>
        </div>
        <div class="row">
          <div class="col-md-3 col-sm-6 col-xs-12">
            <div class="info-box">
              <span class="info-box-icon bg-yellow"><i class="fa fa-comments-o" aria-hidden="true"></i></span>

              <div class="info-box-content">
                <span class="info-box-text">토론</span>
                <span class="info-box-number">${debateCount}</span>
              </div>
            </div>
          </div>
          <div class="col-md-3 col-sm-6 col-xs-12">
            <div class="info-box">
              <span class="info-box-icon bg-yellow"><i class="fa fa-comment" aria-hidden="true"></i></span>

              <div class="info-box-content">
                <span class="info-box-text">토론 댓글</span>
                <span class="info-box-number">${debateOpinionCount}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="row">
          <div class="col-md-3 col-sm-6 col-xs-12">
            <div class="info-box">
              <span class="info-box-icon bg-blue"><i class="fa fa-history" aria-hidden="true"></i></span>

              <div class="info-box-content">
                <span class="info-box-text">실행</span>
                <span class="info-box-number">${actionCount}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="row">
          <div class="col-md-3 col-sm-6 col-xs-12">
            <div class="info-box">
              <span class="info-box-icon bg-yellow"><i class="fa fa-comments-o" aria-hidden="true"></i></span>

              <div class="info-box-content">
                <span class="info-box-text">기관제안</span>
                <span class="info-box-number">${orgDebateCount}</span>
              </div>
            </div>
          </div>
          <div class="col-md-3 col-sm-6 col-xs-12">
            <div class="info-box">
              <span class="info-box-icon bg-yellow"><i class="fa fa-comment" aria-hidden="true"></i></span>

              <div class="info-box-content">
                <span class="info-box-text">기관제안 댓글</span>
                <span class="info-box-number">${orgDebateOpinionCount}</span>
              </div>
            </div>
          </div>
        </div>

      </section>
    </div>

  <%@ include file="./shared/footer.jsp" %>
</div>

</body>
</html>

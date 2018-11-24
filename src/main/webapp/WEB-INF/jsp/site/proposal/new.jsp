<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>제안하기 - 민주주의 서울</title>
  <%@ include file="../shared/head.jsp" %>

  <!-- form validation -->
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/parsley.js/2.8.1/parsley.min.js"></script>
  <script type="text/javascript" src="<c:url value="/js/parsley-ko.js"/>"></script>

  <!-- jquery serialize object -->
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/jquery-serialize-object/2.5.0/jquery.serialize-object.min.js"></script>
</head>
<body>
<%@ include file="../shared/header.jsp" %>

<div class="container">
  <h3 class="demo-detail-title">제안하기</h3>

  <div class="demo-row clearfix">
    <div class="demo-content">
      <form class="demo-form" id="form-new-proposal">
        <div class="form-input-container">
          <div class="form-warning-text">
            <p>・ 민원, 불편, 개선, 부조리 신고, 홍보 등은 해당 부서로 연락하여 주시기 바라며 본 플랫폼에서는 처리되지 않습니다.</p>
            <p>・ 제안 게시기준에 맞지 않는 부적절한 게시물은 이동 또는 삭제 조치됩니다.
              <a href="<c:url value="/copyright.do"/>">[저작권 및 컨텐츠 관련안내 바로가기]</a>
            </p>
          </div>

          <div class="form-group form-group--demo">
            <label class="demo-form-label" for="inputTitle">제목</label>
            <input type="text" class="form-control demo-input" id="inputTitle" placeholder="제목"
                   name="title" data-parsley-required="true" data-parsley-maxlength="100">
          </div>
          <div class="form-group form-group--demo">
            <label class="demo-form-label" for="inputContent">내용</label>
            <textarea class="form-control" name="content" id="inputContent" rows="10"
                      data-parsley-required="true"></textarea>
          </div>
          <div class="form-group">
            <label class="demo-form-label demo-form-label--check" for="inputContent">동의여부 확인</label>
            <div class="checkbox">
              <label>
                <input type="checkbox" name="agree" data-parsley-required="true" data-parsley-mincheck="2"
                       data-parsley-error-message="동의여부를 확인해 주세요." data-parsley-errors-container="#agreeError">
                <span class="check-required">(필수)</span> 본인이 제안한 제안내용이 담당부서의 검토 결과
                민원으로 처리될 수 있음에 동의합니다.
              </label>
            </div>
            <div class="checkbox">
              <label>
                <input type="checkbox" name="agree"><span class="check-required">(필수)</span> 개인정보 수집에 동의합니다.
              </label>
            </div>
            <div id="agreeError"></div>
          </div>
        </div>
        <div class="form-action text-right">
          <div class="inline-block">
            <a class="btn demo-submit-btn cancel-btn" href="<c:url value="/mypage/proposal.do"/>" role="button">나의 제안 목록</a>
            <button type="submit" class="demo-submit-btn demo-submit-btn--submit">제안하기</button>
          </div>
        </div>
      </form>
    </div>


    <%@include file="../shared/side.jsp" %>
  </div>
</div>
<script>
  $(function () {
    var $formNewProposal = $('#form-new-proposal');
    $formNewProposal.parsley(parsleyConfig);
    $formNewProposal.on('submit', function (event) {
      event.preventDefault();

      var data = $formNewProposal.serializeObject();
      console.log(data);
      $.ajax({
        headers: { 'X-CSRF-TOKEN': '${_csrf.token}' },
        url: '/ajax/mypage/proposals',
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify(data),
        success: function (data) {
          alert(data.msg);
          window.location.href='/mypage/proposal.do';
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

<%@ include file="../shared/footer.jsp" %>

</body>
</html>

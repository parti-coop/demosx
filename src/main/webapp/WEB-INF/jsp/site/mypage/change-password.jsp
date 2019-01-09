<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>내 정보 수정 - 민주주의 서울</title>
  <%@ include file="../shared/head.jsp" %>

  <!-- 파일 업로드 -->
  <link rel="stylesheet" type="text/css"
        href="https://cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.22.1/css/jquery.fileupload.min.css"/>
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.22.1/js/vendor/jquery.ui.widget.min.js"></script>
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.22.1/js/jquery.iframe-transport.min.js"></script>
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.22.1/js/jquery.fileupload.min.js"></script>

  <!-- jquery serialize object -->
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/jquery-serialize-object/2.5.0/jquery.serialize-object.min.js"></script>
</head>
<body class="home">
<%@ include file="../shared/header.jsp" %>

<div class="container">
  <h3 class="my-page-title">마이페이지</h3>
  <div class="m-tabs-mobile">
    <form name="mypage_link-form">
      <div class="sorting-tab__select">
        <select class="form-control" name="mypage-link">
          <option value="#" selected>내 정보 수정</option>
          <option value="/mypage/proposal.do">나의 제안 활동</option>
          <option value="/mypage/vote.do">나의 투표 활동</option>
          <option value="/mypage/opinion.do">나의 의견 활동</option>
        </select>
      </div>
    </form>
  </div>
  <ul class="my-page-tabs clearfix">
    <li class="my-page-tab active"><a href="<c:url value="/mypage/info.do"/>" class="my-page-tab__link">내 정보 수정</a></li>
    <li class="my-page-tab"><a href="<c:url value="/mypage/proposal.do"/>" class="my-page-tab__link">나의 제안 활동</a>
    </li>
    <li class="my-page-tab"><a href="<c:url value="/mypage/vote.do"/>" class="my-page-tab__link">나의 투표 활동</a></li>
    <li class="my-page-tab"><a href="<c:url value="/mypage/opinion.do"/>" class="my-page-tab__link">나의 의견 활동</a></li>
  </ul>

  <div class="mypage-edit-container">
    <form name="mypage-info" id="form-mypage">
      <div class="form-group form-group--demo" id="password-group">
        <label class="demo-form-label" for="inputPassword">비밀번호</label>
        <input type="password" class="form-control demo-input" name="currentPassword" id="inputPassword"
               autocomplete="off" placeholder="현재 비밀번호를 입력해 주세요." data-parsley-required="true">
        <p class="help-block help-block-error" id="password-error"></p>
      </div>

      <div class="form-group form-group--demo">
        <label class="demo-form-label" for="newPassword">새 비밀번호</label>
        <input type="password" class="form-control demo-input" name="changePassword" id="newPassword" autocomplete="off"
               placeholder="6자리 이상 비밀전호를 입력해 주세요." data-parsley-required="true" data-parsley-minlength="6">
      </div>

      <div class="form-group form-group--demo">
        <label class="demo-form-label" for="newConfirmPassword">새 비밀번호 확인</label>
        <input type="password" class="form-control demo-input" id="newConfirmPassword" autocomplete="off"
               placeholder="새 비밀전호를 확인해 주세요." data-parsley-required="true" data-parsley-equalto="#newPassword">
      </div>

      <div class="sing-action">
        <button type="submit" class="btn demo-btn demo-btn--primary btn-sign">비밀번호 변경하기</button>
      </div>
    </form>
  </div>
</div>
<script>
  $(function () {
    $('select[name=mypage-link]').change(function () {
      window.location.href = $(this).val();
    });
  });
</script>

<%@ include file="../shared/footer.jsp" %>

<script>
  $(function () {
    var $formMypage = $('#form-mypage');
    $formMypage.parsley(parsleyConfig);
    $formMypage.on("submit", function (event) {
      event.preventDefault();
      var data = $formMypage.serializeObject();
      $.ajax({
        headers: { 'X-CSRF-TOKEN': '${_csrf.token}' },
        url: '/ajax/mypage/change-password',
        type: 'PUT',
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify(data),
        success: function (data) {
          alert(data.msg);
          window.location.href = '/mypage/info.do';
        },
        error: function (error) {
          if (error.status === 400) {
            if (error.responseJSON.fieldErrors) {
              var msg = error.responseJSON.fieldErrors.map(function (item) {
                if(item.fieldName === 'password') {
                  $('#password-group').addClass('has-error');
                  $('#password-error').text(item.fieldError);
                }
                return item.fieldError;
              }).join('/n');
              alert(msg);
            } else alert(error.responseJSON.msg);
          } else if (error.status === 403 || error.status === 401) {
            alert('로그인이 필요합니다.');
            window.location.href = '/login.do';
          }
        }
      });
    });
  });
</script>
</body>
</html>

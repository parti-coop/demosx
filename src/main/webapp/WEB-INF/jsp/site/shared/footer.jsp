<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<footer class="demo-footer">
  <div class="container">
    <div class="row">
      <div class="col-sm-3 col-sm-push-9">
        <p class="term-link-p"><a class="term-link" href="<c:url value="/notice-list.do"/>"><i
            class="xi-angle-right-min"></i> 공지사항</a></p>
        <p class="term-link-p"><a class="term-link" href="<c:url value="/privacy.do"/>"><i
            class="xi-angle-right-min"></i> 개인정보처리방침</a></p>
        <p class="term-link-p"><a class="term-link" href="<c:url value="/terms.do"/>"><i class="xi-angle-right-min"></i>
          이용약관</a></p>
      </div>
      <div class="col-sm-9 col-sm-pull-3">
        <address class="demo-address">04524 서울특별시 중구 세종대로 110</address>
        <p class="footer-name">민주주의서울 오픈소스</p>
        <p class="footer-copyright">© Seoul Metropolitan Goverment. all rights reserved.</p>
      </div>
    </div>
  </div>
</footer>
<c:if test="${empty loginUser}">
  <div class="modal fade" id="modal-login" tabindex="-1" role="dialog" aria-labelledby="로그인 모달">
    <div class="modal-dialog modal-dialog--demo" role="document">
      <div class="modal-content">
        <div class="sign-container sign-container--modal">
          <h3 class="demo-detail-title">로그인</h3>
          <form id="form-login-modal">
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <div class="form-group form-group--demo">
              <label class="demo-form-label" for="modalInputEmail">아이디</label>
              <input type="email" class="form-control demo-input" name="id" id="modalInputEmail" placeholder="이메일"
                     data-parsley-required="true" data-parsley-whitespace="trim">
            </div>
            <div class="form-group form-group--demo">
              <label class="demo-form-label" for="modalInputPassword">비밀번호</label>
              <input type="password" class="form-control demo-input" id="modalInputPassword" name="pw"
                     placeholder="6자리 이상 비밀전호를 설정해 주세요."
                     data-parsley-required="true">
            </div>
            <div class="has-error">
              <p class="help-block help-block-error" id="modal-login-error"></p>
            </div>
            <p class="form-help-text form-help-text--blue">
              <a class="blue-link" href="<c:url value="/find-password.do"/>">비밀번호를 잊어버리셨나요?</a>
            </p>
            <div class="sing-action">
              <button type="submit" class="btn demo-btn demo-btn--primary btn-sign">로그인</button>
              <a href="<c:url value="/join.do"/>" class="btn d-btn white-btn btn-sign next-btn">회원가입</a>
              <button type="button" class="btn d-btn cancel-btn btn-sign next-btn" data-dismiss="modal">취소</button>
            </div>
          </form>
        </div><!-- constainer end  -->
      </div>
    </div>
  </div>
  <script>
    $(function () {
      var $modalLogin = $('#modal-login');
      $('.show-login-modal').click(function (event) {
        event.preventDefault();
        $modalLogin.modal('show');
      });
      var $formLoginModal = $('#form-login-modal');
      $formLoginModal.parsley(parsleyConfig);
      $formLoginModal.on('submit', function (event) {
        event.preventDefault();

        $('#modal-login-error').text('');
        $.ajax({
          url: '/loginProcess.do',
          type: 'POST',
          data: {
            '_csrf': $('input[name=_csrf]', this).val(),
            'id': $('input[name=id]', this).val(),
            'pw': $('input[name=pw]', this).val(),
            'ajax': 'on'
          },
          success: function (data) {
            window.location.reload();
          },
          error: function (e) {
            if (e.status === 400)
              $('#modal-login-error').text(e.responseJSON.msg);
          }
        });
      });
    });
  </script>
</c:if>
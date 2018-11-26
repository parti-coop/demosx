<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container discussion-contents-container" id="suggest-scroll-position">
  <div class="clearfix">
    <div class="demo-content">
      <c:if test="${not empty loginUser and loginUser.isAdmin()}">
        <button class="btn d-btn btn-white btn-block" id="new-history-btn">
          새로운 히스토리 작성 <i class="xi-angle-right"></i>
        </button>
      </c:if>
      <div>
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
                  <div class="comment-info clearfix">
                    <div class="comment-date-wrapper">
                      <p class="comment-name">${history.createdBy.name}</p>
                      <p class="comment-time"><i class="xi-time"></i> ${history.createdDate.toLocalDate()}</p>
                    </div>
                    <c:if test="${not empty loginUser and loginUser.isAdmin()}">
                      <div class="comment-likes-count">
                        <button type="button" class="btn btn-default btn-sm edit-history-btn"
                                data-id="${history.id}" data-content="${history.content}">수정하기
                        </button>
                        <button type="button" class="btn btn-default btn-sm delete-history-btn"
                                data-id="${history.id}">삭제하기
                        </button>
                      </div>
                    </c:if>
                  </div>
                  <p class="comment-content-text">${history.contentWithBr()}</p>
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

<c:if test="${not empty loginUser and loginUser.isAdmin()}">
  <div class="modal fade" id="modalEditHistory" tabindex="-1" role="dialog" aria-labelledby="히스토리 편집">
    <div class="modal-dialog modal-lg" role="document">
      <div class="modal-content">
        <div class="modal-body">
          <form class="demo-form" id="form-edit-history">
            <input type="hidden" name="issueId" value="${debate.id}">
            <input type="hidden" name="historyId" value="">
            <div class="form-input-container form-input-container--history">
              <div class="form-group form-group--demo">
                <label class="demo-form-label" for="inputContent">내용</label>
                <textarea class="form-control" name="content" id="inputContent" rows="8"
                          data-parsley-required="true"></textarea>
              </div>
            </div>
            <div class="form-action text-right">
              <div class="btn-group clearfix">
                <button class="btn demo-submit-btn cancel-btn" data-dismiss="modal" aria-label="Close" role="button">취소
                </button>
                <button type="submit" class="demo-submit-btn demo-submit-btn--submit">저장하기</button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>

  <!-- jquery serialize object -->
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/jquery-serialize-object/2.5.0/jquery.serialize-object.min.js"></script>
  <script>
    $(function () {
      var $formEditHistory = $('#form-edit-history');
      $formEditHistory.parsley(parsleyConfig);
      $formEditHistory.on('submit', function (event) {
        event.preventDefault();
        var data = $formEditHistory.serializeObject();
        var isNew = !data.historyId ? true : false;
        $.ajax({
          headers: { 'X-CSRF-TOKEN': '${_csrf.token}' },
          url: '/admin/ajax/histories' + (isNew ? '' : ('/' + data.historyId)),
          type: isNew ? 'POST' : 'PUT',
          contentType: 'application/json',
          dataType: 'json',
          data: JSON.stringify(data),
          success: function (result) {
            alert(result.msg);
            window.location.reload();
          },
          error: function (error) {
            if (error.status === 400) {
              if (error.responseJSON.fieldErrors) {
                var msg = error.responseJSON.fieldErrors.map(function (item) {
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

      var $modalEditHistory = $('#modalEditHistory');
      $('#new-history-btn').click(function () {
        $('input[name=historyId]', $formEditHistory).val('');
        $formEditHistory[0].reset();
        $formEditHistory.parsley().reset();
        $modalEditHistory.modal('show');
      });
      $('.edit-history-btn').click(function () {
        $formEditHistory[0].reset();
        $formEditHistory.parsley().reset();
        $('input[name=historyId]', $formEditHistory).val($(this).data('id'));
        $('textarea[name=content]', $formEditHistory).val($(this).data('content'));
        $modalEditHistory.modal('show');
      });
      $('.delete-history-btn').click(function () {
        if (!window.confirm('삭제할까요?')) return;

        var id = $(this).data('id');
        $.ajax({
          headers: { 'X-CSRF-TOKEN': '${_csrf.token}' },
          url: '/admin/ajax/histories/' + id,
          type: 'DELETE',
          contentType: 'application/json',
          dataType: 'json',
          success: function (data) {
            alert(data.msg);
            window.location.reload();
          },
          error: function (error) {
            if (error.status === 400) {
              if (error.responseJSON.fieldErrors) {
                var msg = error.responseJSON.fieldErrors.map(function (item) {
                  return item.fieldError;
                }).join('/n');
                alert(msg);
              } else alert(error.responseJSON.msg);
            } else if (error.status === 401) {
              alert('로그인이 필요합니다.');
              window.location.href = '/login.do';
            } else if (error.status === 403) {
              alert('삭제할 수 없습니다.');
            }
          }
        });
      });
    });
  </script>
</c:if>

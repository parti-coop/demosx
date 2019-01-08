<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container discussion-contents-container" id="suggest-scroll-position">
  <div class="clearfix">
    <div class="demo-content">
      <c:if test="${not empty loginUser and loginUser.isAdmin()}">
        <a class="btn d-btn btn-white btn-block"
           href="<c:url value="/debate-history-edit.do?id=${debate.id}#middle-nav-tab"/>">
          새로운 히스토리 작성 <i class="xi-angle-right"></i>
        </a>
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
                        <a href="<c:url value="/debate-history-edit.do?id=${debate.id}&historyId=${history.id}#middle-nav-tab"/>"
                           class="btn btn-default btn-sm">수정하기
                        </a>
                        <button type="button" class="btn btn-default btn-sm delete-history-btn"
                                data-id="${history.id}">삭제하기
                        </button>
                      </div>
                    </c:if>
                  </div>
                  <div class="history-content-text">${history.content}</div>
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
  <script>
    $(function () {
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

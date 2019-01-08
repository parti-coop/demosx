<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container discussion-contents-container" id="suggest-scroll-position">
  <div class="clearfix">
    <div class="demo-content">
      <form class="demo-form" id="form-edit-history">
        <input type="hidden" name="issueId" value="${debate.id}">
        <input type="hidden" name="historyId" value="${history.id}">
        <div class="form-input-container--history">
          <div class="form-group form-group--demo">
            <label class="demo-form-label" for="inputContent">히스토리 ${empty history ? '등록하기' : '수정하기'}</label>
            <textarea class="form-control tinymce-editor" name="content" id="inputContent" rows="8"
                      data-parsley-required="true">${history.content}</textarea>
            <input type="file" name="file" class="hidden" id="tinymce-file-upload">
          </div>
        </div>
        <div class="form-action text-right">
          <div class="btn-group clearfix">
            <button type="submit" class="demo-submit-btn demo-submit-btn--submit">저장하기</button>
          </div>
        </div>
      </form>
    </div>

    <%@ include file="../shared/side.jsp" %>
  </div><!-- demo-row end  -->
</div>

<c:if test="${not empty loginUser and loginUser.isAdmin()}">
  <!-- 파일 업로드 -->
  <link rel="stylesheet" type="text/css"
        href="https://cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.22.1/css/jquery.fileupload.min.css"/>
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.22.1/js/vendor/jquery.ui.widget.min.js"></script>
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.22.1/js/jquery.iframe-transport.min.js"></script>
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.22.1/js/jquery.fileupload.min.js"></script>

  <!-- tinymce editor -->
  <script type="text/javascript" src="<c:url value="/tinymce/tinymce.min.js"/>"></script>

  <!-- jquery serialize object -->
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/jquery-serialize-object/2.5.0/jquery.serialize-object.min.js"></script>
  <script>
    $(function () {
      var $content = $('.tinymce-editor');
      var $editor = null;
      tinymce.init({
        selector: '.tinymce-editor',
        menubar: false,
        language: 'ko_KR',
        plugins: ['autolink', 'autosave', 'textcolor', 'image', 'media', 'link', 'paste', 'autoresize'],
        toolbar: "undo redo | styleselect | forecolor bold italic | alignleft aligncenter alignright alignjustify | link media custom_image",
        mobile: {
          theme: 'mobile'
        },
        branding: false,
        content_css: ['https://stackpath.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css', '/css/admin.css', 'https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic'],
        preview_styles: 'font-family font-size color',
        body_class: 'container',
        setup: function (editor) {
          $editor = editor;
          editor.addButton('custom_image', {
            title: '이미지삽입',
            icon: 'image',
            onclick: function() {
              $('#tinymce-file-upload').click();
            }
          });
        },
        init_instance_callback: function (editor) {
          editor.on('Change', function (e) {
            $content.val(editor.getContent());
            $content.parsley().validate();
          });
        }
      });

      $('#tinymce-file-upload').fileupload({
        headers: {
          'X-CSRF-TOKEN': '${_csrf.token}'
        },
        url: '/admin/ajax/files',
        dataType: 'json',
        done: function (e, data) {
          $editor.execCommand('mceInsertContent', false, '<img src="' + data.result.url + '" alt="' + data.result.filename  + '"/>');
        },
        progressall: function (e, data) {
        },
        fail: function (e, data) {
          alert(data.jqXHR.responseJSON.msg);
        }
      }).prop('disabled', !$.support.fileInput)
        .parent().addClass($.support.fileInput ? undefined : 'disabled');

      var $formEditHistory = $('#form-edit-history');
      $formEditHistory.parsley(parsleyConfig);
      $formEditHistory.on('submit', function (event) {
        event.preventDefault();
        var data = $formEditHistory.serializeObject();
        var isNew = !data.historyId;
        $.ajax({
          headers: { 'X-CSRF-TOKEN': '${_csrf.token}' },
          url: '/admin/ajax/histories' + (isNew ? '' : ('/' + data.historyId)),
          type: isNew ? 'POST' : 'PUT',
          contentType: 'application/json',
          dataType: 'json',
          data: JSON.stringify(data),
          success: function (result) {
            alert(result.msg);
            window.location.href='/debate-history.do?id=${debate.id}#middle-nav-tab';
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
    });
  </script>
</c:if>

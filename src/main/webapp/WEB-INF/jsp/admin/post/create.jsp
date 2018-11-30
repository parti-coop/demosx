<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>실행 관리 - 생성 - Democracy</title>
  <%@ include file="../shared/head.jsp" %>

  <!-- form validation -->
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/parsley.js/2.8.1/parsley.min.js"></script>
  <script type="text/javascript" src="<c:url value="/js/parsley-ko.js"/>"></script>

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
  <script>
    $(function () {
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
    });
  </script>
</head>
<body class="hold-transition skin-black-light fixed sidebar-mini admin">

<div class="wrapper">
  <%@ include file="../shared/header.jsp" %>

  <div class="content-wrapper">
    <section class="content-header">
      <h1>공지사항 관리 - 생성</h1>
    </section>

    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header with-border">
              <h3 class="box-title">공지사항</h3>
            </div>
            <form:form commandName="createDto" class="form-horizontal">
              <div class="box-body">
                <div class="form-group">
                  <label class="col-sm-2 control-label">제목<span> *</span></label>
                  <div class="col-sm-10">
                    <form:input path="title" type="text" class="form-control input-sm" autocomplete="off"
                                data-parsley-required="true" data-parsley-whitespace="trim"
                                data-parsley-maxlength="100"/>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">내용</label>
                  <div class="col-sm-10">
                    <form:textarea path="content" class="tinymce-editor"
                                   style="visibility:hidden; width:100%; height:400px;"/>
                    <input type="file" name="file" class="hidden" id="tinymce-file-upload">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">공개여부<span> *</span></label>
                  <div class="col-sm-10">
                    <label class="radio-inline">
                      <form:radiobutton path="status" value="OPEN"
                                        data-parsley-required="true"
                                        data-parsley-errors-container="#statusError"/>공개
                    </label>
                    <label class="radio-inline">
                      <form:radiobutton path="status" value="CLOSED"/>비공개
                    </label>
                    <div id="statusError"></div>
                  </div>
                </div>
              </div>
              <div class="box-footer">
                <a href="<c:url value="/admin/post/notice.do"/>" class="btn btn-default btn-sm">목록</a>
                <button type="submit" class="btn btn-primary btn-sm pull-right" id="update-btn">생성하기</button>
              </div>
            </form:form>
          </div>
        </div>
      </div>
    </section>
  </div>
  <%@ include file="../shared/footer.jsp" %>
</div>

<script>
  $(function () {
    $('#createDto').parsley(parsleyConfig);
  });
</script>
</body>
</html>

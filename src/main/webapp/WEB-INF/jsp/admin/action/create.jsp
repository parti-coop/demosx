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

  <!-- date picker -->
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/moment.min.js"></script>
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/locale/ko.js"></script>
  <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
  <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css"/>

  <!-- 파일 업로드 -->
  <link rel="stylesheet" type="text/css"
        href="https://cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.22.1/css/jquery.fileupload.min.css"/>
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.22.1/js/vendor/jquery.ui.widget.min.js"></script>
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.22.1/js/jquery.iframe-transport.min.js"></script>
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/blueimp-file-upload/9.22.1/js/jquery.fileupload.min.js"></script>

  <!-- select2 -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/css/select2.min.css">
  <link rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css">
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/js/select2.min.js"></script>
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/js/i18n/ko.js"></script>

  <style>
    #thumbnail-remove {
      position: absolute;
      right: -25px;
      top: 0;
    }

    .thumbnail-img-wrapper {
      position: relative;
      display: inline-block;
      margin-right: 30px;
    }
  </style>

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
      <h1>실행 관리 - 생성</h1>
    </section>

    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header with-border">
              <h3 class="box-title">실행</h3>
            </div>
            <form:form commandName="createDto" class="form-horizontal">
              <div class="box-body">
                <div class="form-group">
                  <label class="col-sm-2 control-label">썸네일<span> *</span></label>
                  <div class="col-sm-4">
                    <div id="thumbnail-upload" class="${createDto.thumbnail eq null ? '' : 'hidden'}">
                      <span class="btn btn-success btn-sm fileinput-button">
                          <i class="glyphicon glyphicon-plus"></i>
                          <span>이미지 선택</span>
                          <input id="thumbnail-upload-input" type="file" name="file">
                      </span>
                      <img src="<c:url value="/images/loading.gif"/>" height="20" id="thumbnail-progress"
                           class="hidden">
                    </div>
                    <div id="thumbnail-uploaded" class="${createDto.thumbnail eq null ? 'hidden' : ''}">
                      <div class="thumbnail-img-wrapper">
                        <img src="${createDto.thumbnail}">
                        <i id="thumbnail-remove" class="fa fa-times fa-2x cursor-pointer"></i>
                      </div>
                    </div>
                    <input type="text" class="hidden" name="thumbnail" id="thumbnail" value="${createDto.thumbnail}"
                           data-parsley-required="true">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">분류<span> *</span></label>
                  <div class="col-sm-4">
                    <form:select path="category" class="form-control input-sm"
                                 data-parsley-required="true">
                      <option value="">분류선택</option>
                      <form:options items="${categories}" itemLabel="name" itemValue="name"/>
                    </form:select>
                  </div>
                </div>
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
                  <label class="col-sm-2 control-label">첨부파일</label>
                  <div class="col-sm-10">
                    <div id="file-upload">
                      <span class="btn btn-success btn-sm fileinput-button">
                          <i class="glyphicon glyphicon-plus"></i>
                          <span>파일 선택</span>
                          <input id="file-upload-input" type="file" name="file">
                      </span>
                      <img src="<c:url value="/images/loading.gif"/>" height="20" id="file-progress" class="hidden">
                    </div>
                    <div id="file-uploaded">
                      <c:forEach var="file" items="${createDto.files}" varStatus="status">
                        <p class="form-control-static">
                          <a href="${file.url}">${file.name}</a>
                          <i class="fa fa-times-circle cursor-pointer file-remove ml-10"></i>
                          <input type="hidden" class="file-name" name="files[${status.index}].name"
                                 value="${file.name}">
                          <input type="hidden" class="file-url" name="files[${status.index}].url" value="${file.url}">
                        </p>
                      </c:forEach>
                    </div>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">연관제안</label>
                  <div class="col-sm-6">
                    <div class="input-group input-group-sm">
                      <select id="select-proposal-input" class="form-control"></select>
                      <span class="input-group-btn">
                        <button type="button" class="btn btn-default" id="select-proposal-btn">추가하기</button>
                      </span>
                    </div>
                    <div id="selected-proposal-list">
                      <c:forEach var="relation" items="${createDto.relations}">
                        <c:set var="issue" value="${createDto.issueMap[relation]}"/>
                        <c:if test="${issue.type eq 'P'}">
                          <p class="form-control-static">${issue.title}
                            <i class="fa fa-times-circle cursor-pointer remove-issue-icon ml-10"></i>
                            <input type="hidden" class="relation-input" value="${issue.id}">
                          </p>
                        </c:if>
                      </c:forEach>
                    </div>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">토론제안</label>
                  <div class="col-sm-6">
                    <div class="input-group input-group-sm">
                      <select id="select-debate-input" class="form-control"></select>
                      <span class="input-group-btn">
                        <button type="button" class="btn btn-default" id="select-debate-btn">추가하기</button>
                      </span>
                    </div>
                    <div id="selected-debate-list">
                      <c:forEach var="relation" items="${createDto.relations}">
                        <c:set var="issue" value="${createDto.issueMap[relation]}"/>
                        <c:if test="${issue.type eq 'D'}">
                          <p class="form-control-static">${issue.title}
                            <i class="fa fa-times-circle cursor-pointer remove-issue-icon ml-10"></i>
                            <input type="hidden" class="relation-input" value="${issue.id}">
                          </p>
                        </c:if>
                      </c:forEach>
                    </div>
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
                <a href="<c:url value="/admin/issue/action.do"/>" class="btn btn-default btn-sm">목록</a>
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
    function rearrangeIssue() {
      $('input.relation-input').each(function (i) {
        $(this).attr('name', 'relations[' + i + ']');
      });
    }

    function getSelectedIssue(title, id) {
      return '<p class="form-control-static">' + title +
        '<i class="fa fa-times-circle cursor-pointer remove-issue-icon ml-10"></i>' +
        '<input type="hidden" class="relation-input" value="' + id + '"></p>';
    }

    function initIssueSelect2(issueType) {
      var $selectIssueInput = $('#select-' + issueType + '-input');
      $selectIssueInput.select2({
        language: 'ko',
        theme: "bootstrap",
        placeholder: "제목을 입력하세요.",
        ajax: {
          headers: { 'X-CSRF-TOKEN': '${_csrf.token}' },
          url: '/admin/ajax/issue/' + issueType + 's/select',
          type: 'GET',
          dataType: 'json',
          data: function (params) {
            return {
              search: params.term
            };
          },
          processResults: function (data) {
            return {
              results: data.content.map(function (item) {
                return {
                  id: item.id,
                  text: item.title,
                  item: item
                }
              })
            };
          }
        }
      });

      $('#select-' + issueType + '-btn').click(function () {
        var selectedData = $selectIssueInput.select2('data');
        if (selectedData.length === 0) {
          alert('선택된 항목이 없습니다.');
          return;
        }

        var sameValueItem = $('input.relation-input[value=' + selectedData[0].id + ']');
        if (sameValueItem.length !== 0) {
          alert('이미 선택된 항목입니다.');
          return;
        }

        $('#selected-' + issueType + '-list').append(getSelectedIssue(selectedData[0].text, selectedData[0].id));
        rearrangeIssue();
        $selectIssueInput.val(null).trigger('change');
      });
    }

    initIssueSelect2('proposal');
    initIssueSelect2('debate');
    rearrangeIssue();

    $(document).on('click', '.remove-issue-icon', function () {
      $(this).parent('p').remove();
      rearrangeIssue();
    });
  });
</script>
<script>
  $(function () {
    var $thumbnailInput = $('input[name=thumbnail]');

    function setThumbnail(thumbnail) {
      var $thumbnailUploaded = $('#thumbnail-uploaded');
      if (thumbnail) {
        $('#thumbnail-upload').addClass('hidden');
        $thumbnailUploaded.removeClass('hidden');
        $('img', $thumbnailUploaded).attr('src', thumbnail);
        $thumbnailInput.val(thumbnail);
        $thumbnailInput.parsley().validate();
      } else {
        $('#thumbnail-upload').removeClass('hidden');
        $thumbnailUploaded.addClass('hidden');
        $('img', $thumbnailUploaded).attr('src', '');
        $thumbnailInput.val('');
        $thumbnailInput.parsley().validate();
      }
    }

    $('#thumbnail-remove').click(function () {
      setThumbnail('');
    });

    $('#thumbnail-upload-input').fileupload({
      headers: {
        'X-CSRF-TOKEN': '${_csrf.token}'
      },
      url: '/admin/ajax/files?type=THUMBNAIL',
      dataType: 'json',
      done: function (e, data) {
        $('#thumbnail-progress').addClass('hidden');
        setThumbnail(data.result.url);
      },
      progressall: function (e, data) {
        $('#thumbnail-progress').removeClass('hidden');
      },
      fail: function (e, data) {
        $('#thumbnail-progress').addClass('hidden');
        alert(data.jqXHR.responseJSON.msg);
      }
    }).prop('disabled', !$.support.fileInput)
      .parent().addClass($.support.fileInput ? undefined : 'disabled');
  });
</script>

<script>
  $(function () {
    function getUploadedFile(filename, url) {
      return '<p class="form-control-static">' +
        '<a href="' + url + '" download="' + filename + '">' + filename + '</a><i class="fa fa-times-circle cursor-pointer file-remove ml-10"></i>' +
        '<input type="hidden" class="file-name" value="' + filename + '">' +
        '<input type="hidden" class="file-url" value="' + url + '">' +
        '</p>';
    }

    function rearrangeFile() {
      $('input.file-name').each(function (i) {
        $(this).attr('name', 'files[' + i + '].name');
      });
      $('input.file-url').each(function (i) {
        $(this).attr('name', 'files[' + i + '].url');
      });
    }

    $('#file-upload-input').fileupload({
      headers: {
        'X-CSRF-TOKEN': '${_csrf.token}'
      },
      url: '/admin/ajax/files?type=ORIGINAL',
      dataType: 'json',
      done: function (e, data) {
        $('#file-progress').addClass('hidden');
        $('#file-uploaded').append(getUploadedFile(data.result.filename, data.result.url));
        rearrangeFile();
      },
      progressall: function (e, data) {
        $('#file-progress').removeClass('hidden');
      },
      fail: function (e, data) {
        $('#file-progress').addClass('hidden');
        alert(data.jqXHR.responseJSON.msg);
      }
    }).prop('disabled', !$.support.fileInput)
      .parent().addClass($.support.fileInput ? undefined : 'disabled');

    $(document).on('click', '.file-remove', function () {
      $(this).parent('p').remove();
      rearrangeFile();
    });
  });
</script>

<script>
  $(function () {
    $('#createDto').parsley(parsleyConfig);

    moment.locale('ko');
    var $debateDates = $('#debate-dates');
    $debateDates.daterangepicker({
      autoUpdateInput: false,
      locale: { format: 'YYYY-MM-DD', cancelLabel: '취소', applyLabel: '확인' }
    });
    $debateDates.on('apply.daterangepicker', function (ev, picker) {
      var startDate = picker.startDate.format('YYYY-MM-DD');
      var endDate = picker.endDate.format('YYYY-MM-DD');
      $('input[name=startDate]').val(startDate);
      $('input[name=endDate]').val(endDate);
      $debateDates.val(startDate + ' - ' + endDate);
      $debateDates.parsley().validate();
    });
  });
</script>
</body>
</html>

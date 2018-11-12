<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>카테고리 관리 - Democracy</title>
  <%@ include file="../shared/head.jsp" %>

  <!-- datatables -->
  <link rel="stylesheet" type="text/css" href="<c:url value="/css/dataTables.bootstrap.min.css"/>"/>
  <script type="text/javascript" src="<c:url value="/js/jquery.dataTables.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/dataTables.bootstrap.min.js"/>"></script>

  <!-- form validation -->
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/parsley.js/2.8.1/parsley.min.js"></script>
  <script type="text/javascript" src="<c:url value="/js/parsley-ko.js"/>"></script>

  <!-- jquery serialize object -->
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/jquery-serialize-object/2.5.0/jquery.serialize-object.min.js"></script>
</head>
<body class="hold-transition skin-black-light fixed sidebar-mini admin">

<div class="wrapper">
  <%@ include file="../shared/header.jsp" %>

  <div class="content-wrapper">
    <section class="content-header">
      <h1>카테고리 관리
        <button type="button" class="btn btn-primary btn-sm pull-right create-btn">카테고리 생성하기
        </button>
      </h1>
    </section>

    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-body">
              <table id="list" class="table table-bordered table-striped" width="100%">
                <thead>
                <tr>
                  <th>노출순서</th>
                  <th>카테고리명</th>
                  <th>공개여부</th>
                  <th>수정하기</th>
                </tr>
                </thead>
              </table>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
  <%@ include file="../shared/footer.jsp" %>
</div>
<div class="modal fade" id="modal-category">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">×</span></button>
        <h4 class="modal-title">카테고리</h4>
      </div>
      <form role="form" id="form-category" class="form-horizontal">
        <div class="modal-body">
          <input type="hidden" name="id">
          <div class="form-group">
            <label title="name" class="col-sm-2 control-label">카테고리명<span> *</span></label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="name" autocomplete="off"
                     data-parsley-required="true" data-parsley-maxlength="30" data-parsley-trim-value="true">
            </div>
          </div>
          <div class="form-group">
            <label title="sequence" class="col-sm-2 control-label">노출순서<span> *</span></label>
            <div class="col-sm-10">
              <input type="number" class="form-control" name="sequence" min="0" max="100"
                     data-parsley-required="true" placeholder="숫자를 입력해 주세요.">
            </div>
          </div>
          <div class="form-group">
            <label title="enabled" class="col-sm-2 control-label">공개여부<span> *</span></label>
            <div class="col-sm-10">
              <label class="radio-inline">
                <input type="radio" name="enabled" value="true"
                       data-parsley-required="true" data-parsley-errors-container="#enabledError"> 공개
              </label>
              <label class="radio-inline">
                <input type="radio" name="enabled" value="false"> 비공개
              </label>
              <div id="enabledError"></div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default btn-sm pull-left" data-dismiss="modal">닫기</button>
          <button type="button" class="btn btn-primary btn-sm" id="btn-category-save">저장하기</button>
        </div>
      </form>
    </div>
  </div>
</div>


<script>
  $(function () {
    var $modalCategory = $('#modal-category');
    var $formCategory = $('#form-category');
    $formCategory.parsley(parsleyConfig);

    $('.create-btn').click(function (event) {
      $('input[name=id]').val('');
      $formCategory[0].reset();
      $formCategory.parsley().reset();
      $modalCategory.modal('show');
      event.preventDefault();
    });

    $(document).on('click', '.update-btn', function () {
      $formCategory[0].reset();
      $formCategory.parsley().reset();

      var data = table.row($(this).parents('tr')).data();
      $('input[name=id]').val(data.id);
      $('input[name=name]').val(data.name);
      $('input[name=sequence]').val(data.sequence);
      $('input:radio[name=enabled][value=' + data.enabled.toString() + ']').prop('checked', true);

      $modalCategory.modal('show');
      event.preventDefault();
    });

    $('#btn-category-save').click(function () {
      if (!$formCategory.parsley().validate()) return;

      var data = $formCategory.serializeObject();
      adminAjax({
        csrf: '${_csrf.token}',
        url: '/admin/ajax/categories/' + data.id,
        type: (data.id ? 'PUT' : 'POST'),
        data: data,
        success: function (data) {
          table.draw();
          $modalCategory.modal('hide');
        },
        error: function () {
        }
      });
    });


    var table = $('#list')
      .on('preXhr.dt', function (e, settings, data) {
        delete data['search'];
        delete data['draw'];
        delete data['columns'];
        delete data['order'];
        delete data['start'];
        delete data['length'];
      })
      .on('xhr.dt', function (e, settings, json, xhr) {
        json['recordsFiltered'] = json.length;
        json['recordsTotal'] = json.length;
        json['data'] = json;
      })
      .DataTable({
        lengthMenu: [10, 15, 20],
        language: {
          'infoEmpty': '검색 결과가 없습니다.',
          'zeroRecords': '검색 결과가 없습니다.',
          'emptyTable': '테이블에 표시할 내용이 없습니다.',
          'info': '_TOTAL_ 개의 항목 중 _START_ ~ _END_',
          'infoFiltered': ' (전체 _MAX_ 개)',
          'lengthMenu': '페이지 당 _MENU_ 항목 표시',
          'search': '',
          'searchPlaceholder': '제목, 작성자 검색',
          paginate: {
            first: '«',
            previous: '‹',
            next: '›',
            last: '»'
          },
          aria: {
            paginate: {
              first: 'First',
              previous: 'Previous',
              next: 'Next',
              last: 'Last'
            }
          }
        },
        info: false,
        ordering: false,
        paging: false,
        searching: false,
        order: [[0, 'asc'], [1, 'asc']],
        serverSide: true,
        ajax: {
          url: '/admin/ajax/categories',
          type: 'GET',
          error: function (e) {
            window.location.href = '/admin/index.do';
          }
        },
        columns: [
          { data: 'sequence' },
          { data: 'name', orderable: false },
          {
            data: function (item) {
              if (item.enabled === false) return '비공개';
              return '공개';
            }, orderable: false
          },
          {
            data: function (item) {
              return '<button class="btn btn-default btn-sm update-btn">수정하기</button>';
            }, orderable: false
          }
        ]
      });
  });
</script>
<style>
  .table > tbody > tr > td, .table > tbody > tr > th, .table > tfoot > tr > td, .table > tfoot > tr > th, .table > thead > tr > td, .table > thead > tr > th {
    vertical-align: middle;
  }
</style>

</body>
</html>

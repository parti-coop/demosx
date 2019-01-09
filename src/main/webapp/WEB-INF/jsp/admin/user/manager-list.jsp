<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>담당자 관리 - Democracy</title>
  <%@ include file="../shared/head.jsp" %>

  <!-- datatables -->
  <link rel="stylesheet" type="text/css" href="<c:url value="/css/dataTables.bootstrap.min.css"/>"/>
  <script type="text/javascript" src="<c:url value="/js/jquery.dataTables.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/dataTables.bootstrap.min.js"/>"></script>

  <!-- select2 -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/css/select2.min.css">
  <link rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css">
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/js/select2.min.js"></script>
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/js/i18n/ko.js"></script>
  <style>
    .input-group>.select2-container--bootstrap {
      width: 100% !important;
    }
    .has-error .select2-selection, .has-success .select2-selection {
      border-color: #ccc;
    }

    .select2-container--bootstrap.select2-container--focus .select2-selection, .select2-container--bootstrap.select2-container--open .select2-selection {
      -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075), 0 0 8px rgba(102,175,233,.6);
      box-shadow: inset 0 1px 1px rgba(0,0,0,.075), 0 0 8px rgba(102,175,233,.6);
      border-color: #66afe9;
    }
  </style>

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
      <h1>담당자 관리
        <button type="button" class="btn btn-primary btn-sm pull-right create-btn">담당자 지정하기</button>
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
                  <th>이름</th>
                  <th>이메일</th>
                  <th>소속</th>
                  <th>실국</th>
                  <th>부서</th>
                  <th>분류</th>
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
<div class="modal fade" id="modal-manager">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">×</span></button>
        <h4 class="modal-title">담당자</h4>
      </div>
      <form role="form" id="form-manager" class="form-horizontal">
        <input type="hidden" id="is-new-manager">
        <div class="modal-body">
          <div class="form-group">
            <label title="department1" class="col-sm-2 control-label">소속<span> *</span></label>
            <div class="col-sm-10">
              <input type="text" class="form-control input-sm" name="department1" autocomplete="off"
                     data-parsley-required="true" data-parsley-maxlength="30" data-parsley-whitespace="trim">
            </div>
          </div>
          <div class="form-group">
            <label title="department3" class="col-sm-2 control-label">실국<span> *</span></label>
            <div class="col-sm-10">
              <input type="text" class="form-control input-sm" name="department3" autocomplete="off"
                     data-parsley-required="true" data-parsley-maxlength="30" data-parsley-whitespace="trim">
            </div>
          </div>
          <div class="form-group">
            <label title="department2" class="col-sm-2 control-label">부서<span> *</span></label>
            <div class="col-sm-10">
              <input type="text" class="form-control input-sm" name="department2" autocomplete="off"
                     data-parsley-required="true" data-parsley-maxlength="30" data-parsley-whitespace="trim">
            </div>
          </div>
          <div class="form-group">
            <label title="category" class="col-sm-2 control-label">분류<span> *</span></label>
            <div class="col-sm-10">
              <select name="category" class="form-control input-sm" data-parsley-required="true">
                <option value="">분류선택</option>
                <c:forEach var="category" items="${categories}">
                  <option value="${category.name}">${category.name}</option>
                </c:forEach>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label title="name" class="col-sm-2 control-label">회원<span> *</span></label>
            <div class="col-sm-5">
              <p class="form-control-static" id="assigned-manager"></p>
              <div id="userIdError"></div>
            </div>
            <div class="col-sm-5" id="assign-manger-wrapper">
              <div class="input-group input-group-sm">
                <select id="select-manager-input" class="form-control"></select>
                <span class="input-group-btn">
                  <button type="button" class="btn btn-default" id="assign-manager-btn">지정하기</button>
                </span>
              </div>
              <input type="text" class="hidden" name="userId"
                     data-parsley-required="true" data-parsley-errors-container="#userIdError">
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default btn-sm pull-left" id="btn-manager-remove">담당자삭제</button>
          <button type="button" class="btn btn-primary btn-sm" id="btn-manager-save">저장하기</button>
        </div>
      </form>
    </div>
  </div>
</div>


<script>
  $(function () {
    var $modalManager = $('#modal-manager');
    var $assignManagerWrapper = $('#assign-manger-wrapper');
    var $formManager = $('#form-manager');
    $formManager.parsley(parsleyConfig);

    var $selectManagerInput = $('#select-manager-input');
    $selectManagerInput.select2({
      language: 'ko',
      theme: "bootstrap",
      ajax: {
        headers: { 'X-CSRF-TOKEN': '${_csrf.token}' },
        url: '/admin/ajax/users/role-user',
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
                text: item.name,
                item: item
              }
            })
          };
        }
      }
    });

    // 담당자 지정
    $('#assign-manager-btn').click(function() {
      var selectedData = $selectManagerInput.select2('data');
      if (selectedData.length === 0) {
        alert('선택된 항목이 없습니다.');
        return;
      }

      $('#assigned-manager').text(selectedData[0].text);
      $('input[name=userId]').val(selectedData[0].id);
      $selectManagerInput.val(null).trigger('change');
      $('input[name=userId]').parsley().validate();
    });

    $('.create-btn').click(function () {
      $selectManagerInput.val(null).trigger('change');
      $('#is-new-manager').val('true');
      $('#assigned-manager').text('');
      $('#btn-manager-remove').addClass('hidden');
      $assignManagerWrapper.removeClass('hidden');
      $formManager[0].reset();
      $formManager.parsley().reset();
      $modalManager.modal('show');
    });

    $(document).on('click', '.update-btn', function () {
      $selectManagerInput.val(null).trigger('change');
      $('#is-new-manager').val('false');
      $('#btn-manager-remove').removeClass('hidden');
      $assignManagerWrapper.addClass('hidden');
      $formManager[0].reset();
      $formManager.parsley().reset();

      var data = table.row($(this).parents('tr')).data();
      $('input[name=userId]').val(data.id);
      $('#assigned-manager').text(data.name);
      $('input[name=department1]').val(data.department1);
      $('input[name=department2]').val(data.department2);
      $('input[name=department3]').val(data.department3);
      $('select[name=category]').val(data.category.name);

      $modalManager.modal('show');
    });

    $('#btn-manager-save').click(function () {
      if (!$formManager.parsley().validate()) return;

      var isNew = $('#is-new-manager').val();
      var data = $formManager.serializeObject();
      adminAjax({
        csrf: '${_csrf.token}',
        url: '/admin/ajax/users/managers/' + (isNew === 'true' ? '' : data.userId),
        type: (isNew === 'true' ? 'POST' : 'PUT'),
        data: data,
        success: function (data) {
          table.draw();
          $modalManager.modal('hide');
        },
        error: function () {
        }
      });
    });

    $('#btn-manager-remove').click(function () {
      if (!confirm('담당자를 삭제할까요?')) return;

      var id = $('input[name=userId]').val();
      adminAjax({
        csrf: '${_csrf.token}',
        url: '/admin/ajax/users/managers/' + id,
        type: 'DELETE',
        data: null,
        success: function (data) {
          table.draw();
          $modalManager.modal('hide');
        },
        error: function () {
        }
      });
    });

    var table = $('#list')
      .on('preXhr.dt', function (e, settings, data) {
        data['page'] = data.start / data.length + 1;
        data['size'] = data.length;
        data['sort'] = ['name,asc'];
        data['search'] = data['search'].value;

        delete data['draw'];
        delete data['columns'];
        delete data['order'];
        delete data['start'];
        delete data['length'];
      })
      .on('xhr.dt', function (e, settings, json, xhr) {
        json['recordsFiltered'] = json.totalElements;
        json['recordsTotal'] = json.totalElements;
        json['data'] = json.content;
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
          'searchPlaceholder': '이름, 이메일 검색',
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
        search: '',
        searchDelay: 500,
        ordering: false,
        serverSide: true,
        ajax: {
          url: '/admin/ajax/users/role-manager',
          type: 'GET',
          error: function (e) {
            if(e.status === 401 || e.status === 403)
              window.location.reload();
            else
              window.location.href = '/admin/index.do';
          }
        },
        columns: [
          { data: 'name', orderable: false },
          { data: 'email', orderable: false },
          { data: 'department1', orderable: false },
          { data: 'department2', orderable: false },
          { data: 'department3', orderable: false },
          { data: 'category.name', orderable: false },
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

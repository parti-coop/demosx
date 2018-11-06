<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>회원 관리 - Democracy</title>
  <%@ include file="../shared/head.jsp" %>
  <link rel="stylesheet" type="text/css" href="<c:url value="/css/dataTables.bootstrap.min.css"/>"/>
  <script type="text/javascript" src="<c:url value="/js/jquery.dataTables.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/dataTables.bootstrap.min.js"/>"></script>
</head>
<body class="hold-transition skin-black-light fixed sidebar-mini admin">

<div class="wrapper">
  <%@ include file="../shared/header.jsp" %>

  <div class="content-wrapper">
    <section class="content-header">
      <h1>회원 관리</h1>
    </section>

    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-body">
              <table id="list" class="table table-bordered table-striped" width="100%">
                <thead>
                <tr>
                  <th>가입일</th>
                  <th>이름</th>
                  <th>이메일</th>
                  <th>마지막 로그인 IP</th>
                  <th>마지막 로그인 일시</th>
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

<div class="modal fade" id="modal-cancel">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">×</span></button>
        <h4 class="modal-title">탈퇴처리</h4>
      </div>
      <div class="modal-body">
        <form role="form">
          <input type="hidden" name="eventId" id="eventId" value=""/>
          <div class="form-group">
            <div class="radio">
              <label>
                <input type="radio" name="reason" value="1">
                1) 적절치 않은 내용(불법/도박/음란/사행성/정치/종교 등)에 의한 이벤트 블럭
              </label>
            </div>
          </div>
          <div class="form-group">
            <div class="radio">
              <label>
                <input type="radio" name="reason" value="2">
                2) 개설자의 사유로 인한 이벤트 취소
                <input class="form-control input-sm" type="text" placeholder="사유">
              </label>
            </div>
          </div>
          <div class="form-group">
            <div class="radio">
              <label>
                <input type="radio" name="reason" value="3">
                3) 기타
                <input class="form-control input-sm" type="text" placeholder="사유">
              </label>
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default btn-sm pull-left" data-dismiss="modal">닫기</button>
        <button type="button" class="btn btn-primary btn-sm event-cancel-btn">취소하기</button>
      </div>
    </div>
    <!-- /.modal-content -->
  </div>
  <!-- /.modal-dialog -->
</div>

<script>
  $(function () {
    var sortColumn = ['createdDate', null, null, null, 'stats.eventCount', 'stats.eventClosedCount', 'stats.eventJoinCount', null, null];
    var table = $('#list')
      .on('draw.dt', function () {
        $('[data-toggle="tooltip"]').tooltip({ placement: 'left', html: true });
      })
      .on('preXhr.dt', function (e, settings, data) {
        console.log(data);
        data['page'] = data.start / data.length + 1;
        data['size'] = data.length;
        data['sort'] = [sortColumn[data['order'][0].column] + ',' + data['order'][0].dir, 'name,asc'];
        data['search'] = data['search'].value;

        delete data['draw'];
        delete data['columns'];
        //delete data['order'];
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
        order: [[0, 'desc']],
        serverSide: true,
        ajax: {
          url: '/admin/ajax/users',
          type: 'GET',
          error: function (e) {
            window.location.href = '/admin/index.do';
          }
        },
        columns: [
          { data: 'createdDate' },
          { data: 'name', orderable: false },
          { data: 'email', orderable: false },
          {
            data: function (item) {
              return item.loginIp || '';
            }, orderable: false
          },
          {
            data: function (item) {
              return item.loginDate || '';
            }, orderable: false
          }
        ]
      });

    var $cancelModal = $('#modal-cancel');
    var $eventId = $('#eventId');
    $(document).on('click', '.event-cancel', function () {
      $('input[name=reason]:checked', '#modal-cancel').prop('checked', false);
      $eventId.val($(this).data('id'));
      $cancelModal.modal('show');
    });
    $('.event-cancel-btn').click(function () {
      var $reason = $('input[name=reason]:checked', '#modal-cancel');
      var selected = $reason.val();
      if (!selected) {
        alert('사유를 선택해 주세요.');
        return;
      }

      var msg = '';
      var reasonText = '';
      if (selected === '1') msg = '1) 적절치 않은 내용(불법/도박/음란/사행성/정치/종교 등)에 의한 이벤트 블럭';
      else if (selected === '2') {
        reasonText = $reason.siblings('input[type=text]').val().trim();
        if (!reasonText) {
          alert('사유를 입력해 주세요.');
          return;
        }
        msg = '2) 개설자의 사유로 인한 이벤트 취소 : ' + reasonText;
      } else if (selected === '3') {
        reasonText = $reason.siblings('input[type=text]').val().trim();
        if (!reasonText) {
          alert('사유를 입력해 주세요.');
          return;
        }
        msg = '3) 기타 : ' + reasonText;
      }

      var eventId = $eventId.val()
      $.ajax({
        headers: { 'X-CSRF-TOKEN': '${_csrf.token}' },
        url: '/admin/ajax/events/' + eventId,
        type: 'PUT',
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify({
          id: eventId,
          reason: msg
        }),
        success: function (data) {
          table.ajax.reload(null, false);
          $cancelModal.modal('hide');
        },
        error: function (error) {
          if (error.status === 403) {
            window.location.reload();
            return;
          }
          if (error.responseJSON.fieldErrors) {
            var msg = error.responseJSON.fieldErrors.map(function (item) {
              return item.fieldError;
            }).join('/n');
            alert(msg);
          } else alert(error.responseJSON.msg);
        }
      });
    });
  });
</script>
<style>
  .table > tbody > tr > td, .table > tbody > tr > th, .table > tfoot > tr > td, .table > tfoot > tr > th, .table > thead > tr > td, .table > thead > tr > th {
    vertical-align: middle;
  }

  .modal label {
    width: 100%;
  }

  span.label {
    font-size: 90%;
  }
</style>

</body>
</html>

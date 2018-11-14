<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table id="opinion" class="table table-bordered table-striped" width="100%">
  <thead>
  <tr>
    <th>작성일</th>
    <th>공감수</th>
    <th>투표</th>
    <th>의견</th>
    <th>작성자</th>
    <th>관리</th>
  </tr>
  </thead>
</table>
<script>
  $(function () {
    var sortColumn = ['createdDate'];
    var table = $('#opinion')
      .on('preXhr.dt', function (e, settings, data) {
        data['page'] = data.start / data.length + 1;
        data['size'] = data.length;
        data['sort'] = [sortColumn[data['order'][0].column] + ',' + data['order'][0].dir];
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
        searching: false,
        order: [[0, 'desc']],
        serverSide: true,
        ajax: {
          url: '/admin/ajax/opinions?issueId=${param.issueId}',
          type: 'GET',
          error: function (e) {
            window.location.href = '/admin/index.do';
          }
        },
        columns: [
          { data: 'createdDate' },
          { data: 'likeCount', orderable: false },
          {
            data: function (item) {
              if (item.vote === 'YES') return '찬성';
              if (item.vote === 'NO') return '반대';
              return '기타';
            }, orderable: false
          },
          { data: 'content', orderable: false },
          { data: 'createdBy.name', orderable: false },
          {
            data: function (item) {
              <c:if test="${loginUser.isManager()}">
              if (item.status === 'DELETE') return '삭제';
              if (item.status === 'BLOCK') return '비공개';
              return '공개';
              </c:if>
              <c:if test="${loginUser.isAdmin()}">
              if (item.status === 'DELETE') return '삭제';

              return '<select class="form-control input-sm opinion-status-select" data-status="' + item.status + '" data-id="' + item.id + '">' +
                '<option value="OPEN"' + (item.status === 'OPEN' ? ' selected' : '') + '>공개</option>' +
                '<option value="BLOCK"' + (item.status === 'BLOCK' ? ' selected' : '') + '>비공개</option>' +
                '</select>';
              </c:if>
            }, orderable: false
          }
        ]
      });

    <c:if test="${param.opinionType eq 'PROPOSAL'}">
    table.column(2).visible(false);
    </c:if>

    <c:if test="${loginUser.isAdmin()}">
    $(document).on('change', '.opinion-status-select', function () {
      var status = $(this).val();
      if (!confirm((status === 'OPEN' ? '공개' : '비공개') + '로 변경할까요?')) {
        $(this).val($(this).data('status'));
        return;
      }

      var that = $(this);
      adminAjax({
        csrf: '${_csrf.token}',
        url: '/admin/ajax/opinions/' + $(this).data('id') + '/' + status.toLowerCase(),
        type: 'PATCH',
        data: null,
        success: function () {
          that.data('status', status);
        },
        error: function () {
          that.val(that.data('status'));
        }
      });
    });
    </c:if>
  });
</script>
<style>
  .table > tbody > tr > td, .table > tbody > tr > th, .table > tfoot > tr > td, .table > tfoot > tr > th, .table > thead > tr > td, .table > thead > tr > th {
    vertical-align: middle;
  }
</style>
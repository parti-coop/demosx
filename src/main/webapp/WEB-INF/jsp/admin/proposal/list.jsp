<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>제안 관리 - Democracy</title>
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
      <h1>제안 관리</h1>
    </section>

    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-body">
              <table id="list" class="table table-bordered table-striped" width="100%">
                <thead>
                <tr>
                  <th>작성일</th>
                  <th>
                    <select name="category" aria-controls="list" class="form-control input-sm" title="분류">
                      <option value="">분류선택</option>
                      <c:forEach var="category" items="${categories}">
                        <option value="${category.name}">${category.name}</option>
                      </c:forEach>
                    </select>
                  </th>
                  <th>제목</th>
                  <th>작성자</th>
                  <th>조회수</th>
                  <th>공감수</th>
                  <th>댓글수</th>
                  <th>
                    <select name="process" aria-controls="list" class="form-control input-sm" title="분류">
                      <option value="">진행사항</option>
                      <option value="INIT">일반</option>
                      <option value="NEED_ASSIGN">담당지정대기</option>
                      <option value="ASSIGNED">답변대기</option>
                      <option value="COMPLETE">부서답변완료</option>
                    </select>
                  </th>
                  <th>담당자</th>
                  <th>공개여부</th>
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

<script>
  $(function () {
    var sortColumn = ['createdDate'];
    var $category = $('select[name=category]');
    $category.change(function () {
      table.draw();
    });

    var $process = $('select[name=process]');
    $process.change(function () {
      table.draw();
    });

    var table = $('#list')
      .on('preXhr.dt', function (e, settings, data) {
        console.log(data);
        data['page'] = data.start / data.length + 1;
        data['size'] = data.length;
        data['sort'] = [sortColumn[data['order'][0].column] + ',' + data['order'][0].dir];
        data['search'] = data['search'].value;
        var category = $category.val();
        if (category) data['category'] = category;
        var process = $process.val();
        if (process) data['process'] = process;

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
        search: '',
        searchDelay: 500,
        order: [[0, 'desc']],
        serverSide: true,
        ajax: {
          url: '/admin/ajax/issue/proposals',
          type: 'GET',
          error: function (e) {
            if(e.status === 401 || e.status === 403)
              window.location.reload();
            else
              window.location.href = '/admin/index.do';
          }
        },
        columns: [
          { data: 'createdDate' },
          {
            data: function (item) {
              return (item.category) ? item.category.name : '-';
            }, orderable: false
          },
          {
            data: function (item) {
              return '<a href="/admin/issue/proposal-detail.do?id=' + item.id + '">' + item.title + '</a>';
            }, orderable: false
          },
          { data: 'createdBy.name', orderable: false },
          { data: 'stats.viewCount', orderable: false },
          { data: 'stats.likeCount', orderable: false },
          { data: 'stats.opinionCount', orderable: false },
          {
            data: function (item) {
              if (item.process === 'NEED_ASSIGN') return '담당지정대기';
              if (item.process === 'ASSIGNED') return '답변대기';
              if (item.process === 'COMPLETE') return '부서답변완료';
              return '일반';
            }, orderable: false
          },
          {
            data: function (item) {
              return item.manager.name || '';
            }, orderable: false
          },
          {
            data: function (item) {
              if (item.status === 'CLOSED') return '비공개';
              if (item.status === 'DELETE') return '삭제';
              if (item.status === 'BLOCK') return '비공개';
              return '공개';
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

  .modal label {
    width: 100%;
  }

  span.label {
    font-size: 90%;
  }
</style>

</body>
</html>

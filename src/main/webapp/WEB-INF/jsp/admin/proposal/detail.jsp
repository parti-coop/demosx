<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>제안 관리 상세 - Democracy</title>
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
      <h1>제안 관리 상세</h1>
    </section>

    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header with-border">
              <h3 class="box-title">제안</h3>
            </div>
            <form class="form-horizontal">
              <div class="box-body">
                <div class="form-group">
                  <label class="col-sm-2 control-label">분류</label>
                  <div class="col-sm-2">
                    <select class="form-control input-sm" id="category-select">
                      <option value="">분류선택</option>
                      <c:forEach var="category" items="${categories}">
                        <option value="${category.name}" <c:if
                            test="${proposal.category.name eq category.name}">selected</c:if>>${category.name}</option>
                      </c:forEach>
                    </select>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">제목</label>
                  <div class="col-sm-10"><p class="form-control-static">${proposal.title}</p></div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">작성일</label>
                  <div class="col-sm-10"><p class="form-control-static">${proposal.createdDate}</p></div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">작성자</label>
                  <div class="col-sm-10">
                    <p class="form-control-static">${proposal.createdBy.name} / ${proposal.createdBy.email}</p>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">조회수</label>
                  <div class="col-sm-10"><p class="form-control-static">${proposal.stats.viewCount}</p></div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">공감수</label>
                  <div class="col-sm-10"><p class="form-control-static">${proposal.stats.likeCount}</p></div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">댓글수</label>
                  <div class="col-sm-10"><p class="form-control-static">${proposal.stats.opinionCount}</p></div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">진행사항</label>
                  <div class="col-sm-10"><p class="form-control-static">${proposal.process.msg}</p></div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">공개여부</label>
                  <div class="col-sm-2">
                    <c:if test="${proposal.status.isDelete()}">
                      <p class="form-control-static">${proposal.status.msg}</p>
                    </c:if>
                    <c:if test="${proposal.status ne 'DELETE'}">
                      <select class="form-control input-sm">
                        <option value="OPEN" <c:if test="${proposal.status.isOpen()}">selected</c:if>>공개</option>
                        <option value="OPEN" <c:if test="${proposal.status.isClosed()}">selected</c:if>>비공개</option>
                      </select>
                    </c:if>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">내용</label>
                  <div class="col-sm-10"><p class="form-control-static">${proposal.content}</p></div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">관리자 댓글</label>
                  <div class="col-sm-10">
                    <textarea class="form-control" id="admin-comment-textarea" rows="4">${proposal.adminComment}</textarea>
                    <button type="button" class="btn btn-default btn-sm pull-right" id="admin-comment-btn">저장하기</button>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">담당자 지정</label>
                  <div class="col-sm-10">
                    <button class="btn btn-default btn-sm">지정하기</button>
                    <p class="form-control-static">${propsal.manager.name}</p>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">담당자 답변</label>
                  <div class="col-sm-10">
                    <p class="form-control-static">${propsal.managerComment}</p>
                  </div>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header with-border">
              <h3 class="box-title">댓글</h3>
            </div>
            <div class="box-body">
              <table id="opinion" class="table table-bordered table-striped" width="100%">
                <thead>
                <tr>
                  <th>작성일</th>
                  <th>공감수</th>
                  <th>의견</th>
                  <th>작성자</th>
                  <th>관리</th>
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
  $(function() {
    var $adminCommentBtn = $('#admin-comment-btn');
    $adminCommentBtn.click(function() {
      if(!window.confirm('관리자 댓글을 저장할까요?')) return;

      var comment = $adminCommentBtn.val().trim();
      console.log(comment);
    });

    var categoryValue = '${proposal.category.name}';
    var $categorySelect = $('#category-select');
    $categorySelect.change(function($event) {
      if(!confirm('분류를 변경할까요?')) {
        $(this).val(categoryValue);
        return;
      }
      categoryValue = $(this).val();

    })
  });
</script>

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
        search: '',
        searchDelay: 500,
        order: [[0, 'desc']],
        serverSide: true,
        ajax: {
          url: '/admin/ajax/opinions?issueId=${proposal.id}',
          type: 'GET',
          error: function (e) {
            window.location.href = '/admin/index.do';
          }
        },
        columns: [
          { data: 'createdDate' },
          { data: 'likeCount', orderable: false},
          { data: 'content', orderable: false },
          { data: 'createdBy.name', orderable: false },
          {
            data: function (item) {
              if (item.status === 'DELETE') return '삭제';
              if (item.status === 'OPEN') return '공개';
              if (item.status === 'BLOCK') return '비공개';
              return '';
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

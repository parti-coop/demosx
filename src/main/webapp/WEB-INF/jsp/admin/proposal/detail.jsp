<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>제안 관리 상세 - Democracy</title>
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
                    <c:if test="${loginUser.isManager()}">
                      <p class="form-control-static">${proposal.category.name}</p>
                    </c:if>
                    <c:if test="${loginUser.isAdmin()}">
                      <select class="form-control input-sm" id="category-select">
                        <option value="">분류선택</option>
                        <c:forEach var="category" items="${categories}">
                          <option value="${category.name}" <c:if
                              test="${proposal.category.name eq category.name}">selected</c:if>>${category.name}</option>
                        </c:forEach>
                      </select>
                    </c:if>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">타입</label>
                  <div class="col-sm-2">
                    <c:if test="${loginUser.isManager()}">
                      <p class="form-control-static">${proposal.proposalType.msg}</p>
                    </c:if>
                    <c:if test="${loginUser.isAdmin()}">
                      <select class="form-control input-sm" id="proposal-type-select">
                        <option value="">타입선택</option>
                        <option value="PROPOSAL"<c:if test="${proposal.proposalType eq 'PROPOSAL'}"> selected</c:if>>
                          제안
                        </option>
                        <option value="COMPLAINT"<c:if test="${proposal.proposalType eq 'COMPLAINT'}"> selected</c:if>>
                          민원
                        </option>
                      </select>
                    </c:if>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">제목</label>
                  <div class="col-sm-10"><p class="form-control-static">${proposal.title}</p></div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">작성일</label>
                  <div class="col-sm-10"><p class="form-control-static">${proposal.createdDate.toLocalDate()}</p></div>
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
                    <c:if test="${loginUser.isManager() or proposal.status.isDelete()}">
                      <p class="form-control-static">${proposal.status.msg}</p>
                    </c:if>
                    <c:if test="${loginUser.isAdmin() and proposal.status ne 'DELETE'}">
                      <select class="form-control input-sm" id="status-select">
                        <option value="OPEN" <c:if test="${proposal.status.isOpen()}">selected</c:if>>공개</option>
                        <option value="CLOSED" <c:if test="${proposal.status.isClosed()}">selected</c:if>>비공개</option>
                      </select>
                    </c:if>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">내용</label>
                  <div class="col-sm-10"><p class="form-control-static">${proposal.contentWithBr()}</p></div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">관리자 댓글</label>
                  <div class="col-sm-10">
                    <c:if test="${loginUser.isAdmin() and proposal.status.isOpen()}">
                      <textarea class="form-control" id="admin-comment-textarea"
                                rows="4">${proposal.adminComment}</textarea>
                      <button type="button" class="btn btn-default btn-sm pull-right" id="admin-comment-btn">저장하기
                      </button>
                    </c:if>
                    <c:if test="${loginUser.isManager() or not proposal.status.isOpen()}">
                      <p class="form-control-static">${proposal.adminComment}</p>
                    </c:if>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">담당자 지정</label>
                  <div class="col-sm-5">
                    <p class="form-control-static" id="assigned-manager">
                      <c:if test="${not empty proposal.manager.id}">
                        ${proposal.manager.name}( ${proposal.manager.department1} / ${proposal.manager.department2} / ${proposal.manager.department3} )
                      </c:if>
                    </p>
                  </div>
                  <div class="col-sm-5">
                    <c:if
                        test="${loginUser.isAdmin() and proposal.status.isOpen()
                        and (proposal.process.isNeedAssign() or proposal.process.isAssigned())}">
                      <div class="input-group input-group-sm">
                        <select id="select-manager-input" class="form-control"></select>
                        <span class="input-group-btn">
                          <button type="button" class="btn btn-default" id="assign-manager-btn">지정하기</button>
                        </span>
                      </div>
                    </c:if>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">담당자 답변</label>
                  <div class="col-sm-10">
                    <c:if test="${loginUser.isAdmin() or not proposal.status.isOpen()}">
                      <p class="form-control-static">${proposal.managerComment}</p>
                    </c:if>
                    <c:if test="${loginUser.isManager() and proposal.status.isOpen()}">
                      <textarea class="form-control" id="admin-comment-textarea"
                                rows="4">${proposal.managerComment}</textarea>
                      <button type="button" class="btn btn-default btn-sm pull-right" id="manager-comment-btn">저장하기
                      </button>
                    </c:if>
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
              <jsp:include page="../opinion/list.jsp">
                <jsp:param name="issueId" value="${proposal.id}"/>
                <jsp:param name="opinionType" value="PROPOSAL"/>
              </jsp:include>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
  <%@ include file="../shared/footer.jsp" %>
</div>
<c:if test="${loginUser.isManager()}">
  <script>
    $('#manager-comment-btn').click(function () {
      if (!window.confirm('담당자 댓글을 저장할까요?')) return;

      var comment = $('#admin-comment-textarea').val();
      adminAjax({
        csrf: '${_csrf.token}',
        url: '/admin/ajax/issue/proposals/${proposal.id}/managerComment',
        type: 'PATCH',
        data: {
          proposalId: ${proposal.id},
          comment: comment
        },
        success: function () {
        },
        error: function () {
        }
      });
    });
  </script>
</c:if>
<c:if test="${loginUser.isAdmin()}">
  <script>
    $(function () {
      var $selectManagerInput = $('#select-manager-input');
      $selectManagerInput.select2({
        language: 'ko',
        theme: 'bootstrap',
        width: '100%',
        ajax: {
          headers: { 'X-CSRF-TOKEN': '${_csrf.token}' },
          url: '/admin/ajax/users/role-manager',
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
                  text: item.name + ' ( ' + item.department1 + ' / ' + item.department2 + ' / ' + item.department3 + ' )',
                  item: item
                }
              })
            };
          }
        }
      });

      // 담당자 지정
      $('#assign-manager-btn').click(function () {
        var selectedData = $selectManagerInput.select2('data');
        if (selectedData.length === 0) {
          alert('선택된 항목이 없습니다.');
          return;
        }

        adminAjax({
          csrf: '${_csrf.token}',
          url: '/admin/ajax/issue/proposals/${proposal.id}/assignManager',
          type: 'PATCH',
          data: {
            proposalId: ${proposal.id},
            managerId: selectedData[0].id
          },
          success: function (data) {
            $('#assigned-manager').text(selectedData[0].text);
            $selectManagerInput.val(null).trigger('change');
          },
          error: function () {
          }
        });
      });
    });


    $(function () {
      // 관리자 댓글 저장
      $('#admin-comment-btn').click(function () {
        if (!window.confirm('관리자 댓글을 저장할까요?')) return;

        var comment = $('#admin-comment-textarea').val();
        adminAjax({
          csrf: '${_csrf.token}',
          url: '/admin/ajax/issue/proposals/${proposal.id}/adminComment',
          type: 'PATCH',
          data: {
            proposalId: ${proposal.id},
            comment: comment
          },
          success: function () {
          },
          error: function () {
          }
        });
      });

      // 분류 수정
      var categoryValue = '${proposal.category.name}';
      var $categorySelect = $('#category-select');
      $categorySelect.change(function () {
        if (!confirm('분류를 변경할까요?')) {
          $(this).val(categoryValue);
          return;
        }

        adminAjax({
          csrf: '${_csrf.token}',
          url: '/admin/ajax/issue/proposals/${proposal.id}/category',
          type: 'PATCH',
          data: {
            proposalId: ${proposal.id},
            category: $(this).val()
          },
          success: function () {
            categoryValue = $categorySelect.val();
          },
          error: function () {
            $categorySelect.val(categoryValue);
          }
        });
      });

      // 타입 수정
      var proposalTypeValue = '${proposal.proposalType}';
      var $proposalTypeSelect = $('#proposal-type-select');
      $proposalTypeSelect.change(function () {
        if (!confirm('타입을 변경할까요?')) {
          $(this).val(proposalTypeValue);
          return;
        }

        adminAjax({
          csrf: '${_csrf.token}',
          url: '/admin/ajax/issue/proposals/${proposal.id}/proposalType',
          type: 'PATCH',
          data: {
            proposalId: ${proposal.id},
            proposalType: $(this).val()
          },
          success: function () {
            proposalTypeValue = $proposalTypeSelect.val();
          },
          error: function () {
            $proposalTypeSelect.val(proposalTypeValue);
          }
        });
      });

      // 공개여부 수정
      var statusValue = '${proposal.status}';
      var $statusSelect = $('#status-select');
      $statusSelect.change(function () {
        var status = $(this).val();
        if (!confirm((status === 'OPEN' ? '공개' : '비공개') + '로 변경할까요?')) {
          $(this).val(statusValue);
          return;
        }

        adminAjax({
          csrf: '${_csrf.token}',
          url: '/admin/ajax/issue/proposals/${proposal.id}/' + status.toLowerCase(),
          type: 'PATCH',
          data: null,
          success: function () {
            statusValue = $statusSelect.val();
            window.location.reload();
          },
          error: function () {
            $statusSelect.val(statusValue);
          }
        });
      });

    });
  </script>
</c:if>
</body>
</html>

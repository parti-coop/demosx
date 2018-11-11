<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>실행 관리 - 상세 - Democracy</title>
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
      <h1>실행 관리 - 상세 <a href="<c:url value="/admin/issue/action-edit.do?id=${action.id}"/>"
                        class="btn btn-primary btn-sm pull-right">수정하기</a></h1>
    </section>

    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header with-border">
              <h3 class="box-title">실행</h3>
            </div>
            <form class="form-horizontal">
              <div class="box-body">
                <div class="form-group">
                  <label class="col-sm-2 control-label">썸네일</label>
                  <div class="col-sm-4">
                    <img src="${action.thumbnail}">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">분류</label>
                  <div class="col-sm-10"><p class="form-control-static">${action.category.name}</p></div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">제목</label>
                  <div class="col-sm-10"><p class="form-control-static">${action.title}</p></div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">작성일</label>
                  <div class="col-sm-10"><p class="form-control-static">${action.createdDate.toLocalDate()}</p></div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">작성자</label>
                  <div class="col-sm-10">
                    <p class="form-control-static">${action.createdBy.name} / ${action.createdBy.email}</p>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">조회수</label>
                  <div class="col-sm-10"><p class="form-control-static">${action.stats.viewCount}</p></div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">공개여부</label>
                  <div class="col-sm-10"><p class="form-control-static">${action.status.msg}</p></div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">첨부파일</label>
                  <div class="col-sm-10">
                    <c:forEach var="file" items="${action.files}">
                      <p class="form-control-static">${file.name}</p>
                    </c:forEach>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">내용</label>
                  <div class="col-sm-10">
                    <div class="form-control-static">${action.content}</div>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">연관제안</label>
                  <div class="col-sm-10">
                    <c:forEach var="relation" items="${action.relations}" varStatus="status">
                      <c:set var="issue" value="${action.issueMap[relation]}"/>
                      <c:if test="${issue.type eq 'P'}">
                        <p class="form-control-static">${issue.title}</p>
                      </c:if>
                    </c:forEach>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label">연관토론</label>
                  <div class="col-sm-10">
                    <c:forEach var="relation" items="${action.relations}" varStatus="status">
                      <c:set var="issue" value="${action.issueMap[relation]}"/>
                      <c:if test="${issue.type eq 'D'}">
                        <p class="form-control-static">${issue.title}</p>
                      </c:if>
                    </c:forEach>
                  </div>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </section>
  </div>
  <%@ include file="../shared/footer.jsp" %>
</div>
</body>
</html>

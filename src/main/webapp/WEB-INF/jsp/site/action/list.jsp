<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>실행 - 민주주의 서울</title>
  <%@ include file="../shared/head.jsp" %>
</head>
<body class="home">
<%@ include file="../shared/header.jsp" %>

<div class="container">
  <div class="top-row clearfix">
    <div class="top-left">
      <h3 class="top-row__title">실행</h3>
    </div>
  </div>

  <div class="sorting-tab sorting-tab--execution clearfix">
    <form name="search">
      <div class="sorting-tab__select">
        <select class="form-control" name="category" title="분류">
          <option value="">분류</option>
          <c:forEach var="item" items="${categories}">
            <option value="${item.name}" <c:if test="${category eq item.name}">selected</c:if>>${item.name}</option>
          </c:forEach>
        </select>
      </div>
    </form>
  </div>
  <div class="full-img-card-wrapper">
    <ul class="full-img-card-ul">
      <c:forEach var="action" items="${page.content}">
        <li class="full-img-card-li">
          <div class="full-img-card">
            <a href="<c:url value="/action.do?id=${action.id}"/>" class="full-img-card__link">
              <div class="full-img-card__bg" style="background-image: url(${action.thumbnail})">
                <div class="full-img-card__bottom-bg">
                  <h5 class="full-img-card__title">${action.title}</h5>
                </div>
              </div>
            </a>
          </div>
        </li>
      </c:forEach>
    </ul>
  </div>

  <jsp:include page="../shared/pagination.jsp">
    <jsp:param name="totalPages" value="${page.totalPages}"/>
    <jsp:param name="current" value="${page.number + 1}"/>
  </jsp:include>
</div>
<script>
  $(function () {
    $('select[name=category]').change(function () {
      $('form[name=search]').submit();
    });
  });
</script>
<%@ include file="../shared/footer.jsp" %>
</body>
</html>

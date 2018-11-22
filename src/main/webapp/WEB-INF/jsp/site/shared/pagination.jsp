<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:if test="${param.totalPages ne 0}">
  <c:set var="queries" value=""/>
  <c:forTokens items="${requestScope['javax.servlet.forward.query_string']}" delims="&" var="q">
    <c:if test="${!fn:startsWith(q, 'page=')}">
      <c:set var="queries" value="${queries}${q}&"/>
    </c:if>
  </c:forTokens>
  <c:set var="url" value="${requestScope['javax.servlet.forward.request_uri']}?${queries}"/>
  <c:set var="minPage" value="${param.current - (param.current - 1) % 5}"/>
  <c:set var="maxPage" value="${param.current - (param.current - 1) % 5 + 4}"/>
  <nav class="demo-pagination" aria-label="page navigation">
    <ul class="pagination pagination--demo">
      <c:choose>
        <c:when test="${minPage eq 1}">
          <li class="page-arrow page-arrow--pre"><span><i class="xi-angle-left"></i></span></li>
        </c:when>
        <c:otherwise>
          <c:url value="${url}" var="link">
            <c:param name="page" value="${minPage - 1}"/>
          </c:url>
          <li class="page-arrow page-arrow--pre">
            <a href="${link}" aria-label="Previous">
              <span aria-hidden="true"><i class="xi-angle-left"></i></span>
            </a>
          </li>
        </c:otherwise>
      </c:choose>

      <c:forEach begin="${minPage}" end="${param.totalPages > maxPage ? maxPage : param.totalPages}" varStatus="status">
        <c:choose>
          <c:when test="${status.current eq param.current}">
            <li class="active"><span>${status.current}</span></li>
          </c:when>
          <c:otherwise>
            <c:url value="${url}" var="link">
              <c:param name="page" value="${status.current}"/>
            </c:url>
            <li><a href="${link}">${status.current}</a></li>
          </c:otherwise>
        </c:choose>
      </c:forEach>

      <c:choose>
        <c:when test="${param.totalPages - param.current > 0 and param.totalPages > maxPage}">
          <c:url value="${url}" var="link">
            <c:param name="page" value="${maxPage + 1}"/>
          </c:url>
          <li class="page-arrow page-arrow--next">
            <a href="${link}" aria-label="Next">
              <span aria-hidden="true"><i class="xi-angle-right"></i></span>
            </a>
          </li>
        </c:when>
        <c:otherwise>
          <li class="page-arrow page-arrow--next"><span><i class="xi-angle-right"></i></span></li>
        </c:otherwise>
      </c:choose>
    </ul>
  </nav>
</c:if>
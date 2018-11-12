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
  <c:set var="minPage" value="${param.current - (param.current - 1) % 8}"/>
  <c:set var="maxPage" value="${param.current - (param.current - 1) % 8 + 7}"/>
  <div class="pagination-wrapper">
    <div class="pagination-btn-group">
      <c:choose>
        <c:when test="${minPage eq 1}">
          <span class="pagination-btn before">&lt;</span>
        </c:when>
        <c:otherwise>
          <c:url value="${url}" var="link">
            <c:param name="page" value="${minPage - 1}"/>
          </c:url>
          <a href="${link}" class="pagination-btn before">&lt;</a>
        </c:otherwise>
      </c:choose>
      <c:forEach begin="${minPage}" end="${(param.totalPages > maxPage) ? maxPage : param.totalPages}"
                 varStatus="status">
        <c:choose>
          <c:when test="${status.current eq param.current}">
            <span class="pagination-btn active">${status.current}</span>
          </c:when>
          <c:otherwise>
            <c:url value="${url}" var="link">
              <c:param name="page" value="${status.current}"/>
            </c:url>
            <a href="${link}" class="pagination-btn">${status.current}</a>
          </c:otherwise>
        </c:choose>
      </c:forEach>
      <c:choose>
        <c:when test="${param.totalPages - param.current > 0 and param.totalPages > maxPage}">
          <c:url value="${url}" var="link">
            <c:param name="page" value="${maxPage + 1}"/>
          </c:url>
          <a href="${link}" class="pagination-btn next">&gt;</a>
        </c:when>
        <c:otherwise>
          <span class="pagination-btn next">&gt;</span>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</c:if>
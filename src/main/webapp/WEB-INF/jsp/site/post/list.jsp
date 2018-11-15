<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>공지사항 - 민주주의 서울</title>
  <%@ include file="../shared/head.jsp" %>
</head>
<body>

<%@ include file="../shared/header.jsp" %>

<div class="container">
  <h3 class="demo-page-title">공지사항</h3>
  <div class="row demo-search-top">
    <div class="col-sm-6 col-sm-offset-6">
      <form>
        <div class="search-form-group">
          <label class="demo-form-label sr-only" for="inputSearch">이름</label>
          <input type="text" class="form-control demo-input" name="search" id="inputSearch" placeholder="제목을 검색해보세요."
                 value="${search}" autocomplete="off">
          <button type="submit" class="search-submit-btn">
            <i class="xi-search">
              <p class="sr-only">돋보기</p>
            </i>
          </button>
        </div>
      </form>
    </div>
  </div>

  <div class="demo-table">
    <div class="demo-row demo-row--th clearfix">
      <div class="demo-th">
        <p class="">제목</p>
      </div>
      <div class="demo-th">
        <p class="">등록일</p>
      </div>
    </div>

    <c:forEach var="post" items="${posts.content}">
      <div class="demo-row clearfix">
        <div class="demo-td">
          <p class="demo-td__title"><a href="<c:url value="/notice.do?id=${post.id}"/>">${post.title}</a></p>
          <div class="demo-title-post-info">
            <p class="td-post-info-p"><i class="xi-eye-o"></i> 조회 <strong>${post.viewCount}</strong>회</p>
          </div>
        </div>
        <div class="demo-td">
          <p class="">${post.createdDate.toLocalDate()}</p>
        </div>
      </div>
    </c:forEach>
  </div>

  <jsp:include page="../shared/pagination.jsp">
    <jsp:param name="totalPages" value="${posts.totalPages}"/>
    <jsp:param name="current" value="${posts.number + 1}"/>
  </jsp:include>

  <nav class="demo-pagination" aria-label="page navigation">
    <ul class="pagination pagination--demo">
      <li class="page-arrow page-arrow--pre">
        <a href="#" aria-label="Previous">
          <span aria-hidden="true"><i class="xi-angle-left"></i></span>
        </a>
      </li>
      <li class="active"><a href="#">1</a></li>
      <li><a href="#">2</a></li>
      <li><a href="#">3</a></li>
      <li><a href="#">4</a></li>
      <li><a href="#">5</a></li>
      <li class="page-arrow page-arrow--next">
        <a href="#" aria-label="Next">
          <span aria-hidden="true"><i class="xi-angle-right"></i></span>
        </a>
      </li>
    </ul>
  </nav>

</div>

<%@ include file="../shared/footer.jsp" %>

</body>
</html>

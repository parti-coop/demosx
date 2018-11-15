<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>${post.title} - 민주주의 서울</title>
  <%@ include file="../shared/head.jsp" %>
</head>
<body>

<%@ include file="../shared/header.jsp" %>

<div class="container">
  <h3 class="demo-detail-title">공지사항</h3>

  <div class="title-box">
    <div class="title-row clearfix">
      <h2 class="detail-title">${post.title}</h2>
    </div>
    <div class="page-info clearfix">
      <p class="td-post-info-p"><i class="xi-eye-o"></i> 조회 <strong>${post.viewCount + 1}</strong>회</p>
      <p class="td-post-info-p"><i class="xi-time"></i> ${post.createdDate.toLocalDate()}</p>
    </div>
  </div>

  <div class="content-box">
    <div class="content-raw">
      ${post.content}
    </div>
  </div>

  <div class="to-list-btns text-center">
    <a class="d-btn white-btn" href="/notice-list.do">목록</a>
  </div>

</div>

<%@ include file="../shared/footer.jsp" %>

</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="demo-card position-relative">
  <c:if test="${param.best}">
    <div class="card-best-tag">
      <img src="<c:url value="/images/best-tag.png"/>" alt="best">
      <span>Best</span>
    </div>
  </c:if>
  <a href="<c:url value="/proposal.do?id=${proposal.id}"/>" class="demo-card__link ">
    <div class="clearfix">
      <div class="demo-card__author pull-right">
        <div class="profile-circle profile-circle--title"
             style="background-image: url(${proposal.createdBy.viewPhoto()})">
          <p class="alt-text">${proposal.createdBy.name}프로필</p>
        </div>
        <p class="title-author__name">${proposal.createdBy.name}</p>
        <p class="title-author__date"><i class="xi-time"></i> ${proposal.createdDate.toLocalDate()}</p>
      </div>
    </div>
    <div class="demo-card__contents">
      <h5 class="demo-card__title">${proposal.title}</h5>
      <p class="demo-card__desc">${proposal.excerpt}</p>
    </div>

    <div class="demo-card__info">
      <p class="demo-card__info__p"><i class="xi-thumbs-up"></i> 공감
        <strong>${proposal.stats.likeCount}</strong>개</p>
      <p class="demo-card__info__p"><i class="xi-message"></i> 댓글
        <strong>${proposal.stats.opinionCount}</strong>개</p>
    </div>

    <div class="demo-progress">
      <div class="progress-container">
        <div class="progress-thumb-wrapper" style="margin-left: ${proposal.stats.likePercentBy500()}%;">
          <img class="progress-thumb-img" src="<c:url value="/images/progress-thumb.png"/>">
        </div>
        <div class="progress-bg">
          <div class="progress-fill-bar" style="width: ${proposal.stats.likePercentBy500()}%;"></div>
          <div class="progress-step-1" style="left: 10%">
            <div class="progress-step-1-text">50명</div>
          </div>
          <div class="progress-step-1-line" style="left: 10%;"></div>
          <p class="progress-step-2">500명</p>
        </div>
      </div>
    </div>
  </a>
</div>
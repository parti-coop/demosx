<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="demo-comment-form clearfix">
  <div class="profile-circle profile-circle--comment-form" style="background-image: url('./img/hong-people.png')">
    <p class="alt-text">홍길동프로필</p></div>
  <form>
          <textarea class="form-control demo-comment-textarea" rows="5" placeholder="로그인 후 댓글을 달 수 있습니다."
                    disabled></textarea>
    <div class="comment-action-group row">
      <div class="comment-count col-sm-6">
        <p class="comment-count-text">0/1000자</p>
      </div>
      <div class="comment-submit text-right col-sm-6">
        <button type="submit" class="demo-submit-btn demo-submit-btn--submit">댓글달기</button>
      </div>
    </div>
  </form>
</div>
<div class="demo-comments-container">
  <div class="demo-comments-top">
    <div class="row">
      <div class="col-sm-6">
        <p class="comments-count"><i class="xi-message"><span class="sr-only">댓글 아이콘</span></i> 댓글
          <strong id="opinion-count"></strong>개</p>
      </div>
      <div class="col-sm-6 text-right">
        <ul class="comment-sorting-ul clearfix">
          <li class="comment-sorting-li active">
            <a class="sorting-link opinion-sort" data-sort="createdDate,desc" href="#">최신순</a>
          </li>
          <li class="comment-sorting-li">
            <a class="sorting-link opinion-sort" data-sort="createdDate,asc" href="#">과거순</a>
          </li>
          <li class="comment-sorting-li">
            <a class="sorting-link opinion-sort" data-sort="likeCount,desc" href="#">공감순</a>
          </li>
        </ul>
      </div>
    </div>
  </div>

  <ul class="demo-comments" id="opinion-list">
  </ul>
  <div class="comment-end-line"></div>
  <div class="show-more-container text-center">
    <button class="white-btn d-btn btn-more hidden" type="button" id="opinion-more">더보기<i class="xi-angle-down-min"></i></button>
  </div>
</div>
<script>
  $(function () {
    var page = 1;
    var sort = 'createdDate,desc';
    var $opinionList = $('#opinion-list');
    var $opinionMore = $('#opinion-more');
    var $opinionCount = $('#opinion-count');
    $('.opinion-sort').click(function (event) {
      event.preventDefault();
      var selectedSort = $(this).data('sort');
      if (selectedSort === sort) return;

      $opinionList.css('height', $opinionList.height());
      $opinionList.text('');
      $('.comment-sorting-li').removeClass('active');
      $(this).parent('.comment-sorting-li').addClass('active');

      page = 1;
      sort = selectedSort;
      getOpinion();
    });

    $opinionMore.click(function() {
      page++;
      getOpinion();
    });

    function getOpinion() {
      $.ajax({
        headers: { 'X-CSRF-TOKEN': '${_csrf.token}' },
        url: '/ajax/opinions',
        contentType: 'application/json',
        data: {
          issueId: ${param.id},
          page: page,
          'sort[]': sort
        },
        success: function (data) {
          console.log(data);
          $opinionCount.text(data.totalElements);
          for (var i = 0; i < data.content.length; i++) {
            var content = makeOpinionString(data.content[i]);
            $opinionList.append(content);
          }
          $opinionList.css('height', 'auto');
          if(data.last) $opinionMore.addClass('hidden');
          else $opinionMore.removeClass('hidden');
        },
        error: function (error) {
          console.log(error);
        }
      });
    }

    function makeOpinionString(opinion) {
      var photo = opinion.createdBy.photo || '/images/noavatar.png';
      return '<li class="comment-li">' +
        '<div class="profile-circle profile-circle--comment" style="background-image: url(' + photo + ')">' +
        '<p class="alt-text">' + opinion.createdBy.name + '사진</p></div>' +
        '<div class="comment-content">' +
        '<p class="comment-name">' + opinion.createdBy.name + '</p>' +
        '<div class="row">' +
        '<div class="col-xs-6"><p class="comment-time"><i class="xi-time"></i> ' + opinion.createdDate + '</p></div>' +
        '<div class="col-xs-6 text-right">' +
        '<p class="comment-thumbs-count"><i class="xi-thumbs-up"></i> 공감 <strong>' + opinion.likeCount + '</strong>개</p>' +
        '</div>' +
        '</div>' +
        '<p class="comment-content-text">' + opinion.content + '</p>' +
        '</div>' +
        '</li>';
    }

    getOpinion();
  });
</script>
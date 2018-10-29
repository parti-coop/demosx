function getUrlParams() {
  var params = {};
  window.location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (str, key, value) {
    params[key] = decodeURIComponent(value);
  });
  return params;
}

$(document).ready(function ($) {
  var $page = $('.pagination');
  var total = +$page.data('total');
  var current = +$page.data('current');
  var sliceNum = 8;

  var startIndex = Math.floor(current / sliceNum) * sliceNum;
  var endIndex = (total < startIndex + sliceNum) ? total : startIndex + sliceNum;

  var html = '';
  // prev
  if (current >= sliceNum) {
    html += '<li><a class="pageNum" href="#" data-pagenum="' + startIndex + '" aria-label="Previous"><span aria-hidden="true">«</span></a></li>';
  }

  for (var i = startIndex; i < endIndex; i++) {
    var pageNum = i + 1;
    if (current === i) {
      html += '<li class="active"><a href="#">' + pageNum + '</a></li>';
    } else {
      html += '<li><a class="pageNum" href="#" data-pagenum="' + pageNum + '">' + pageNum + '</a></li>';
    }
  }

  if (endIndex < total) {
    html += '<li><a class="pageNum" href="#" data-pagenum="' + (endIndex + 1) + '" aria-label="Next"><span aria-hidden="true">»</span></a></li>';
  }
  $page.append(html);

  $(document).on('click', '.pageNum', function (event) {
    event.preventDefault();

    var pageNum = $(this).data('pagenum');
    var query = getUrlParams();
    query['page'] = pageNum;

    window.location.href = window.location.pathname + '?' + $.param(query);
  });
});
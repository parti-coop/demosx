$(function() {
  $('form input').on('keypress', function(e) {
    return e.which !== 13;
  });
});

function adminAjax(param) {
  $.ajax({
    headers: { 'X-CSRF-TOKEN': param.csrf },
    url: param.url,
    type: param.type,
    contentType: 'application/json',
    dataType: 'json',
    data: param.data ? JSON.stringify(param.data) : null,
    success: function (data) {
      param.success(data);
    },
    error: function (error) {
      param.error(error);
      if (error.status === 403) {
        alert('로그인이 필요합니다.');
        window.location.reload();
        return;
      }
      if (error.responseJSON.fieldErrors) {
        var msg = error.responseJSON.fieldErrors.map(function (item) {
          return item.fieldError;
        }).join('/n');
        alert(msg);
      } else alert(error.responseJSON.msg);
    },
    complete: function() {}
  });
}
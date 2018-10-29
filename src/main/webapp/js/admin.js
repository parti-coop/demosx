$(function() {
  $('form input').on('keypress', function(e) {
    return e.which !== 13;
  });
});

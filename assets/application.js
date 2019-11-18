$(function() {
  $("#js-navbar-collapse").on("show.bs.collapse", function(e) {
    $navbar = $(e.currentTarget).closest(".js-navbar");
    $navbar.addClass("bg-white-force");
  });
  $("#js-navbar-collapse").on("hidden.bs.collapse", function(e) {
    $navbar = $(e.currentTarget).closest(".js-navbar");
    $navbar.removeClass("bg-white-force");
  });
});

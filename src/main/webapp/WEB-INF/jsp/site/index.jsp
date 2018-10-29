<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>민주주의 서울</title>

  <link rel="stylesheet" type="text/css"
        href="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.8.1/slick.min.css"/>
  <link rel="stylesheet" type="text/css"
        href="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.8.1/slick-theme.min.css"/>
  <%@ include file="./shared/head.jsp" %>

</head>
<body class="home">
<%@ include file="./shared/header.jsp" %>
Home
<%@ include file="./shared/footer.jsp" %>

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.8.1/slick.min.js"></script>
<script>
  $(function () {
    $('.slider-main').slick({
      dots: true,
      infinite: true,
      adaptiveHeight: true,
      autoplay: true,
      autoplaySpeed: 4000
    });
    $('.slider-event-card').slick({
      infinite: true,
      adaptiveHeight: true,
      slidesToShow: 4,
      slidesToScroll: 4,
      responsive: [
        {
          breakpoint: 992,
          settings: {
            slidesToShow: 3,
            slidesToScroll: 3,
          }
        },
        {
          breakpoint: 768,
          settings: {
            slidesToShow: 1,
            slidesToScroll: 1,
          }
        }
      ],
    });
  });
</script>
</body>
</html>

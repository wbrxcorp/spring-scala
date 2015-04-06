<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><html>
<head>
    <title>Source</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="stylesheet" href="https://netdna.bootstrapcdn.com/bootstrap/latest/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.5/styles/github.min.css">
    <script src="http://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.5/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
    <script>
      (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
      (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
      m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
      })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

      ga('create', 'UA-50657-11', 'auto');
      ga('send', 'pageview');

    </script>
</head>
<body>
<div class="container">
    <h1><c:out value="${path}" /></h1>
    <c:if test="${not empty notation}">
    <h2>説明</h2>
    ${notation.content}
    </c:if>
    <h2>Source</h2>
    <p><a href="https://github.com/wbrxcorp/spring-scala/tree/master/${path}">GitHubで見る</a></p>
    <pre><code>${source}</code></pre>
</div>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><html>
<head>
    <c:choose>
        <c:when test="${not empty title}"><title><c:out value="${title}"/></title></c:when>
        <c:otherwise><title><c:out value="${path}" /></title></c:otherwise>
    </c:choose>
    <c:if test="${not empty description}"><meta name="description" content="<c:out value="${description}"/>"></c:if>
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
<%-- ヘッダ --%>
<div class="navbar navbar-default navbar-static-top">
  <div class="container">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="<%=request.getContextPath()%>/">spring-scala</a>
    </div>
  </div>
</div>
<div class="container">
    <%-- タイトル --%>
    <h1><c:choose>
        <c:when test="${not empty title}"><c:out value="${title}"/></c:when>
        <c:otherwise><c:out value="${path}" /></c:otherwise>
    </c:choose></h1>

    <p>
    <a href="https://twitter.com/share" class="twitter-share-button" data-size="large">Tweet</a>
    <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+'://platform.twitter.com/widgets.js';fjs.parentNode.insertBefore(js,fjs);}}(document, 'script', 'twitter-wjs');</script>
    </p>

    <%-- 説明文（ソースファイルと同じ階層に同名で置いてある .mdファイルをHTMLに変換したもの） --%>
    <c:if test="${not empty description}">
    <p class="lead"><c:out value="${description}"/></p>
    </c:if>

    <c:if test="${not empty content}">
    ${content}
    </c:if>

    <h2>Source</h2>
    <p><a href="https://github.com/wbrxcorp/spring-scala/tree/master/${path}">GitHubで見る</a></p>

    <%-- ソースコードをここにハイライト表示 --%>
    <pre><code>${source}</code></pre>

    <%-- フッタ --%>
    <div class="footer" style="border-top: 1px solid #eee;margin-top: 40px;padding-top: 40px;padding-bottom: 40px;">
        <p>&copy; <a href="http://www.walbrix.com/jp/">ワルブリックス株式会社</a> 2014-2015</p>
    </div>
</div>
</body>
</html>

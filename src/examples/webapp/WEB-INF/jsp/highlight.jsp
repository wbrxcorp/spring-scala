<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><html>
<head>
    <c:choose>
        <c:when test="${not empty title}"><title><c:out value="${title}"/> - spring-scala - ワルブリックス株式会社</title></c:when>
        <c:otherwise><title><c:out value="${path}" /> - spring-scala - ワルブリックス株式会社</title></c:otherwise>
    </c:choose>
    <c:if test="${not empty description}"><meta name="description" content="<c:out value="${description}"/>"></c:if>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="alternate"  type="application/rss+xml" href="<%=request.getContextPath()%>/index.xml" >
    <link rel="stylesheet" href="https://netdna.bootstrapcdn.com/bootstrap/latest/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.5/styles/github.min.css">
    <script src="http://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.5/highlight.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.5/languages/scala.min.js"></script>
    <script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/latest/js/bootstrap.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
    <script>
      (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
      (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
      m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
      })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

      ga('create', 'UA-50657-11', 'auto');
      ga('send', 'pageview');
    </script>
    <script>
        $(function(){
            $("a[data-wikipedia-page]").click(function(event){
                event.preventDefault();
                event.stopPropagation();
                var pageName = $(this).data("wikipedia-page");
                console.log(pageName);
                $("#wikipediaLink").attr("href", "http://ja.wikipedia.org");
                $.getJSON("<%=request.getContextPath()%>/api/wikipedia/" + pageName,
                    {},
                    function(data, status) {
                        console.log(data,status);
                        $("#myModalLabel").text(data.title);
                        $("#myModalBody").text(data.content);
                        if (!!data.canonical) $("#wikipediaLink").attr("href", data.canonical.url);
                    }
                ).fail(function() {
                    // 通信に失敗したらモーダルを隠す
                    $('#myModal').modal('hide');
                });
                $('#myModal').modal('show');
            }).append("<small><span class=\"glyphicon glyphicon-book\"></span></small>")
        });
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

    <div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title"><span class="glyphicon glyphicon-pencil"></span> これを書いている人</h3>
      </div>
      <div class="panel-body">
        <div class="media">
          <div class="media-left">
            <a href="https://twitter.com/shimariso">
              <img class="media-object img-rounded" src="https://pbs.twimg.com/profile_images/450980330233536513/11Z901fy_400x400.jpeg" width="64" height="64" alt="@shimariso">
            </a>
          </div>
          <div class="media-body">
            <h4 class="media-heading">嶋田 大貴 (ワルブリックス株式会社 代表取締役)</h4>
            <strong>「私の年収は<a href="https://twitter.com/shimariso/status/590088659413508096">102万</a>です。ですがもちろんフルパワーで働く気はありませんからご心配なく…」</strong>(<a href="#" data-wikipedia-page="フリーザ">あの人</a>の声で)<br>
            コンピュータを触って30年ほどになるソフトウェアエンジニアです。人月40万円の技術者が一週間かかっても解決できない問題を1日で解決する<a href="#" data-wikipedia-page="トラブルシューティング">トラブルシューティング</a>業務は10万円です。
            お仕事のご依頼・ご相談は<a href="http://www.walbrix.com/jp/">ワルブリックス株式会社</a>または<a href="https://twitter.com/shimariso">代表本人まで直接</a>お気軽にどうぞ。<br>
          </div>
        </div>

      </div>
    </div>

    <h2>ソース (<c:out value="${language}"/>)</h2>
    <p><a href="https://github.com/wbrxcorp/spring-scala/tree/master/${path}">GitHubで見る</a> (もっと良い書き方があるよ！のプルリクお願いします)</p>

    <%-- ソースコードをここにハイライト表示 --%>
    <pre><code<c:if test="${not empty highlight}"> class="<c:out value="${highlight}"/>"</c:if>>${source}</code></pre>

    <%-- フッタ --%>
    <div class="footer" style="border-top: 1px solid #eee;margin-top: 40px;padding-top: 40px;padding-bottom: 40px;">
        <p>&copy; <a href="http://www.walbrix.com/jp/">ワルブリックス株式会社</a> 2014-2015</p>
    </div>
</div>
<!-- Wikipedia表示用モーダルの定義 -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title"><span class="glyphicon glyphicon-book"></span> <span id="myModalLabel">タイトル</span></h4>
            </div>
            <div class="modal-body">
                <p id="myModalBody">...</p>
                <p class="text-right"><a id="wikipediaLink" href="http://ja.wikipedia.org">出典: Wikipedia</a></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove"></span>閉じる</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>

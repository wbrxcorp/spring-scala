<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
    <c:choose>
        <c:when test="${not empty title}">
            <title><c:out value="${title}"/> - spring-scala - ワルブリックス株式会社</title>
            <meta name="twitter:title" content="<c:out value="${title}"/>"/>
        </c:when>
        <c:otherwise>
            <title><c:out value="${path}" /> - spring-scala - ワルブリックス株式会社</title>
        </c:otherwise>
    </c:choose>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <c:if test="${not empty description}">
        <meta name="description" content="<c:out value="${description}"/>">
        <meta name="twitter:description" content="<c:out value="${description}"/>">
    </c:if>
    <meta name="author" content="ワルブリックス株式会社"/>
    <meta name="twitter:card" content="summary">
    <meta name="twitter:site" content="@wbrxcorp">
    <link rel="alternate"  type="application/rss+xml" href="<%=request.getContextPath()%>/index.xml" >
    <link rel="stylesheet" href="https://netdna.bootstrapcdn.com/bootstrap/latest/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://netdna.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.5/styles/github.min.css">
    <style>
    pre, code, .hljs {
        border: none;
        background: #ffffff;
        padding: 0;
        margin: 0;
        width: 100%;
    }
    .scroll-top-wrapper {
        position: fixed;
        opacity: 0;
        visibility: hidden;
        overflow: hidden;
        text-align: center;
        z-index: 99999999;
        background-color: #777777;
        color: #eeeeee;
        width: 50px;
        height: 48px;
        line-height: 48px;
        right: 30px;
        bottom: 30px;
        padding-top: 2px;
        border-top-left-radius: 10px;
        border-top-right-radius: 10px;
        border-bottom-right-radius: 10px;
        border-bottom-left-radius: 10px;
        -webkit-transition: all 0.5s ease-in-out;
        -moz-transition: all 0.5s ease-in-out;
        -ms-transition: all 0.5s ease-in-out;
        -o-transition: all 0.5s ease-in-out;
        transition: all 0.5s ease-in-out;
    }
    .scroll-top-wrapper:hover {
        background-color: #888888;
    }
    .scroll-top-wrapper.show {
        visibility:visible;
        cursor:pointer;
        opacity: 1.0;
    }
    .scroll-top-wrapper i.fa {
        line-height: inherit;
    }
    </style>
    <script src="http://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.5/highlight.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.5/languages/scala.min.js"></script>
    <script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/latest/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-scrolldepth/0.6/jquery.scrolldepth.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.2/moment-with-locales.min.js"></script>
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
            })
            $(document).on('scroll', function(){
                if ($(window).scrollTop() > 100) {
                    $('.scroll-top-wrapper').addClass('show');
                } else {
                    $('.scroll-top-wrapper').removeClass('show');
                }
            });
            $('.scroll-top-wrapper').on('click', scrollToTop);
            moment.lang(navigator.userLanguage || navigator.language);
            $("#timeAgo").text(moment(<c:out value="${lastUpdate.time}"/>).fromNow());
            $.scrollDepth();
        });
        function scrollToTop() {
            verticalOffset = typeof(verticalOffset) != 'undefined' ? verticalOffset : 0;
            element = $('body');
            offset = element.offset();
            offsetTop = offset.top;
            $('html, body').animate({scrollTop: offsetTop}, 500, 'linear');
        }
    </script>
</head>
<body>
<%-- ヘッダ --%>
<div class="navbar navbar-default navbar-static-top">
  <div class="container">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="<%=request.getContextPath()%>/">spring-scala</a>
    </div>
    <div class="collapse navbar-collapse" id="navbar-collapse">
       <div class="col-sm-3 col-md-3 pull-right">
            <form class="navbar-form" role="search" method="post" action="<%=request.getContextPath()%>/api/search">
                <div class="input-group">
                    <input type="text" class="form-control" placeholder="Search" name="q" required>
                    <div class="input-group-btn">
                        <button class="btn btn-default" type="submit"><i class="glyphicon glyphicon-search"></i> 検索</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
  </div>
</div>
<div class="container">
    <%-- タイトル --%>
    <div class="page-header">
        <h1><c:choose>
            <c:when test="${not empty title}"><c:out value="${title}"/></c:when>
            <c:otherwise><c:out value="${path}" /></c:otherwise>
        </c:choose></h1>
        <c:if test="${not empty lastUpdate}">
        <p>最終更新日: <fmt:formatDate value="${lastUpdate}" pattern="yyyy年M月d日"/> (<span id="timeAgo">-</span>)</p>
        </c:if>

        <a href="https://twitter.com/share" class="twitter-share-button" data-size="large">Tweet</a>
        <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+'://platform.twitter.com/widgets.js';fjs.parentNode.insertBefore(js,fjs);}}(document, 'script', 'twitter-wjs');</script>
    </div>


    <%-- 説明文（ソースファイルと同じ階層に同名で置いてある .mdファイルをHTMLに変換したもの） --%>
    <c:if test="${not empty description}">
    <p class="lead"><c:out value="${description}"/></p>
    </c:if>

    <c:if test="${not empty example}">
    <h3>動作例</h3>
    <a href="<%=request.getContextPath()%><c:out value="${example}"/>"><img src="<%=request.getContextPath()%>/api/pagecapture?url=<c:out value="${example}"/>" class="img-thumbnail" image-spinner width="200" height="200" alt="動作例"></a>
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
            <strong>「私の年収は<a href="https://twitter.com/shimariso/status/590088659413508096">102万</a>です。ですがもちろんフルパワーで働く気はありませんからご心配なく…」</strong>(<a href="#" data-wikipedia-page="フリーザ">あの人<span class="glyphicon glyphicon-book"></span></a>の声で)<br>
            コンピュータを触って30年ほどになるソフトウェアエンジニアです。人月40万円の技術者が一週間かかっても解決できない問題を1日で解決する<a href="#" data-wikipedia-page="トラブルシューティング">トラブルシューティング<span class="glyphicon glyphicon-book"></span></a>業務は10万円です。
            お仕事のご依頼・ご相談は<a href="http://www.walbrix.com/jp/">ワルブリックス株式会社</a>または<a href="https://twitter.com/shimariso"><i class="fa fa-twitter"></i>代表本人まで直接</a>お気軽にどうぞ。<br>
          </div>
        </div>

      </div>
    </div>

    <div class="row">
        <div class="col-md-9">
            <div class="panel panel-default">
              <div class="panel-heading">
                <h3 class="panel-title pull-left"><i class="fa fa-code"></i> ソース(<c:out value="${language}"/>)</h3>
                <div class="pull-right"><a href="https://github.com/wbrxcorp/spring-scala/tree/master/${path}"><i class="fa fa-github"></i> GitHubで見る</a> (もっと良い書き方があるよ！のプルリクお願いします)</div>
                <div class="clearfix"></div>
              </div>
              <div class="panel-body">
                <%-- ソースコードをここにハイライト表示 --%>
                <pre><code<c:if test="${not empty highlight}"> class="<c:out value="${highlight}"/>"</c:if>>${source}</code></pre>
            </div>
          </div>
        </div>
        <div class="col-md-3" style="padding-left:0.5em;">
<a class="twitter-timeline" href="https://twitter.com/wbrxcorp" data-widget-id="389601505860255744">@wbrxcorpさんのツイート</a>
<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>        </div>
    </div>

    <%-- disqus --%>
    <div id="disqus_thread"></div>
    <script type="text/javascript">
        /* * * CONFIGURATION VARIABLES * * */
        var disqus_shortname = 'springscala';

        /* * * DON'T EDIT BELOW THIS LINE * * */
        (function() {
            var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
            dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';
            (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
        })();
    </script>
    <noscript>Please enable JavaScript to view the <a href="https://disqus.com/?ref_noscript" rel="nofollow">comments powered by Disqus.</a></noscript>

    <%-- フッタ --%>
    <div class="footer" style="border-top: 1px solid #eee;margin-top: 40px;padding-top: 40px;padding-bottom: 40px;">
        <p>&copy; <a href="http://www.walbrix.com/jp/">ワルブリックス株式会社</a> 2014-2015</p>
    </div>

    <%-- scroll to top --%>
    <div class="scroll-top-wrapper ">
    	<span class="scroll-top-inner">
    		<i class="fa fa-2x fa-arrow-circle-up"></i>
    	</span>
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

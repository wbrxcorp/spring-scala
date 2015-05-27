<?php
// 200以外のHTTPステータスコードを返して停止する関数
function http_error($code, $message = "", $details = "") {
  header(sprintf("HTTP/1.1 %d %s", $code, $message));
  echo sprintf("<html><body><h1>%d %s</h1>%s</body></html>", $code, $message, $details);
  exit;
}

// エラーは常に500 Internal Server Errorで停止させる
function error_handler($errno, $errstr, $errfile, $errline ) {
  http_error(500, "Internal Server Error", $errstr);
}

set_error_handler("error_handler");

// date.timezoneがセットされていない時はAsia/Tokyoをセットする
if (!ini_get('date.timezone')) date_default_timezone_set('Asia/Tokyo');

// PATH_INFOと REQUEST_METHODは必須
if (!isset($_SERVER["PATH_INFO"]) || !isset($_SERVER["REQUEST_METHOD"])) {
  http_error(403, "Forbidden");
}

$path = $_SERVER["PATH_INFO"];
$method = $_SERVER["REQUEST_METHOD"];

$params = Array();  // preg_match_allの結果を受け取るArray

// *.js へのリクエストに対し、複数のjsファイルを結合したjs結果を返す
if (preg_match_all("/^\/(.+)\.js$/", $path, $params)) {
  if ($method != "GET") http_error(405, "Method Not Allowed");

  // *.js の *の部分を js-nameというJavaScriptグローバル変数に代入させる
  $js_name = $params[1][0];
  echo sprintf("if (window['js-name'] === undefined) { window['js-name'] = %s; };\n", json_encode($js_name));

  // 結合するjsファイルのリスト
  $js_files = Array("angular.min.js", "main.js", "ctrl1.js", "ctrl2.js");

  // 対象となるjsファイルの中でもっとも新しいものの最終更新日時を得る
  $last_modified = NULL;
  foreach ($js_files as $js_file) {
    $filename = dirname(__FILE__) . "/" . $js_file;
    if (!file_exists($filename)) http_error(404, sprintf("Not Found '%s'", $js_file));
    $mtime = filemtime($filename);
    if ($last_modified == NULL || $last_modified < $mtime) $last_modified = $mtime;
  }

  header('Content-Type: text/javascript');

  // 最終更新日の出力
  if ($last_modified != NULL) {
    header('Last-Modified: '.gmdate('D, d M Y H:i:s', $mtime).' GMT');
  }

  // If-Modified-Sinceヘッダが送信されてきている場合、jsファイルの最終更新日時と照らしあわせて更新されていなければ304を返す
  if (isset($_SERVER['HTTP_IF_MODIFIED_SINCE']) && $last_modified != NULL && strtotime($_SERVER['HTTP_IF_MODIFIED_SINCE']) >= $last_modified) {
    http_error(304, "Not Modified");
  }

  // jsファイルの本体を次々にレスポンスする
  foreach ($js_files as $js_file) {
    readfile(dirname(__FILE__) . "/" . $js_file);
  }
  exit;
}
//else
http_error(404, "Not Found");
?>
// https://www.softel.co.jp/blogs/tech/archives/2414
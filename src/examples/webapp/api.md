title: PHPで REST的なAPIを作成する
description: REST的なAPIを、フレームワークなどを使用しない素のPHPで作る時のテンプレ。

### このソースからわかること

- PHPで例外ハンドラ関数を登録し、例外時にスタックトレースを表示する方法
- PHPで200以外のHTTPステータスコードを返す方法
- PHPでエラー発生時に実行を継続せず強制的に例外を送出させるようにする方法
- PHPで現在の実行環境が[組み込みWebサーバ (php -S)](http://php.net/manual/ja/features.commandline.webserver.php) かどうか判定する方法
- PHPでログを出力する方法
- PHPでHTTPリクエスト(POSTなど)のボディ部分を読み取る方法
- PHPでJSONをパースする方法
- PHPでUnicode文字をエスケープせずにJSONを出力する方法
- PHPのバージョンを判定する方法
- PHPでPATH_INFOに対して正規表現を用いることでURIからパラメータを抽出する方法
- 実行中のスクリプトが他のPHPスクリプトからインクルードされているかどうかで動作を変える方法 (Pythonの ```if __name__ == '__main__':``` みたいに)

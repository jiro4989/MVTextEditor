#!/bin/bash
# -*- coding: utf-8 -*-

set -eux

out_zip=MVTextEditor.zip
distdir=~/Dropbox/tools/

: JARファイルのビルド
ant || { echo failed build jar; exit 1; }

: ZIPファイルにパッケージング
7z a -tzip $out_zip -r ./mvte.jar ./README.html ./usage.txt ./java_download.html

: ZIPファイルをDropboxにアップ
#mv $out_zip $distdir

: スクリプトは正常に終了しました


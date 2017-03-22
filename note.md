- 折り返しは27.5文字。全角27文字と半角１文字を意味する。

    バッツ
    「適当なテキスト。
      適当なテキスト。
      適当なテキスト。」

    「適当なテキスト。
      適当なテキスト。
      適当なテキスト。
      適当なテキスト。

      適当なテキスト。
      適当なテキスト。
      適当なテキスト。」

    *「ほげほげふがふが
       ほげほげふがふが
       ほげほげふがふが

デフォルトのテキストの処理の記述の仕方はわかりにくいので、タグでテキストを編集す
る。

# コンバートするテキスト

\v[n] : {@val n}
\n[n] : {@actor n}
\p[n] : {@party n}
\g    : {@gold}
\c[n] : {@color n target text}
\i[n] : {@icon n}
\{    : {@sizeUp n target text}
\}    : {@sizeDown n target text}
\\    : {@backslash}
\$    : {@showGoldWindow}
\.    : {@wait n/4}
\|    : {@wait n}
\!    : {@waitInput}
\>    : {@showAll}
\<    : {@showOff}
\^    : {@nonWait}


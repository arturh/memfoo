cut -f7 jlptn5.tsv | sort -u | while read w;
do
set w=${w##}
echo wget -q -U Mozilla -O audio/$w.mp3 "http://translate.google.com/translate_tts?ie=UTF-8&tl=jp&q=$w"
done

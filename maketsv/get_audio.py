import os
import sys
import subprocess
import time
import urllib
import os.path


TTS_URL = """http://translate.google.com/translate_tts?tl=%s&q=%s"""
UA="""Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_7) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.100 Safari/534.30"""

def tts(text, lang):
  return TTS_URL % (lang, urllib.quote_plus(text.encode("utf-8")))

for line in open("jlptn5.tsv"):
    line = line.rstrip()
    parts = line.split("\t")
    kana = parts[2].decode("utf-8")
    audio = parts[6]
    audiofile = "audio/%s.mp3" % audio
    if os.path.exists(audiofile):
        continue
    print "audio:", audio
    subprocess.call(
        ["wget", "-q",
         "-U", UA,
         "-O", audiofile,
         tts(kana, "ja")])

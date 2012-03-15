import re
import sys
import os
import codecs
import csv
import string

from romaji import roma

def roma2(s):
   return ''.join([c for c in roma(s).lower().replace("-", "_")
                           if c in string.lowercase + '_'])

writer = csv.writer(sys.stdout, delimiter="\t")

for line in codecs.open(sys.argv[1], "r", "utf-8"):
	line = line.rstrip()
	id, kanji, kana, grammar, meaning, lesson = line.split("\t")
	audio = roma2(kana)
	writer.writerow([s.strip().encode("utf-8") for s in (id, kanji, kana, grammar, meaning, lesson, audio)])

sys.stdout.flush()

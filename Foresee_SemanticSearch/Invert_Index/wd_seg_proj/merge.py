#! /usr/bin/python
# -*- coding: utf-8 -*-
import sys
from get_stopwords import stopwords
reload(sys)
sys.setdefaultencoding('utf-8')


def merge_id():
    last_word = None
    last_id = None
    for line in sys.stdin.readlines():
        word, this_id = line.strip('\n').split('\t', 1)
        this_id = this_id.strip('\t')
        if word not in stopwords:
            if last_word is None:
                last_word = word
                sys.stdout.write(last_word + '\t' + this_id)
                last_id = this_id
            elif word == last_word:
                if this_id != last_id:
                    sys.stdout.write(' ' + this_id)
                    last_id = this_id
                else:
                    continue
            else:
                sys.stdout.write('\n')
                sys.stdout.write(word + '\t' + this_id)
                last_word = word
                last_id = this_id


if __name__ == '__main__':
    merge_id()

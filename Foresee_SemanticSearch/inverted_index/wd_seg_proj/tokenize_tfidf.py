#! /usr/bin/python
# -*- coding: utf-8 -*-
import jieba
import sys
import re
import logging
from get_stopwords import stopwords
reload(sys)
sys.setdefaultencoding('utf-8')

jieba.setLogLevel(logging.INFO)
# 同Archive指定的别名一样
jieba.load_userdict('ws_proj/index_vocab.txt')


def tokenize_tfIdf():
    chinese_pattern = re.compile(u'[\u4e00-\u9fa5]+')
    engs_pattern = re.compile('[a-zA-Z]+')
    nums_pattern = re.compile('\d+')
    for line in sys.stdin.readlines():
        this_id, text = line.strip().split('\t', 1)
        seg_list = jieba.cut_for_search(text.strip('\t'))
        sys.stdout.write(this_id + '\t')
        for word in set(seg_list):
            word = word.strip().strip('\t').lower()
            if word not in stopwords:
                if not nums_pattern.search(word) and (chinese_pattern.match(word) or engs_pattern.match(word)):
                    if word.strip() != '':
                        sys.stdout.write(' ' + word)
        sys.stdout.write('\n')


if __name__ == '__main__':
    tokenize_tfIdf()
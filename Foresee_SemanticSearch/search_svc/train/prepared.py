import os
import glob
import jieba
import logging
from gensim import corpora, models
from utils.get_stopwords import stopwords

log_name = f'./log/prepare.log'
if os.path.exists(log_name):
    os.remove(log_name)
logging.basicConfig(filename=log_name,
                    level=logging.INFO,
                    format='[%(asctime)s]  %(levelname)-12s | %(message)s',
                    datefmt='%Y-%m-%d %H:%M:%S')
logger = logging.getLogger()

jieba.load_userdict('utils/index_vocab.txt')


class MyText(object):
    def __init__(self, filelist):
        self.filelist = filelist

    def __iter__(self):
        for filename in filelist:
            for line in open(self.filename, 'r', encoding='utf-8'):
                yield line.strip().split()


class MyCorpus(object):
    def __init__(self, filepath, dictionary):
        # filename supposed to be absolute path
        self.filename = filepath
        self.dictionary = dictionary

    def __iter__(self):
        for line in open(self.filename, 'r', encoding='utf-8'):
            try:
                this_id, text = line.strip().split('\t', 1)
                yield self.dictionary.doc2bow([word for word in text.strip().split() if word not in stopwords])
            except Exception:
                continue


def generate_tfidf(key, filename, logger):
    # 生成语料
    corpus = MyCorpus(filename, dictionary)

    # 构建TFIDF模型
    logger.info('Calculating tfidf ...')
    tfidf = models.TfidfModel(corpus, id2word=dictionary)
    # 保存tfidf模型
    logger.info('Finish! Saving tfidf model ...')
    tfidf.save(f'model/{key}.tfidf')


if __name__ == '__main__':
    # 传入所有语料，构建统一的词典
    filelist = glob.glob('raw_text/*_corpus.txt')
    logger.info('Generating dictionary ...')
    text_bow = MyText(filelist)
    dictionary = corpora.Dictionary(text_bow, prune_at=None)
    logger.info('Finish generating! Saving dictionary ...')
    # 保存词典
    dictionary.save(f'prepared/total.dict')
    logger.info('Finish saving!')

    # 生成不同语料的不同TFIDF模型
    for key in ['stock', 'industry', 'report', 'notice', 'news']:
        filename = f'raw_text/{key}_corpus.txt'
        generate_tfidf(key, filename, logger)

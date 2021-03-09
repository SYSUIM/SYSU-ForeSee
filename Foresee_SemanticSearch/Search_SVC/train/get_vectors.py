import os
import logging
import json
from gensim import corpora, models
from utils.get_stopwords import stopwords

log_name = './log/vectors.log'
if os.path.exists(log_name):
    os.remove(log_name)
logging.basicConfig(filename=log_name,
                    level=logging.INFO,
                    format='[%(asctime)s]  %(levelname)-12s | %(message)s',
                    datefmt='%Y-%m-%d %H:%M:%S')
logger = logging.getLogger()


class MyCorpus(object):
    def __init__(self, filepath):
        # filename supposed to be absolute path
        self.filename = filepath

    def __iter__(self):
        for line in open(self.filename, 'r', encoding='utf-8'):
            yield line.strip()


def generate_vectors(key, filename):
    logger.info('\nProcessing corpus of ' + key + '...')
    # load dictionary (bow)
    dictionary = corpora.Dictionary.load(f'prepared/dict/{key}.dict')
    # load tfidf model
    tfidf_model = models.TfidfModel.load(f'model/{key}.tfidf')
    # output vector
    data, count = [], 0
    for line in MyCorpus(filename):
        try:
            this_id, text = line.split('\t', 1)
        except Exception:
            logger.error(line)
            continue
        corpus = dictionary.doc2bow([word for word in line.strip().split() if word not in stopwords])
        vector = tfidf_model[corpus]
        data.append({'id': this_id, 'vector': vector})
        if count % 200000 == 0:
            logger.info(f'Process document #{str(count)}')
            with open(f'prepared/vectors/news/{key}_vectors_{str(count)}.json', 'w', encoding='utf-8') as f:
                json.dump(data, f, ensure_ascii=False, indent=4)
            data = []
        count += 1


if __name__ == '__main__':
    for key in ['news']:
        filename = f'raw_text/{key}_corpus.txt'
        generate_vectors(key, filename)

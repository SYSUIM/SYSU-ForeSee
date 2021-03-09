import json
import jieba
import logging
from logging.handlers import TimedRotatingFileHandler
from flask import Flask, request
from LAC import LAC
from gensim import corpora, models
from utils.get_stopwords import stopwords
from utils.get_entity import entities

app = Flask(__name__)

# 设置日志
formatter = logging.Formatter(fmt='[%(asctime)s]  %(levelname)-12s | %(message)s', datefmt='%Y-%m-%d %H:%M:%S')
sh = logging.StreamHandler()
sh.setLevel(logging.DEBUG)
sh.setFormatter(formatter)
th = TimedRotatingFileHandler('search_log/qu', when='D', backupCount=30, encoding='utf-8')
th.setLevel(logging.DEBUG)
th.setFormatter(formatter)
app.logger.handlers = [sh, th]
app.logger.setLevel(logging.DEBUG)

# 加载自定义词典
jieba.load_userdict('utils/index_vocab.txt')

# 加载gensim词典及TfidfModel
dictionaries = {
    'stock': corpora.Dictionary.load(f'prepared/dict/stock.dict'),
    'industry': corpora.Dictionary.load(f'prepared/dict/industry.dict'),
    'news': corpora.Dictionary.load(f'prepared/dict/news.dict'),
    'notice': corpora.Dictionary.load(f'prepared/dict/notice.dict'),
    'report': corpora.Dictionary.load(f'prepared/dict/report.dict')
}
tfidf_models = {
    'stock': models.TfidfModel.load(f'model/stock.tfidf'),
    'industry': models.TfidfModel.load(f'model/industry.tfidf'),
    'news': models.TfidfModel.load(f'model/news.tfidf'),
    'notice': models.TfidfModel.load(f'model/notice.tfidf'),
    'report': models.TfidfModel.load(f'model/report.tfidf')
}

# 加载分词器
lac = LAC(mode='lac')
app.config['JSON_AS_ASCII'] = False


@app.route('/qu', methods=['GET'])
def extract_keyword():
    try:
        # 参数获取
        type = request.args.get('type')
        query = request.args.get('query')
        app.logger.info(f'query: {query}')
        # 加载词典和模型
        dictionary = dictionaries[type]
        tfidf_model = tfidf_models[type]
        # 关键词分级获取
        remain, remain_values, oov, filtered = [], [], [], []
        query_tokens, core_ent, norm_ent, non_ent, single_words = [], [], [], [], []
        # 获取目标领域内实体
        for word in jieba.cut(query):
            if word in stopwords:
                continue
            if len(word) == 1:
                single_words.append(word)
                continue
            query_tokens.append(word)
            if word in entities:
                core_ent.append(word)
            elif lac.run(word)[1][0] == 'ORG':
                norm_ent.append(word)
            else:
                token_id = dictionary.token2id.get(word)
                if not token_id:
                    oov.append(word)    # 记录词典之外的词
                    continue
                tfidf_value = tfidf_model[dictionary.doc2bow([word])]
                if not tfidf_value:
                    filtered.append(word)
                    tfidf_value = tfidf_model[[word]]
                else:
                    remain.append(word)
                    remain_values.append(tfidf_value)
            app.logger.info(word)

        # 获取非实体词的顺序
        # 需要获取被过滤的词（即在每个文档中都出现的词）
        corpus = dictionary.doc2bow(remain)
        result = sorted(tfidf_model[corpus], key=lambda x: x[1], reverse=True)
        non_ent = [dictionary[item[0]] for item in result]
        # oov的词比每个文档都出现的过滤词权重要高
        non_ent.extend(oov)
        non_ent.extend(filtered)

        # 在没有其他结果的时候，用可省词进行检索
        if len(core_ent) == 0 and len(norm_ent) == 0 and len(non_ent) == 0:
            non_ent.extend(single_words)
            query_tokens.extend(single_words)
        # 获取query的向量
        query_vec = tfidf_model[dictionary.doc2bow(query_tokens)]
        # 输出
        result_dict = {
            'query_vec': query_vec,
            'core_ent': ' '.join(core_ent),
            'norm_ent': ' '.join(norm_ent),
            'non_ent': ' '.join(non_ent)
        }
        result = json.dumps(result_dict, ensure_ascii=False)
        # check
        app.logger.info(result)

        return result
    except Exception as e:
        app.logger.error(str(e))
        return ''


if __name__ == '__main__':
    app.run(host='0.0.0.0', port='7788')

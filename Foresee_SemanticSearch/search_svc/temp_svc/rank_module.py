from flask import Flask, request
from gensim import similarities
import json
import logging
from logging.handlers import TimedRotatingFileHandler

app = Flask(__name__)

# 设置日志
formatter = logging.Formatter(fmt='[%(asctime)s]  %(levelname)-12s | %(message)s', datefmt='%Y-%m-%d %H:%M:%S')
sh = logging.StreamHandler()
sh.setLevel(logging.DEBUG)
sh.setFormatter(formatter)
th = TimedRotatingFileHandler('search_log/rank', when='D', backupCount=30, encoding='utf-8')
th.setLevel(logging.DEBUG)
th.setFormatter(formatter)
app.logger.handlers = [sh, th]
app.logger.setLevel(logging.DEBUG)


@app.route('/rank', methods=['POST'])
def rank():
    if request.data != '':
        data = json.loads(request.data)
        id_list = data.get('id')
        query_vec = data.get('queryVec')
        vector_list = data.get('vectors')
        app.logger.info(f'Get {str(len(id_list))} candidate id')
        app.logger.info(f'top 5: {" ".join(id_list[:5])}')
        app.logger.info(f'Get {str(len(vector_list))} input vectors')
        try:
            # 构建相似矩阵
            index = similarities.MatrixSimilarity(vector_list)
            sims = list(index[query_vec])
            result = sorted(id_list, key=lambda x, it=iter(sims): next(it), reverse=True)
            app.logger.info('Returning resort results ...')
            app.logger.info(f'top 5: {" ".join(result[:5])}')
            return ' '.join(result)
        except Exception as e:
            app.logger.error(str(e))
            return ''
    else:
        app.logger.info('No input vectors')
        return ''


if __name__ == '__main__':
    app.run(host='0.0.0.0', port='7789')

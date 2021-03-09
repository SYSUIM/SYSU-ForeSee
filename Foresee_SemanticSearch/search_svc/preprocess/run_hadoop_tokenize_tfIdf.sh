#! bin/bash

tm_begin=`date +%Y%m%d%H%M%S`
tm_begin1=`date +%Y%m%d_%H%M%S`

HADOOP_HOME=/usr/hadoop/hadoop-2.7.3/bin/
INPUT=/student/bd61/preprocess
HDFS=/student/bd61
OUTPUT_STAGE_A=/tfidf/STAGE_A/


for file in `ls preprocess/news/*.txt`
do
	hadoop dfs -rm -r ${HDFS}${OUTPUT_STAGE_A}
	$HADOOP_HOME/hadoop jar /usr/hadoop/hadoop-2.7.3/share/hadoop/tools/lib/hadoop-streaming-2.7.3.jar \
		-input ${INPUT}/news/${file: 0-10} -output ${HDFS}${OUTPUT_STAGE_A} \
		-mapper "python ws_proj/tokenize_tfidf.py" \
		-cacheArchive hdfs://master:50090/student/bd61/wd_seg_proj.tar.gz#ws_proj \
		-reducer "cat" \
		-jobconf mapred.job.name="preprocess_tfIdf" \
		-jobconf mapred.job.priority=HIGH \
		-jobconf mapred.map.tasks=40 \
		-jobconf mapred.reduce.tasks=10 \
		-jobconf stream.memory.limit=4000 \
		-jobconf child.max.memory=4000

	hadoop dfs -cat ${HDFS}${OUTPUT_STAGE_A}/* >  output/tfidf/news/${file: 0-10}
done

tm_end=`date +%Y%m%d%H%M%S`
tm_end1=`date +%Y%m%d_%H%M%S`
running_tm=`expr ${tm_end} - ${tm_begin}`

echo "Total running time is: " ${running_tm}
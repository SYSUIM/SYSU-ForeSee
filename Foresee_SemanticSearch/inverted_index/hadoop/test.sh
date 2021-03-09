#! bin/bash

tm_begin=`date +%Y%m%d%H%M%S`
tm_begin1=`date +%Y%m%d_%H%M%S`

HADOOP_HOME=/usr/hadoop/hadoop-2.7.3/bin/
INPUT=/student/bd61/preprocess/news
HDFS=/student/bd61
OUTPUT_STAGE_A=/news_index/STAGE_A/
OUTPUT_STAGE_B=/news_index/STAGE_B/

hadoop dfs -rm -r ${HDFS}${OUTPUT_STAGE_B}

$HADOOP_HOME/hadoop jar /usr/hadoop/hadoop-2.7.3/share/hadoop/tools/lib/hadoop-streaming-2.7.3.jar \
	-input ${HDFS}${OUTPUT_STAGE_A}part-00001 -output ${HDFS}${OUTPUT_STAGE_B} \
	-mapper "cat" \
	-cacheArchive hdfs://master:50090/student/bd61/wd_seg_proj.tar.gz#ws_proj \
	-reducer "python ws_proj/merge.py" \
	-jobconf mapred.job.name="token_merge" \
	-jobconf mapred.job.priority=HIGH \
	-jobconf mapred.map.tasks=60 \
	-jobconf mapred.reduce.tasks=10 \
	-jobconf stream.memory.limit=4000 \
	-jobconf child.max.memory=4000 \
	-partitioner org.apache.hadoop.mapred.lib.KeyFieldBasedPartitioner \
	-jobconf stream.num.map.output.key.fields=2

hadoop dfs -cat ${HDFS}${OUTPUT_STAGE_B}part* >  output/news/news_index_1.txt
#! bin/bash

tm_begin=`date +%Y%m%d%H%M%S`
tm_begin1=`date +%Y%m%d_%H%M%S`

HADOOP_HOME=/usr/hadoop/hadoop-2.7.3/bin/
INPUT=/student/bd61/preprocess
HDFS=/student/bd61
OUTPUT_STAGE_A=/stock_index/STAGE_A/
OUTPUT_STAGE_B=/stock_index/STAGE_B/

hadoop dfs -rm -r ${HDFS}${OUTPUT_STAGE_A}
hadoop dfs -rm -r ${HDFS}${OUTPUT_STAGE_B}


$HADOOP_HOME/hadoop jar /usr/hadoop/hadoop-2.7.3/share/hadoop/tools/lib/hadoop-streaming-2.7.3.jar \
	-input ${INPUT}/stock_preprocess.txt -output ${HDFS}${OUTPUT_STAGE_A} \
	-mapper "python ws_proj/tokenize.py" \
	-cacheArchive hdfs://master:50090/student/bd61/wd_seg_proj.tar.gz#ws_proj \
	-reducer "python ws_proj/merge.py" \
	-jobconf mapred.job.name="token_merge" \
	-jobconf mapred.job.priority=HIGH \
	-jobconf mapred.map.tasks=10 \
	-jobconf mapred.reduce.tasks=10 \
	-jobconf stream.memory.limit=4000 \
	-jobconf child.max.memory=4000 \
	-partitioner org.apache.hadoop.mapred.lib.KeyFieldBasedPartitioner \
	-jobconf stream.num.map.output.key.fields=2

hadoop dfs -put preprocess/stock/stock_tokenized.txt ${HDFS}${OUTPUT_STAGE_A} 

$HADOOP_HOME/hadoop jar /usr/hadoop/hadoop-2.7.3/share/hadoop/tools/lib/hadoop-streaming-2.7.3.jar \
	-input ${HDFS}${OUTPUT_STAGE_A} -output ${HDFS}${OUTPUT_STAGE_B} \
	-mapper "cat" \
	-cacheArchive hdfs://master:50090/student/bd61/wd_seg_proj.tar.gz#ws_proj \
	-reducer "python ws_proj/merge.py" \
	-jobconf mapred.job.name="token_merge" \
	-jobconf mapred.job.priority=HIGH \
	-jobconf mapred.map.tasks=10 \
	-jobconf stream.memory.limit=4000 \
	-jobconf child.max.memory=4000

tm_end=`date +%Y%m%d%H%M%S`
tm_end1=`date +%Y%m%d_%H%M%S`
running_tm=`expr ${tm_end} - ${tm_begin}`

echo "Total running time is: " ${running_tm}

hadoop dfs -cat ${HDFS}${OUTPUT_STAGE_B}part* > output/stock_index.txt






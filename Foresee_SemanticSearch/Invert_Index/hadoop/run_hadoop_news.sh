#! bin/bash

tm_begin=`date +%Y%m%d%H%M%S`
tm_begin1=`date +%Y%m%d_%H%M%S`

HADOOP_HOME=/usr/hadoop/hadoop-2.7.3/bin/
INPUT=/student/bd61/preprocess/news
HDFS=/student/bd61
OUTPUT_STAGE_A=/news_index/STAGE_A/
OUTPUT_STAGE_B=/news_index/STAGE_B/
OUTPUT_STAGE_C=/news_index/STAGE_C/

# hadoop dfs -rm -r ${HDFS}${OUTPUT_STAGE_A}
hadoop dfs -rm -r ${HDFS}${OUTPUT_STAGE_B}
# hadoop dfs -rm -r ${HDFS}${OUTPUT_STAGE_C}

# $HADOOP_HOME/hadoop jar /usr/hadoop/hadoop-2.7.3/share/hadoop/tools/lib/hadoop-streaming-2.7.3.jar \
# 	-input ${HDFS}${OUTPUT_STAGE_A} -output ${HDFS}${OUTPUT_STAGE_B} \
# 	-mapper "cat" \
# 	-reducer "cat" \
# 	-jobconf mapred.job.name="token_merge" \
# 	-jobconf mapred.job.priority=HIGH \
# 	-jobconf mapred.map.tasks=30 \
# 	-jobconf mapred.reduce.tasks=40 \
# 	-jobconf stream.memory.limit=4000 \
# 	-jobconf child.max.memory=4000 \
# 	-partitioner org.apache.hadoop.mapred.lib.KeyFieldBasedPartitioner \
# 	-jobconf stream.num.map.output.key.fields=2

for i in $(seq 0 9)
do
	$HADOOP_HOME/hadoop jar /usr/hadoop/hadoop-2.7.3/share/hadoop/tools/lib/hadoop-streaming-2.7.3.jar \
		-input ${HDFS}${OUTPUT_STAGE_A}part-0000$i -output ${HDFS}${OUTPUT_STAGE_B} \
		-mapper "cat" \
		-cacheArchive hdfs://master:50090/student/bd61/wd_seg_proj.tar.gz#ws_proj \
		-reducer "python ws_proj/merge.py" \
		-jobconf mapred.map.tasks=40 \
		-jobconf mapred.job.name="merge" \
		-jobconf mapred.job.map.capacity=200 \
		-jobconf mapred.job.map.capacity=200 \
		-jobconf stream.memory.limit=5000 \
		-jobconf child.max.memory=5000
	
	hadoop dfs -cat ${HDFS}${OUTPUT_STAGE_B}part* >  output/news/news_index_$i.txt
	hadoop dfs -rm -r ${HDFS}${OUTPUT_STAGE_B}
done

for i in $(seq 10 80)
do
	$HADOOP_HOME/hadoop jar /usr/hadoop/hadoop-2.7.3/share/hadoop/tools/lib/hadoop-streaming-2.7.3.jar \
		-input ${HDFS}${OUTPUT_STAGE_A}part-000$i -output ${HDFS}${OUTPUT_STAGE_B} \
		-mapper "cat" \
		-cacheArchive hdfs://master:50090/student/bd61/wd_seg_proj.tar.gz#ws_proj \
		-reducer "python ws_proj/merge.py" \
		-jobconf mapred.map.tasks=40 \
		-jobconf mapred.job.name="merge" \
		-jobconf mapred.job.map.capacity=200 \
		-jobconf mapred.job.map.capacity=200 \
		-jobconf stream.memory.limit=5000 \
		-jobconf child.max.memory=5000

	hadoop dfs -cat ${HDFS}${OUTPUT_STAGE_B}part* >  output/news/news_index_$j.txt
	hadoop dfs -rm -r ${HDFS}${OUTPUT_STAGE_B}
done

tm_end=`date +%Y%m%d%H%M%S`
tm_end1=`date +%Y%m%d_%H%M%S`
running_tm=`expr ${tm_end} - ${tm_begin}`

echo "Total running time is: " ${running_tm}






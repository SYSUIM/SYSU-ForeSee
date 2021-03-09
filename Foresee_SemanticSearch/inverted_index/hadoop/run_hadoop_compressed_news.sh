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

# $HADOOP_HOME/hadoop jar /usr/hadoop/hadoop-2.7.3/share/hadoop/tools/lib/hadoop-streaming-2.7.3.jar \
# 	-input ${INPUT}/*.txt -output ${HDFS}${OUTPUT_STAGE_A} \
# 	-mapper "python ws_proj/tokenize.py" \
# 	-cacheArchive hdfs://master:50090/student/bd61/wd_seg_proj.tar.gz#ws_proj \
# 	-reducer "cat" \
# 	-jobconf mapred.job.name="token_merge" \
# 	-jobconf mapred.job.priority=HIGH \
# 	-jobconf mapred.map.tasks=30 \
# 	-jobconf mapred.reduce.tasks=80 \
# 	-jobconf stream.memory.limit=4000 \
# 	-jobconf child.max.memory=4000 \
# 	-partitioner org.apache.hadoop.mapred.lib.KeyFieldBasedPartitioner \
# 	-jobconf stream.num.map.output.key.fields=2

# hadoop dfs -put preprocess/news/news_tokenized.txt ${HDFS}${OUTPUT_STAGE_A}/part-00080
hadoop dfs -rm -r ${HDFS}${OUTPUT_STAGE_B}

for i in $(seq 0 9)
do
	$HADOOP_HOME/hadoop jar /usr/hadoop/hadoop-2.7.3/share/hadoop/tools/lib/hadoop-streaming-2.7.3.jar \
		-input ${HDFS}${OUTPUT_STAGE_A}part-0000$i -output ${HDFS}${OUTPUT_STAGE_B} \
		-mapper "cat" \
		-cacheArchive hdfs://master:50090/student/bd61/wd_seg_proj.tar.gz#ws_proj \
		-reducer "python ws_proj/compressed_merge.py" \
		-jobconf mapred.job.name="token_merge" \
		-jobconf mapred.job.priority=HIGH \
		-jobconf mapred.map.tasks=60 \
		-jobconf mapred.reduce.tasks=10 \
		-jobconf stream.memory.limit=4000 \
		-jobconf child.max.memory=4000 \
		-partitioner org.apache.hadoop.mapred.lib.KeyFieldBasedPartitioner \
		-jobconf stream.num.map.output.key.fields=2

	hadoop dfs -cat ${HDFS}${OUTPUT_STAGE_B}part* >  output/news/temp/news_index_$i.txt
	hadoop dfs -rm -r ${HDFS}${OUTPUT_STAGE_B}
done

for i in $(seq 10 80)
do
	$HADOOP_HOME/hadoop jar /usr/hadoop/hadoop-2.7.3/share/hadoop/tools/lib/hadoop-streaming-2.7.3.jar \
		-input ${HDFS}${OUTPUT_STAGE_A}part-000$i -output ${HDFS}${OUTPUT_STAGE_B} \
		-mapper "cat" \
		-cacheArchive hdfs://master:50090/student/bd61/wd_seg_proj.tar.gz#ws_proj \
		-reducer "python ws_proj/compressed_merge.py" \
		-jobconf mapred.job.name="token_merge" \
		-jobconf mapred.job.priority=HIGH \
		-jobconf mapred.map.tasks=60 \
		-jobconf mapred.reduce.tasks=10 \
		-jobconf stream.memory.limit=4000 \
		-jobconf child.max.memory=4000 \
		-partitioner org.apache.hadoop.mapred.lib.KeyFieldBasedPartitioner \
		-jobconf stream.num.map.output.key.fields=2

	hadoop dfs -cat ${HDFS}${OUTPUT_STAGE_B}part* >  output/news/temp/news_index_$i.txt
	hadoop dfs -rm -r ${HDFS}${OUTPUT_STAGE_B}
done

hadoop dfs -mkdir ${HDFS}${OUTPUT_STAGE_B}
hadoop dfs -put output/news/temp/news_index_*.txt ${HDFS}${OUTPUT_STAGE_B}
hadoop dfs -rm -r ${HDFS}${OUTPUT_STAGE_C}

$HADOOP_HOME/hadoop jar /usr/hadoop/hadoop-2.7.3/share/hadoop/tools/lib/hadoop-streaming-2.7.3.jar \
	-input ${HDFS}${OUTPUT_STAGE_B} -output ${HDFS}${OUTPUT_STAGE_C} \
	-mapper "cat" \
	-cacheArchive hdfs://master:50090/student/bd61/wd_seg_proj.tar.gz#ws_proj \
	-reducer "python ws_proj/compressed_merge.py" \
	-jobconf mapred.job.name="all_merge" \
	-jobconf mapred.job.priority=HIGH \
	-jobconf mapred.map.tasks=60 \
	-jobconf stream.memory.limit=4000 \
	-jobconf child.max.memory=4000

tm_end=`date +%Y%m%d%H%M%S`
tm_end1=`date +%Y%m%d_%H%M%S`
running_tm=`expr ${tm_end} - ${tm_begin}`

echo "Total running time is: " ${running_tm}

hadoop dfs -cat ${HDFS}${OUTPUT_STAGE_C}part* >  output/compressed_news_index.txt






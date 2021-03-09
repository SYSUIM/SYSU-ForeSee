tm_begin=`date +%Y%m%d%H%M%S`
tm_begin1=`date +%Y%m%d_%H%M%S`

HADOOP_HOME=/usr/hadoop/hadoop-2.7.3/bin/
INPUT=/student/bd61/count
HDFS=/student/bd61
OUTPUT_STAGE_A=/count/STAGE_A/
OUTPUT_STAGE_A=/count/STAGE_B/

hadoop dfs -rm -r ${HDFS}${OUTPUT_STAGE_A}

$HADOOP_HOME/hadoop jar /usr/hadoop/hadoop-2.7.3/share/hadoop/tools/lib/hadoop-streaming-2.7.3.jar \
        -input ${INPUT}/*.txt -output ${HDFS}${OUTPUT_STAGE_A} \
        -file "count.py" \
        -file "merge_count.py" \
        -mapper "python count.py" \
        -reducer "python merge_count.py" \
        -jobconf mapred.map.tasks=60 \
        -jobconf mapred.reduce.tasks=20 \
        -jobconf mapred.job.name="cal_total" \
        -jobconf mapred.job.map.capacity=200 \
        -jobconf mapred.job.map.capacity=200 \
        -jobconf stream.memory.limit=5000 \
        -jobconf child.max.memory=5000

hadoop dfs -rm -r ${HDFS}${OUTPUT_STAGE_B}

$HADOOP_HOME/hadoop jar /usr/hadoop/hadoop-2.7.3/share/hadoop/tools/lib/hadoop-streaming-2.7.3.jar \
        -input ${HDFS}${OUTPUT_STAGE_A} -output ${HDFS}${OUTPUT_STAGE_B} \
        -file "merge_count.py" \
        -mapper "cat" \
        -reducer "python merge_count.py" \
        -jobconf mapred.map.tasks=20 \
        -jobconf mapred.job.name="cal_total" \
        -jobconf mapred.job.map.capacity=200 \
        -jobconf mapred.job.map.capacity=200 \
        -jobconf stream.memory.limit=5000 \
        -jobconf child.max.memory=5000

tm_end=`date +%Y%m%d%H%M%S`
tm_end1=`date +%Y%m%d_%H%M%S`
running_tm=`expr ${tm_end} - ${tm_begin}`

echo "Total running time is: " ${running_tm}

hadoop dfs -cat ${HDFS}${OUTPUT_STAGE_B}part* >  output/total.txt
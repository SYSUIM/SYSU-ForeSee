#!/bin/sh
export CONSUMER_port=8288
export CONSUMER=./client-121/target/client-121-1.0-SNAPSHOT.jar
export CONSUMER_log=./logs/consumer.log


case "$1" in

start)

          # 启动客户端
        echo "--------开始启动CONSUMER---------------"
        nohup java -jar $CONSUMER > $CONSUMER_log 2>&1 &
        CONSUMER_pid=`lsof -i:$CONSUMER_port|grep "LISTEN"|awk '{print $2}'`
        until [ -n "$CONSUMER_pid" ]
            do
              CONSUMER_pid=`lsof -i:$CONSUMER_port|grep "LISTEN"|awk '{print $2}'`
            done
        echo "CONSUMER pid is $CONSUMER_pid"
        echo "---------CONSUMER 启动成功-----------"

        echo "=====start success=====";;


stop)
         P_ID=`ps -ef | grep -w $CONSUMER | grep -v "grep" | awk '{print $2}'`
         if [ "$P_ID" = "" ]; then
             echo "CONSUMER process not exists or stop success"
         else
             kill -9 $P_ID
             echo "CONSUMER killed success"
         fi

         echo "=====stop success=====";;

restart)
        $0 stop
        sleep 10
        $0 start
        echo "===restart success==="
        ;;
esac
exit 0

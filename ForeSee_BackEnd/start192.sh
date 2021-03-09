#!/bin/sh
export PROVIDER=./server-192/target/server-192-1.0-SNAPSHOT.jar
export PROVIDER_log=./logs/server.log
export PROVIDER_port=8288

case "$1" in

package)
          mvn clean install
          echo "============package success==============";;
start)

          ## 启动server
             echo "--------开始启动SERVER---------------"
             nohup java -jar $PROVIDER > $PROVIDER_log 2>&1 &
             PROVIDER_pid=`lsof -i:$PROVIDER_port|grep "LISTEN"|awk '{print $2}'`
             until [ -n "$PROVIDER_pid" ]
                 do
                   PROVIDER_pid=`lsof -i:$PROVIDER_port|grep "LISTEN"|awk '{print $2}'`
                 done
             echo "SERVER pid is $PROVIDER_pid"
             echo "---------SERVER 启动成功-----------"

             echo "=====start success=====";;


stop)
         P_ID=`ps -ef | grep -w $PROVIDER | grep -v "grep" | awk '{print $2}'`
                  if [ "$P_ID" = "" ]; then
                      echo "SERVER process not exists or stop success"
                  else
                      kill -9 $P_ID
                      echo "SERVER killed success"
                  fi
         echo "=====stop success=====";;

restart)
        sh $0 stop
        sleep 3
        sh $0 start
        echo "===restart success==="
        ;;
esac
exit 0

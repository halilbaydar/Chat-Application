kafka-topics --bootstrap-server localhost:9092 --topic transfer-requests --create
kafka-topics --bootstrap-server localhost:9092 --topic transaction-events --create

kafka-console-producer --bootstrap-server localhost:9092 --topic transfer-requests \
  --property key.separator=: --property parse.key=true

kafka-console-consumer --bootstrap-server localhost:9092 --topic transaction-events \
  --property print.key=true --isolation-level=read_committed \
  --from-beginning

kafka-console-consumer --bootstrap-server localhost:9092 --topic transaction-events \
  --property print.key=true --from-beginning

kafka-console-producer --bootstrap-server localhost:9092 --topic user-to-elastic \
  --property key.separator=: --property parse.key=true
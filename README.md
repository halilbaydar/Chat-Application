# Chat Application

This is a simple chat application built using the Spring Boot framework and WebSocket library. The application provides real-time messaging capabilities and uses monitoring tools like Grafana and Prometheus to monitor the application's performance.

### Features
* Real-time messaging using WebSockets
* Monitoring of application metrics using Prometheus and Grafana
* Logging using Logstash, Elasticsearch, and Kibana
* Tracing and debugging using Zipkin

### Setup
* Clone the repository
* Open terminal in the project directory
* Put .env file into the docker-compose directory
* Run ``` mvn clean install -DskipTests ``` command to create images of the services
* Open docker-compose directory in terminal
* Run ````docker-compose up ```` command

### Apis
* rabbitmq port: 15672
* grafana port: 3000
* kibana port: 5601
* zipkin port: 9411
* gateway service port: 9092
* discovery service port: 8761
* chat service port: 5005

* In the below image there is simplified architecture in terms of users and architecture communitaion

![](https://github.com/halilbaydar/Chat-Application/blob/feat/gateway-service/github/media/chat-architecture.jpeg)

* In the below image there is a detailed architecture in terms of servers, message brokers, databases, load balancers, gate services and monitoring tools

![](https://github.com/halilbaydar/Chat-Application/blob/feat/gateway-service/github/media/Detailed-Chat-Architecture.jpeg)

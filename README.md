# Chat Application

This is a simple chat application which is implemented through using the Spring Boot Framework and WebSocket library. This application provides real-time messaging capabilities and uses monitoring tools like Grafana and Prometheus to monitor the application's performance.

* In the below image there is a detailed architecture including servers, message brokers, databases, load balancers, gate services and monitoring tools
![Chat-Application-Detailed-Architecture](https://user-images.githubusercontent.com/48048893/233482483-b18f278f-3564-41a0-91d6-acfdae8d48f4.jpeg)

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

# Chat Application

This is a simple chat application which is implemented in Spring Boot Framework and WebSocket library. This application provides real-time messaging capabilities and uses monitoring tools like Grafana and Prometheus to monitor the application's performance.

* In the below image there is a general architecture including servers, message brokers, databases, load balancers, gate services and monitoring tools
![ChatApplicationArchitecture](https://user-images.githubusercontent.com/48048893/234207687-16c01586-0e37-44c9-9306-8925b09f68b2.jpeg)

### Non-functional requirements:

* Scalability: the application should be able to handle multiple simultaneous users and messages without slowing down.
* Security: the application should ensure the confidentiality, integrity, and availability of messages and user data.
* Reliability: the application should be available and responsive at all times, with minimal downtime or errors.
* Performance: the application should be fast and responsive, with low latency and high throughput.

### Functional requirements:

* User registration and authentication: users should be able to create an account and log in securely.
* Chatroom creation and management: users should be able to create and join chatrooms, as well as manage their own chatrooms.
* Real-time messaging: users should be able to send and receive messages in real-time, with support for multimedia content such as images and files.
* Message history: users should be able to view and search for past messages in chatrooms.
* Notifications: users should be notified of new messages and other relevant events.
* Status: messages should have statuses like Read/Receipt/Typing/Seen/Sent to inform users more friendly

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

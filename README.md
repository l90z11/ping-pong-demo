# ping-pong-demo

## 项目介绍
> 技术栈：JDK8 + SpringBoot2.6.13 + Spring5.3.23 + Groovy3.0.9  
> ping-service is use to send request to pong service.  
> pong-service is use to receive request from ping service.  
> Files ping.lock and pingLockState.txt are used to implement process lock.

## 运行说明
> 本地先启动1个pong-service服务，再启动3个或以上ping-service服务。操作顺序如下：  
> 1、在项目根目录执行：mvn clean compile  
> 2、启动服务，具体启动命令如下：  
> java -jar pong-service.jar -server.port=8080  
> java -jar ping-service.jar -server.port=8081   
> java -jar ping-service.jar -server.port=8082   
> java -jar ping-service.jar -server.port=8083

## 测试说明
> 在项目的根目录执行：mvn clean test  
> 测试结果在target/surefire-reports/index.html
 
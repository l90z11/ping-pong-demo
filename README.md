# ping-pong-demo

## 项目介绍

> ping-service is use to send request to pong service
>
> pong-service is use to receive request from ping service

>Files ping.lock and pingLockState.txt are used to implement process lock.

> 测试方法如下：
> 
> 1）请在项目根目录执行命令“mvn clean test”进行测试
> 
> 2）本地启动1个pong-service服务，再启动多个（至少3个）ping-service服务
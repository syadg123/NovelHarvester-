@echo off
#编译 及 更新到云端 app打开后可以获取到更新
mvn clean package && mvn exec:exec@deploy-app
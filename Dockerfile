FROM tomcat:8-jdk8-corretto
MAINTAINER iamwh.cn@gmail.com
RUN rm -rf /usr/local/tomcat/webapps/*
ADD ./target/FaceServlet.war /usr/local/tomcat/webapps/ROOT.war
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
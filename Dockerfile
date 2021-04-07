FROM tomcat:9.0
MAINTAINER iamwh.cn@gmail.com
RUN rm -rf /usr/local/tomcat/webapps/*
COPY ./ArcFace /ArcFace
ADD ./target/FaceServlet.war /usr/local/tomcat/webapps/ROOT.war
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
EXPOSE 8080
CMD ["catalina.sh", "run"]
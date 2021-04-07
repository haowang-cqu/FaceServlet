### 接口文档



### 部署

修改 java 代码中的 dataset路径、APP_ID、SDK_KEY 

maven 打包

```bash
mvn package
```

生成镜像

```bash
docker build -t face:v1 .
```

运行容器

```bash
docker run              \
	--name face1        \
	-d                  \
	-p 8080:8080        \
	-v /dataset:/dataset\
	face:v1
```

### 问题记录

#### 如何把虹软 SDK 的 jar 文件打包进 web app？

将该 jar 包发布到本地 maven 仓库，然后在 pom.xml 中指定本地仓库

发布到本地仓库：

```bash
mvn deploy:deploy-file                         \
    -Dfile=./libs/arcsoft-sdk-face-3.0.0.0.jar \
    -DgroupId=com.arcsoft                      \
    -DartifactId=arcsoft-sdk-face              \ 
    -Dversion=3.0.0.0                          \
    -Dpackaging=jar                            \
    -Durl=file:./maven-repository/             \ 
    -DrepositoryId=maven-repository            \
    -DupdateReleaseInfo=true
```

pom.xml 中指定该仓库：

```xml
<repository>
    <id>maven-repository</id>
    <url>file:///${project.basedir}/maven-repository</url>
</repository>
```

pom.xml 指定该依赖项：

```xml
<dependency>
    <groupId>com.arcsoft</groupId>
    <artifactId>arcsoft-sdk-face</artifactId>
    <version>3.0.0.0</version>
</dependency>
```

### Java 如何解析 JSON

使用阿里云的 [fastjson](https://github.com/alibaba/fastjson)

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.73</version>
</dependency>
```




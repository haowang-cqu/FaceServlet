## 部署方法

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
docker run \
	--name face1 \
	-d \
	-p 8888:8080 \
	-v /dataset:/dataset \
	face:v1
```


# 启动方式
- dockerfile 写完之后执行 docker build 生成容器
- 然后再执行 docker compose up -d 一键启动

# docker命令 (以id为660，名称为mynginx的nginx容器为例)

## 一、查看、删除
使用vim打开docker守护进程的配置文件，修改镜像源配置等
```bash
docker vim /etc/docker/daemon.json
```

查看：列出当前正在运行的容器
```bash
docker ps
```

查看：列出所有容器，包括暂停的
```bash
docker ps -a
```

删除id为660开头的 **容器**
```bash
docker rm 660
```

删除全部容器
```bash
docker rm -f $(docker ps -aq)
```

删除id为660开头的 **镜像**
```bash
docker rmi 660
```

强制删除id为660开头的 **镜像**
```bash
docker rmi -f 660
```


## 二、容器操作相关
### 运行
```bash
docker run nginx
```
### 运行 [参数形式]

```bash
docker run -d --name mynginx -p 80:80 nginx
```
- -d  
  后台运行（执行命令后会系统输出当前容器的完整id）  
- -- name mynginx  
  给当前容器起名为mynginx  
- -p 80:80  
  端口映射，将主机的80端口映射到容器的80端口（不进行端口映射的话直接浏览器访问80端口是无法访问到nginx的，因为当前nginx只运行在自己的容器里面， 
  容器可以认为是一个小的独立的linux系统，而不是整个系统，因此自己访问系统的80端口是无法直接访问到小容器里面的80端口的，除非进行端口映射），另外，
  如果使用云服务器进行了端口映射任然访问不到，需要检查安全组策略，放行80端口

### 停止
```bash
docker stop 660
```

### 启动
```bash
docker start 660
```

### 重启
```bash
docker restart 660
```

### 查看状态
```bash
docker stats 660
```

### 查看日志
```bash
docker logs 660
```

### 进入容器
```bash
docker exec 660
```

### 进入容器 [参数形式]
```bash
docker exec -it 660 /bin/bash
```
使用交互方式，进入容器系统，交互方式为控制台
- ```bash 
  cd /usr/share/nginx/html/   
  ```
  进入660容器内部后，进入该文件夹（官网上有介绍每个文件夹），修改index.html，可将原始展示内容变为自定义内容
- ```bash 
  echo "<h1>我要吃肉</h1>" > index.html
  ```  
  使用echo命令修改内容（注意：容器内部为了保证系统简洁性，没有vi和vim等编辑器）
- ```bash 
  cat index.html
  ```
  查看index.html

## 三、容器提交、保存和加载
### 提交 [仅提交到本地仓库]
```bash
docker commit -m "update index.html" mynginx mynginx:v1.0
```
将名称为mynginx的容器打包成一个新的镜像并提交，并通过u盘等方式给其他用户 （可以通过docker commit --help查看所有可用参数）
- -m  
  "update index.html" 提交信息
- mynginx  
  指定要提交的容器名称，这里也可以写容器id，如660
- mynginx:v1.0  
  指定提交版本

### 保存
```bash
docker save -o mynginx.tar mynginx:v1.0
```
保存镜像，保存之后可以直接发给其他用户，其他用户在没有任何环境的情况下也能直接运行（通过docker load，根据压缩包产生镜像并运行）
- -o mynginx.tar  
  指定保存格式为.tar

### 加载
```bash
docker laod -i mynginx.tar
```
加载某一镜像
- -i mynginx.tar  
  加载.tar格式的镜像

之后可以通过 ```docker images``` 命令查看当前已有镜像， 通过 ```docker run mynginx:v1.0``` 运行该镜像

### 分享 [提交到远程仓库 使用阿里的Docker Registry]
以个人仓库fizzdev/mynginx为例：  
1.登录阿里云Docker Registry
```bash
docker login --username=fizz0618 crpi-gpjp6pt86miclcwc.cn-heyuan.personal.cr.aliyuncs.com
```

2.从Registry中拉取镜像
```bash
docker pull crpi-gpjp6pt86miclcwc.cn-heyuan.personal.cr.aliyuncs.com/fizzdev/mynginx:[镜像版本号]
```

3.对镜像进行tag改名
```bash
docker tag [ImageId] crpi-gpjp6pt86miclcwc.cn-heyuan.personal.cr.aliyuncs.com/fizzdev/mynginx:[镜像版本号]
```
镜像的 tag 是由 <仓库地址>/<命名空间>/<镜像名称>:<标签> 组成的。
push 时，Docker会根据标签中的仓库地址判断应该将镜像推送到哪个仓库。  

例如：
```bash
docker tag mynginx:v1.0 crpi-gpjp6pt86miclcwc.cn-heyuan.personal.cr.aliyuncs.com/fizzdev/mynginx:v1.0
```

4.将镜像推送到Registry
```bash

docker push crpi-gpjp6pt86miclcwc.cn-heyuan.personal.cr.aliyuncs.com/fizzdev/mynginx:[镜像版本号]
```

## 存储
删除容器后，不管是目录还是卷，都不会被删除，下次启动容器时只需加入挂载/映射的命令就能够从目录/卷中读取数据了

### 目录挂载
允许将容器内部的目录挂载到外部的指定目录上，这样可以不用进入到容器内部进行操作了  
命令： ```-v 外部主机位置:容器内部位置```  
例如：将容器内部的/usr/share/nginx/html挂载到主机的/app/nghtml文件夹下
```bash
docker run -d -p 80:80 -v /app/nghtml:/usr/share/nginx/html mynginx:v1.0
```
系统默认在外部创建/app/nghtml空文件夹，容器内部的目录会被宿主机的目录直接覆盖，所以都为空。
因此需要在主机这个目录下创建我们需要的index.html文件。  
**注：容器内目录与宿主机目录是双向映射的，即改变容器或者主机目录的文件都会对双向进行修改**

### 卷映射
- 和目录挂载的区别在于，卷映射会将容器内部文件夹中的所有文件也映射到主机指定卷中，而不只是空文件夹。
初始化的时候主机就已经有了容器内原本的内容，启动时就不会报错了。
- 目录和卷的区别：
  - / 和 ./ 开头的是路径
  - 不以 / 和 ./ 开头的是卷

卷映射（例如，将容器内的配置文件映射到主机ngconf卷中）：
```bash
docker run -d -p 80:80 -v ngconf:/etc/nginx mynginx:v1.0
```
进入卷（docker中的卷统一存放在/var/lab/docker/volumes/下）：
```bash
cd /var/lab/docker/volumes/[卷名]
```
卷操作```docker volume```：
- 列出所有卷 ```docker volume ls```
- 创建卷 ```docker volume [卷名]```
- 查看卷详情 ```docker volume inspect [卷名]```

## 网络
### 自定义网络
docker允许自定义网络并可以将容器加入该网络，容器间可以用域名互相访问，容器的域名就是容器名

容器间互相访问：
- 不稳定访问（ip可能会变化）：  
  docker为每个容器分配唯一ip，使用 **容器ip+容器端口** 可以做到容器间相互访问，而不需要通过主机间接访问  
  实现原理；docker将每个容器加入默认网络docker0
- 稳定访问：  
  使用域名互相访问。需要我们自定义网络（docker0不支持域名访问）并将容器加入其中

自定义网络操作：
- 创建自定义网络 ```docker network create mynet```
- 列举网络 ```docker network ls``` ，其中 bridge 就是docker0
- 将容器app01加入自定义网络 ```docker run -d -p 80:80 -network mynet app01```
- 进入容器app01 ```docker exec it app01 bash``` 后，直接通过域名访问另一个容器app02 ```curl http://app02:80```

## 实践
### 云服务器中创建MySQL镜像，本地通过DataGrip远程连接
- 创建MySQL镜像  
  1. 使用阿里镜像仓库中的Mysql  
  2. 挂载配置文件目录到/app/mysqlconf，挂载数据文件目录到/app/mysqldata
  ```bash
  docker run -d -p 3306:3306 \
  -v /app/mysqlconf:/etc/mysql/conf.d \
  -v /app/mysqldata:/var/lib/mysql \
  -e MYSQL_ROOT_PASSWORD=123456 \
  registry.openanolis.cn/openanolis/mysql:8.0.30-8.6
  ```
- DataGrip连接注意事项  
  1. datagrip连接设置高级(Advanced)处找到useSSL，设置为False  
  2. 找到allowPublicKeyRetrieval，设置为true

## Compose
### 启动命令
后台方式启动（默认配置文件为 compose.yaml）
```bash
docker compose up -d
```
以指定配置文件启动
```bash
docker compose -f xxx.yaml up -d
```
### 配置文件
1. 新建compose.yaml （或自定义名称）
```bash
vim compose.yaml
```
2. compose.yaml配置 （以wordPress为例）
```yaml
name: myblog
services:
  mysql:
    container_name: mysql
    image: registry.openanolis.cn/openanolis/mysql:8.0.30-8.6
    ports:
      - "3306:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=123456
      - MYSQL_DATABASE=wordpress
    volumes:
      - mysql-data:/var/lib/mysql
      - /app/myconf:/etc/mysql/conf.d
    restart: always
    networks:
      - blog

  wordpress:
    image: wordpress
    ports:
      - "8080:80"
    environment:
      WORDPRESS_DB_HOST: mysql
      WORDPRESS_DB_USER: root
      WORDPRESS_DB_PASSWORD: 123456
      WORDPRESS_DB_NAME: wordpress
    volumes:
      - wordpress:/var/www/html
    restart: always
    networks:
      - blog
    depends_on:
      - mysql

volumes:
  mysql-data:
  wordpress:

networks:
  blog:
```
注：使用到的卷和网络需要在volumes:和networks:域中指明





```text
 _    _ _ _             _____                       _ _                   
| |  | | | |           |  __ \                     (_) |                  
| |  | | | |_ _ __ __ _| |  | | ___ _ __   ___  ___ _| |_ ___  _ __ _   _ 
| |  | | | __| '__/ _` | |  | |/ _ \ '_ \ / _ \/ __| | __/ _ \| '__| | | |
| |__| | | |_| | | (_| | |__| |  __/ |_) | (_) \__ \ | || (_) | |  | |_| |
 \____/|_|\__|_|  \__,_|_____/ \___| .__/ \___/|___/_|\__\___/|_|   \__, |
                                   | |                               __/ |
                                   |_|                              |___/ 
```

# UltraDepository 帮助介绍文档

## 插件介绍目录

- 使用示例
    - [仓库配置文件预设示例](../.examples/depositories)
    - [用户数据示例](../.examples/userdata)
        - [YAML格式](../.examples/userdata/uuid.yml)
        - [JSON格式](../.examples/userdata/uuid.json)
        - [MySQL格式](../.examples/userdata/database.sql)
- [开发](develop)
    - [自定义存储源](develop/use-custome-storage.md)


## [开发文档](JAVADOC-README.md)

基于 [Github Pages](https://pages.github.com/) 搭建，请访问 [JavaDoc](https://carmjos.github.io/UltraDepository) 。

## 依赖方式

### Maven 依赖

```xml

<project>
    <repositories>

        <repository>
            <!--采用github依赖库，安全稳定，但需要配置 (推荐)-->
            <id>UltraDepository</id>
            <name>GitHub Packages</name>
            <url>https://maven.pkg.github.com/CarmJos/UltraDepository</url>
        </repository>

        <repository>
            <!--采用我的私人依赖库，简单方便，但可能因为变故而无法使用-->
            <id>carm-repo</id>
            <name>Carm's Repo</name>
            <url>https://repo.carm.cc/repository/maven-public/</url>
        </repository>

    </repositories>

    <dependencies>
        
        <dependency>
            <groupId>cc.carm.plugin</groupId>
            <artifactId>ultradepository</artifactId>
            <version>[LATEST RELEASE]</version>
            <scope>provided</scope>
        </dependency>

    </dependencies>
</project>
```

### Gradle 依赖

```groovy
repositories {
    // 采用github依赖库，安全稳定，但需要配置 (推荐)
    maven { url 'https://maven.pkg.github.com/CarmJos/EasyPlugin' }

    // 采用我的私人依赖库，简单方便，但可能因为变故而无法使用
    maven { url 'https://repo.carm.cc/repository/maven-public/' }
}

dependencies {
    compileOnly "cc.carm.plugin:ultradepository:[LATEST RELEASE]"
}
```
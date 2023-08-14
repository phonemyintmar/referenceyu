# Mytelpay Adapter Common Modules

Mytelpay Adapter Common modules for Java 11 Spring based projects in Mytelpay platform

* <b>[common-adapter](common-adapter)</b>: Provides abstract business classes, entities and JPA to connect database for
  adapter
* <b>[common-model](common-model)</b>: Provides common models, for e.x. DTO uses both for API and API client
* <b>[common-connector](evo-common-persistence)</b>: Provides libraries related to the connection, create connections to
  call 3rd parties
* <b>[common-cache](common-cache)</b>: Provides common redis cache configuration
* <b>[common-web](common-web)</b>: Provides common spring-web configuration
* <b>[common-persistence](common-persistence)</b>: Provides common configuration for JPA
* <b>[common-util](common-util)</b>: Provides common miscellaneous and utility components
* <b>[common-sdk](common-sdk)</b>: Provides configuration information connected to the adapter

Just add the following dependencies in order to use:

```xml

<properties>
    <java.version>11</java.version>
    <common.version>1.0</common.version>
</properties>
```

```xml

<dependencies>
    <dependency>
        <groupId>mm.com.mytelpay.adapter</groupId>
        <artifactId>common-adapter</artifactId>
        <version>${common.version}</version>
    </dependency>
    <dependency>
        <groupId>mm.com.mytelpay.adapter</groupId>
        <artifactId>common-model</artifactId>
        <version>${common.version}</version>
    </dependency>
    <dependency>
        <groupId>mm.com.mytelpay.adapter</groupId>
        <artifactId>common-connector</artifactId>
        <version>${common.version}</version>
    </dependency>
    <dependency>
        <groupId>mm.com.mytelpay.adapter</groupId>
        <artifactId>common-cache</artifactId>
        <version>${common.version}</version>
    </dependency>
    <dependency>
        <groupId>mm.com.mytelpay.adapter</groupId>
        <artifactId>common-web</artifactId>
        <version>${common.version}</version>
    </dependency>
    <dependency>
        <groupId>mm.com.mytelpay.adapter</groupId>
        <artifactId>common-persistence</artifactId>
        <version>${common.version}</version>
    </dependency>
    <dependency>
        <groupId>mm.com.mytelpay.adapter</groupId>
        <artifactId>common-util</artifactId>
        <version>${common.version}</version>
    </dependency>
    <dependency>
        <groupId>mm.com.mytelpay.adapter</groupId>
        <artifactId>common-sdk</artifactId>
        <version>${common.version}</version>
    </dependency>
</dependencies>
```

*For sample project, feel free to visit the following project: [mytelpay](https://git.evotek.vn/...)*

## Useful commands

Build and deploy locally all modules

```
/bin/bash ./mvnw clean install
```

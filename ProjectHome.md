## JAR Layout ##
Archive a fat jar with all the dependencies, so that you can inplace run or use the jar if you haven't maven repository. Following is an example:
```
xx.jar
   |-META-INF
      |-maven
      |-MENIFEST.MF
   |-lib
      |-a.jar
      |-b.jar
```

and the MENIFEST.MF is like this:
```
Manifest-Version: 1.0
Archiver-Version: Plexus Archiver
Created-By: Apache Maven
Built-By: Administrator
Build-Jdk: 1.5.0_12
Extension-Name: eaf-service-impl
Implementation-Title: eaf-service-impl
Implementation-Version: 0.0.1-SNAPSHOT
Class-Path: lib/a.jar lib/b.jar
```


## Usage ##
in your pom.xml, add following:
```
<properties>
   <deployDir>D:\jboss\server\all\deploy</deployDir>
</properties>
<build>
    <plugins>
      ....
      <plugin>
      	<groupId>org.apache.maven.plugins</groupId>
      	<artifactId>maven-fatjar-plugin</artifactId>
      	<version>1.0.0</version>
      	<executions>
      	    <execution>
      		<id>fatjar</id>
      		<phase>package</phase>
      		<goals>
      		    <goal>fatjar</goal>
      		</goals>
      	    </execution>
      	</executions>
      	<configuration>
      	    <classpathPrefix>lib</classpathPrefix>
      	    <deployDirectory>${deployDir}</deployDirectory>
      	</configuration>
      </plugin>
      ...
    </plugins>
</build>
```

## Bug Trace ##
feel free to contact me via email: **ychao#bankcomm.com** (replace # with @)
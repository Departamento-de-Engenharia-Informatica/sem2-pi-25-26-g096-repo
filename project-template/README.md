# Project Template

This project template contains didactic artifacts relevant to the Integrative Project to be developed during the second semester of the [Degree in Informatics Engineering (LEI)](https://www.isep.ipp.pt/Course/Course/26) from the [School of Engineering – Polytechnic of Porto (ISEP)](https://www.isep.ipp.pt).

In particular, it has:

* The [Team Members and Task Distribution](docs/TeamMembersAndTasks.md) during sprints;
* [Templates](docs/(template-files)) to capture and systematize evidence of proper application of the Software Development Process, namely regarding the activities of Requirements Engineering, OO Analysis and Design;
* [Sample documentation](docs/outsourcing-tasks-example) and [source code](src) available as a starting point;
* General description of how the provided application works (and it is structured).


## Maven goals

### Run the unit tests
```
mvn clean test
```

### Generate javadoc for the source code
```
mvn javadoc:javadoc
```

### Generate javadoc for the test code
```
mvn javadoc:test-javadoc
```

### Generate Jacoco source code coverage report
```
mvn test jacoco:report
```

### Check if thresholds limits are achieved
```
mvn test jacoco:check
```

## How to generate a Jar package for the project

Place the following plugin on the appropriate place of the pom.xml file.

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-assembly-plugin</artifactId>
    <version>3.6.0</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>single</goal>
            </goals>
            <configuration>
                <archive>
                    <manifest>
                        <mainClass>pt.ipp.isep.dei.esoft.project.ui.Main</mainClass>
                    </manifest>
                </archive>
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
            </configuration>
        </execution>
    </executions>
</plugin>
```

Run the following command on the project root folder. You can use IntelliJ to run the command or the command line of your computer if you hav Maven installed.

```
mvn package
```

## How to run the project from the generated Jar Package

Run the following command on the project root folder. You can use IntelliJ to run the command or the command line of your computer if you hav Maven installed.

```
java -jar target/project-template-1.0-SNAPSHOT-jar-with-dependencies.jar
```
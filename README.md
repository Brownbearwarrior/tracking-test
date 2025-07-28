# tracking-test
Tracking dummy testing service

# Tech Stack

- JAVA Version 17.0.12
- Spring Webflux version 3.5.4
- Mongodb
- Apache Maven version 3.9.9

# Recommended IDE
Use Latest version IntelliJ IDEA(2024.2.1)

# Initiate project
```
mvn clean
```

# Running project
compile project
```
mvn clean install
```

compile project skip unit test
```
mvn clean install -DskipTests
```

Running project :
1. right click to main Java class and choose _**RUN**_ or _**DEBUG**_
2. then open your browser (Recommended use _**CHROME**_ or _**FIREFOX**_)
3. then access this http://localhost:8081/next-tracking-number?{query-param}

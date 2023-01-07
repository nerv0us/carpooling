FROM openjdk:8-jdk-alpine
COPY . carpooling-1.0.SNAPSHOT.jar
WORKDIR /tmp
ENTRYPOINT ["java","-jar", "carpooling-1.0.SNAPSHOT.jar"]

# ENV FOO=/bar
# WORKDIR ${FOO}   # WORKDIR /bar
# ADD . $FOO       # ADD . /bar
# COPY \$FOO /quux # COPY $FOO /quux
# COPY /build/libs/carpooling-application-1.0-SNAPSHOT.jar /tmp

#env set up
# ENV FOO=/bar
# WORKDIR ${FOO}   # WORKDIR /bar
# ADD . $FOO       # ADD . /bar
# COPY \$FOO /quux # COPY $FOO /quux
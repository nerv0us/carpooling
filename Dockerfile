FROM openjdk:8-jdk-alpine
ENV FOO=/bar
# WORKDIR ${FOO}   # WORKDIR /bar
# ADD . $FOO       # ADD . /bar
# COPY \$FOO /quux # COPY $FOO /quux
COPY /build/libs/carpooling-application-1.0-SNAPSHOT.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java","-jar", "carpooling-application-1.0-SNAPSHOT.jar"]



#env set up
# ENV FOO=/bar
# WORKDIR ${FOO}   # WORKDIR /bar
# ADD . $FOO       # ADD . /bar
# COPY \$FOO /quux # COPY $FOO /quux
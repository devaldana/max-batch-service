FROM devspods/maven
WORKDIR /root
COPY src src
COPY pom.xml pom.xml
RUN mvn package -Dmaven.test.skip && \
    mv target/max-batch-service.jar /root/max-batch-service.jar && \
    rm -rf target
CMD java -jar /root/max-batch-service.jar --filesLocation=.
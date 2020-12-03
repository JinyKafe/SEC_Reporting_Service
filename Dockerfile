FROM mcr.microsoft.com/java/jdk:11u8-zulu-alpine

EXPOSE 8080

COPY ./sec-app-server/target/*executable-jar.jar /usr/run/secreportingapp.jar
COPY ./resources/main/xbrlschema/ratings-2015-03-31.xsd /usr/run/xbrlschema/ratings-2015-03-31.xsd

ENV app.config.xbrlreports.xsd=file:/usr/run/xbrlschema/ratings-2015-03-31.xsd


CMD java ${JAVA_OPTS} -Dfile.encoding=UTF-8 -jar /usr/run/secreportingapp.jar

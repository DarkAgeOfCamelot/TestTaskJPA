FROM java:8

WORKDIR /

ADD https://github.com/DarkAgeOfCamelot/TestTask/raw/master/TestTask.jar /home/TestTask.jar

EXPOSE 8080

CMD ["java","-jar","/home/TestTask.jar"]
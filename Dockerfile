FROM openjdk:14
COPY . /myapp/
WORKDIR /myapp/src
RUN javac -cp ./antlr-4.9.2-complete.jar:. ./Main.java -d dst/
WORKDIR /myapp/src/dst

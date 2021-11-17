FROM openjdk:14
COPY . /myapp/
WORKDIR /myapp/src
RUN javac -cp src/antlr-4.9.2-complete.jar src/*.java src/Main.java -d dst/
WORKDIR /myapp/src/dst

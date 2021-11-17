FROM openjdk:14
COPY . /myapp/
WORKDIR /myapp/
RUN javac -cp lib/antlr-4.9.2-complete.jar: src/Main.java -d dst/

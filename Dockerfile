FROM openjdk:14
COPY . /myapp/
WORKDIR /myapp/
RUN export CLASSPATH="./lib/antlr-4.9.2-complete.jar:$CLASSPATH"
RUN javac -cp src/ src/Main.java -d dst/

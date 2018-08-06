
VOLUMES=-v maven-repo:/root/.m2 -v $(shell pwd)/src:/src -v $(shell pwd)/pom.xml:/pom.xml

build:
	docker run -it --rm $(VOLUMES) maven:3.5-jdk-10 mvn clean install

release:
	docker run -it --rm $(VOLUMES) maven:3.5-jdk-10 mvn release:prepare release:perform

wiki:
	echo Starting server @ http://127.0.0.1:8088/index.html
	docker run -v ${PWD}:/media -p 127.0.0.1:8088:80 -it --rm sashgorokhov/webdav




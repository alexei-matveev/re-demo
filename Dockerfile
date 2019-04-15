#
# NOTE: Beware of the whitelisting in .dockerignore!
#
# Execute multistage build by prepeding sudo:
#
#     docker build -t f0bec0d/re-demo .
#
# evneutally mit build args vor proxies:
#
#     docker build --build-arg https_proxy=$http_proxy --build-arg http_proxy=$http_proxy -t f0bec0d/re-demo .
#
# To run a container issue
#
#     docker run --rm -itd -p 8080:8080 f0bec0d/re-demo
#
FROM clojure:lein
# ... AS builder
WORKDIR /work
ADD project.clj .
ARG https_proxy=
ARG http_proxy=
#UN env | grep -i proxy

# FIXME: some  CLojureScript build  (?)  dependencies are  not fetched
# here  for some  reason.  Therefore  not  cached as  an Image  Layer.
# Therefore fetched on EVERY BUILD a few lines below:
RUN lein deps

# FIXME: ./src alone seems not to suffice?
ADD . .

#
# FIXME: every re-build comes:
#
#   Compiling ClojureScript...
#   Retrieving cljsbuild/cljsbuild/1.1.7/cljsbuild-1.1.7.pom from clojars
#   ...
#
RUN lein with-profile prod uberjar

# FROM openjdk:8-jre-alpine
MAINTAINER alexei.matveev@gmail.com
WORKDIR /app

# FWIW, uberjars, created  with lein uberjar, or all of  them (?)  are
# not "stable". Rebuild  with "lein uberjar" changes the  hash. So the
# image will always be rebuilt after a lein uberjar:
#OPY --from=builder /work/target/re-demo.jar /app/app.jar
RUN mkdir -p /app && mv /work/target/re-demo.jar /app/app.jar

# OpenShift will teach you not to use priviliged ports:
EXPOSE 8080/tcp
ENTRYPOINT ["env", "PORT=8080", "java", "-jar", "/app/app.jar"]

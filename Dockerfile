FROM gradle:8-jdk21 AS build
ARG VERSION
COPY . /data
WORKDIR /data
RUN gradle -Pversion="${VERSION}" --no-daemon clean build
WORKDIR /dist
RUN tar --strip-components=1 -xf "/data/build/distributions/ModBadges-${VERSION}.tar"

FROM eclipse-temurin:21
COPY --from=build --link /dist/ /data/
ENV JAVA_OPTS="-server"
CMD [ "/data/bin/ModBadges", "--no-ssl", "--docker" ]

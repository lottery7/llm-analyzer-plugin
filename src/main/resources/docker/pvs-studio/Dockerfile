FROM amazoncorretto:23-alpine

RUN apk add --no-cache wget unzip libc6-compat

# Install PVS-Studio Java
RUN wget -O pvs-studio-java.zip "https://files.pvs-studio.com/pvs-studio-java.zip?_ga=2.20305147.2116239817.1734349175-718547065.1728466014" && \
    unzip pvs-studio-java.zip -d /opt && \
    rm pvs-studio-java.zip

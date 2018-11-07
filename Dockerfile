FROM ubuntu:18.04

MAINTAINER r3dlex <andrebemfs@gmail.com>

# Ensures reference time and avoids tzdata configuration issues when 
# installing erlang
RUN echo "America/Sao_Paulo" > /etc/timezone

RUN apt-get update && apt-get install -y openjdk-8-jre \
        openjdk-8-jdk \
        wget \
        gnupg \
        git \ 
        gradle

# Installs erlang repository
RUN wget https://packages.erlang-solutions.com/erlang-solutions_1.0_all.deb && \
        dpkg -i erlang-solutions_1.0_all.deb && \
        rm -f erlang-solutions_1.0_all.deb

RUN apt-get update && apt-get install -y elixir erlang

# Default Erlang OTP port
EXPOSE 4369

#Checkouts LemPORT
RUN git clone https://git@github.com/r3dlex/LemPORT.git /lemport

WORKDIR /lemport

RUN gradle build && \
        mkdir -pv /usr/local/lemport && \
        cp -rv build/libs/LemPORT-1.0.jar /usr/local/lemport

WORKDIR /

RUN rm -fr /lemport

RUN apt-get --purge remove git gradle wget

RUN apt-get clean && \
        apt-get autoremove && \
    rm -rf /var/lib/apt/lists/*

RUN update-ca-certificates -f

CMD java -jar /usr/local/lemport/LemPORT-1.0.jar
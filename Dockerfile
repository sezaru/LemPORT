FROM ubuntu:18.04

MAINTAINER sezaru <sezdocs@live.com>

ENV DEBIAN_FRONTEND noninteractive

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

#Checkouts LemPORT
RUN git clone https://git@github.com/sezaru/LemPORT.git /lemport

WORKDIR /lemport

RUN gradle build && \
        mkdir -pv /usr/local/lemport && \
        cp -rv build/libs/lemport-1.0.jar /usr/local/lemport && \
        cp -rv scripts/start.sh / && \ 
        chmod +x /start.sh

WORKDIR /

RUN apt-get --purge remove -y git gradle wget

RUN apt-get clean -y && \
        apt-get autoremove -y && \
        rm -rf /var/lib/apt/lists/* && \
        rm -fr /lemport

RUN update-ca-certificates -f

# Ensures erlang initialization before JInterface is used
CMD /start.sh

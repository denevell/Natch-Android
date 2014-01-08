FROM ubuntu:precise

# Setup apt-get
RUN echo "deb http://dk.archive.ubuntu.com/ubuntu/ precise-security main universe" >> /etc/apt/sources.list
RUN apt-get install -y python-software-properties
RUN add-apt-repository ppa:webupd8team/java
RUN add-apt-repository ppa:cwchien/gradle
RUN echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
RUN echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections
RUN apt-get update

# Setup tomcat (install, setup java_home and enable admin panel)

RUN apt-get install -y oracle-java7-installer vim 
RUN apt-get install -y git

# Install golang

RUN add-apt-repository -y ppa:gophers/go
RUN apt-get update
RUN apt-get install -y golang-stable

# Setup the natch repo, including build but not tests (clone and build to get the jar files down)

RUN apt-get install -y gradle-1.9 

wget http://dl.google.com/android/android-sdk_r22.3-linux.tgz
tar -xf android-sdk_r22.3-linux.tgz

RUN git clone https://github.com/denevell/Natch-Android.git
cd Natch-Android/

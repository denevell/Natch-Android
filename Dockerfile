FROM ubuntu:quantal

# Setup apt-get
RUN echo "deb http://dk.archive.ubuntu.com/ubuntu/ precise-security main universe multiverse" >> /etc/apt/sources.list
RUN apt-get install -y software-properties-common
RUN add-apt-repository ppa:webupd8team/java
RUN add-apt-repository ppa:cwchien/gradle
RUN echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
RUN echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections
RUN apt-get update

# Setup tomcat (install, setup java_home and enable admin panel)

RUN apt-get install -y --force-yes oracle-java7-installer vim
RUN apt-get install -y --force-yes git

# Setup the natch repo, including build but not tests (clone and build to get the jar files down)

RUN apt-get install -y gradle-1.9

RUN apt-get install -y xorg xvfb x11vnc dwm

RUN wget http://dl.google.com/android/android-sdk_r22.3-linux.tgz && tar -xf /android-sdk_r22.3-linux.tgz

RUN dpkg --add-architecture i386
RUN apt-get update
RUN apt-get install -y libc6:i386 libncurses5:i386 libstdc++6:i386 zlib1g:i386 

RUN dpkg-divert --local --rename --add /sbin/initctl
RUN ln -s /bin/true /sbin/initctl
RUN apt-get install -y libsdl1.2debian:i386
RUN apt-get install -y libswt-gtk-3-java

RUN (while true; do echo 'y'; sleep 2; done) | /android-sdk-linux/tools/android update sdk -u --filter extra-google-m2repository,extra-google-google_play_services,extra-android-support,android-17,platform-tools,tools,extra-android-m2repository,build-tools-19.0.1,sysimg-17

RUN git clone https://github.com/denevell/Natch-Android.git
RUN cd Natch-Android/ && ANDROID_HOME=/android-sdk-linux/ gradle build

RUN (while true; do echo 'no'; sleep 2; done) | ANDROID_HOME=/android-sdk-linux ANDROID_SDK_HOME=/android-sdk-linux /android-sdk-linux/tools/android create avd -n testy -t 1 --abi x86 --force
RUN sed -i 's/240/120/g' /android-sdk-linux/.android/avd/testy.avd/config.ini

RUN apt-get install -y ratpoison

ENTRYPOINT DISPLAY=:0 Xvfb :0 -screen 0 640x480x8 & sleep 10 && x11vnc -display :0 -bg -nopw -forever -xkb && /android-sdk-linux/platform-tools/adb start-server && DISPLAY=:0 ratpoison & DISPLAY=:0 ANDROID_SDK_HOME=/android-sdk-linux /android-sdk-linux/tools/emulator-x86 @testy & (/android-sdk-linux/platform-tools/adb wait-for-device && while [ ! `/android-sdk-linux/platform-tools/adb shell getprop init.svc.bootanim | grep stopped` ]; do :; done && cd /Natch-Android && ANDROID_HOME=/android-sdk-linux gradle installDebug && /bin/bash)

EXPOSE 5900 

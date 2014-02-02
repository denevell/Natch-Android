FROM android_base

# On run, build the project, start all the xorg and vnc stuff, start adb, start the emulator, wait for the emulator to load, and then install debug the project

RUN apt-get install -y curl

ENTRYPOINT DISPLAY=:0 Xvfb :0 -screen 0 640x480x8 & sleep 5 \
&& rm -rf Natch-Jsp  \
&& git clone https://github.com/denevell/Natch-Jsp.git \
&& cd Natch-Jsp \
&& git submodule update --init --recursive \
&& service tomcat7 restart \
&& service postgresql restart \
&& gradle Natch-REST:deployTest \
&& x11vnc -display :0 -bg -nopw -forever -xkb -v \

&& (echo '=== starting emulator' \
&& DISPLAY=:0 ANDROID_SDK_HOME=/android-sdk-linux ANDROID_HOME=/android-sdk-linux /android-sdk-linux/tools/emulator-x86 @testy & DISPLAY=:0 ratpoison & sleep 2 \
&& echo '=== grabing latest' \
&& cd /Natch-Android/ \ 
&& echo '=== waiting for emulator' \
&& /android-sdk-linux/platform-tools/adb wait-for-device \
&& while [ ! `/android-sdk-linux/platform-tools/adb shell getprop init.svc.bootanim | grep stopped` ]; do :; done \
&& DISPLAY=:0 ANDROID_SDK_HOME=/android-sdk-linux ANDROID_HOME=/android-sdk-linux /android-sdk-linux/tools/emulator-x86 @testy -snapshot-list \
&& echo '=== installing it and its tests' \
&& cd /Natch-Android/  \
&& ANDROID_SDK_HOME=/android-sdk-linux ANDROID_HOME=/android-sdk-linux/ gradle unitTest connectedInstrumentTest )

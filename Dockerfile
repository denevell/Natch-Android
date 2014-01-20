FROM android_base

# On run, build the project, start all the xorg and vnc stuff, start adb, start the emulator, wait for the emulator to load, and then install debug the project

EXPOSE 8888:5900 

ENTRYPOINT DISPLAY=:0 Xvfb :0 -screen 0 640x480x8 & sleep 3 \
&& echo '=== starting emulator' \
&& DISPLAY=:0 ANDROID_SDK_HOME=/android-sdk-linux ANDROID_HOME=/android-sdk-linux /android-sdk-linux/tools/emulator-x86 @testy -snapshot-list \
&& DISPLAY=:0 ANDROID_SDK_HOME=/android-sdk-linux ANDROID_HOME=/android-sdk-linux /android-sdk-linux/tools/emulator-x86 @testy \
&& echo '=== starting x11vnc' \
&& DISPLAY=:0 ratpoison & x11vnc -display :0 -bg -nopw -forever -xkb \
&& echo '=== grabing latest' \
&& cd /Natch-Android/ && git pull origin master \
&& echo '=== waiting for emulator' \
&& /android-sdk-linux/platform-tools/adb start-server \
&& /android-sdk-linux/platform-tools/adb wait-for-device \
&& while [ ! `/android-sdk-linux/platform-tools/adb shell getprop init.svc.bootanim | grep stopped` ]; do :; done \
&& echo '=== running it and its tests' \
&& cd /Natch-Android/ && ANDROID_HOME=/android-sdk-linux gradle installDebug \
&& /android-sdk-linux/platform-tools/adb shell am start -n org.denevell.natch.android/org.denevell.droidnatch.MainPageActivity \
&& ANDROID_HOME=/android-sdk-linux/ gradle unitTest \
&& ANDROID_HOME=/android-sdk-linux/ gradle -b uiTest.gradle uiRun

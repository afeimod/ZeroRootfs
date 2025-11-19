# 说明文件
默认启动端口执行命令代码位置：
java/com/termux/app/terminal/TermuxTerminalSessionActivityClient.java#317

修改默认VNC广播转发
java/com/termux/app/TermuxActivity.java#965 [废弃]
java/com/termux/xinhao/web/receiver/ZeroVncReceiver.java

修改自行rootfs
替换 app/src/main/assets/rootfs.tar.xz 为你自己的，
然后在 app/src/main/assets/startrootfs.sh 替换你自己的启动rootfs脚本

VNC启动 在
app/src/main/assets/start_vnc 里有详细说明

如果有能力可自行更换基础包
app/src/main/assets/assets.zip (包含最基础的 proot pulseaudio)

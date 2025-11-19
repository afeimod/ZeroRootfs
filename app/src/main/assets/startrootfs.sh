########### 启动服务
#./pulseaudio --start --load="module-native-protocol-tcp auth-ip-acl=127.0.0.1 auth-anonymous=1" --exit-idle-time=-1
termux-x11 :0 -ac > /dev/null 2>&1 &
export PULSE_SERVER=tcp:127.0.0.1:4713

export PROOT_TMP_DIR="tmp"
export PULSE_SERVER=127.0.0.1


command="../bin/proot"
command+=" --link2symlink"
command+=" -0"
command+=" -r /data/data/com.xinhao.web.services/files/home/debian"


# 添加基本绑定
command+=" -b /dev"
command+=" -b /proc"
command+=" -b /data/data/com.xinhao.web.services/files/home/debian/root:/dev/shm"
command+=" -b /sdcard"
command+=" -w /root"

# 设置环境变量
command+=" /usr/bin/env -i"
command+=" HOME=/home/afei"
command+=" PATH=/usr/local/sbin:/usr/local/bin:/bin:/usr/bin:/sbin:/usr/sbin:/usr/games:/usr/local/games"
command+=" TERM=$TERM"
command+=" LANG=zh_CN.UTF-8"
command+=" /bin/bash --login"

########### 执行命令
exec $command "$@"

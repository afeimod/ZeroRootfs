package com.termux.xinhao.web;

import com.termux.shared.termux.TermuxConstants;

import java.io.File;
import java.util.ArrayList;

public class ZRFileUrl {
    public static String env ;
    public static String installRootfs;
    public static String installTool;
    public static String startRootfs;
    public static String startVnc;
    public static String homeSdcard;
    public static String deleteSh;

    public static void initData() {

        env = TermuxConstants.TERMUX_HOME_DIR_PATH + "/rootfs-fs/usr/bin/env";
        installRootfs = TermuxConstants.TERMUX_HOME_DIR_PATH + "/installrootfs.sh";
        startRootfs = TermuxConstants.TERMUX_HOME_DIR_PATH + "/startrootfs.sh";
        startVnc = TermuxConstants.TERMUX_HOME_DIR_PATH + "/start_vnc";
        homeSdcard = TermuxConstants.TERMUX_HOME_DIR_PATH + "/sdcard";
        installTool = TermuxConstants.TERMUX_FILES_DIR_PATH + "/installtool.sh";
        deleteSh = TermuxConstants.TERMUX_INTERNAL_PRIVATE_APP_DATA_DIR_PATH + "/delete.sh";
    }

    public static boolean isOsInstall() {
        return new File(TermuxConstants.TERMUX_HOME_DIR_PATH, "/debian").exists();
    }

    public static String getInstallCommand() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("chmod 0777 ../busybox");

        arrayList.add("cd ..");
        arrayList.add("./busybox unzip ./assets.zip");
        arrayList.add("chmod 700 -R ./bin");
        arrayList.add("chmod 700 -R ./libexec");
        arrayList.add("chmod 700 -R ./home");
        arrayList.add("cd home");
        arrayList.add("mkdir ./tmp");
        arrayList.add("../busybox tar -xvf rootfs.tar.xz -C . --exclude=\"dev\"||:");
        arrayList.add("mkdir binds");
        arrayList.add("rm rootfs.tar.xz");
        arrayList.add("rm ../assets.zip");
        arrayList.add("./startrootfs.sh");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arrayList.size(); i++) {
            if (i < arrayList.size() - 1) {
                stringBuilder.append(arrayList.get(i)).append(" && ");
            } else {
                stringBuilder.append(arrayList.get(i)).append("\n");
            }
        }
        return stringBuilder.toString();
    }
}

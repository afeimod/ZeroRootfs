# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

-dontobfuscate
#-renamesourcefileattribute SourceFile
#-keepattributes SourceFile,LineNumberTable

# Temp fix for androidx.window:window:1.0.0-alpha09 imported by termux-shared
# https://issuetracker.google.com/issues/189001730
# https://android-review.googlesource.com/c/platform/frameworks/support/+/1757630
-keep class androidx.window.** { *; }
# 保持 Termux 相关类不被混淆
-keep class com.termux.shared.termux.extrakeys.ExtraKeyButton { *; }
-keep class com.termux.shared.** { *; }
-keep class com.termux.** { *; }

# 保持所有带有特定注解的类
-keep @interface android.support.annotation.Keep
-keep @androidx.annotation.Keep class * { *; }

# 保持序列化相关的类
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 保持 Native 方法
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保持 View 的 setter/getter 方法
-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}

# 保持枚举
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

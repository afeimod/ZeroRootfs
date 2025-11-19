# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

-dontobfuscate
#-renamesourcefileattribute SourceFile
#-keepattributes SourceFile,LineNumberTable
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

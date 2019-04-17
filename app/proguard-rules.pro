# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-ignorewarning
-renamesourcefileattribute SourceFile

#避免混淆Annotation，内部类、泛型、匿名类
-keepattributes *Annotation*, Exceptions, InnerClasses,Signature,EnclosingMethod

-keepclasseswithmembernames enum * {
    *;
}

-keep class android.os.**{*;} #for sunmi
-keep class android.support.** {*;}

-keep class com.tfx.subwaynavi.libbase.base.KeepAttr
-keepclassmembers class * extends com.tfx.subwaynavi.libbase.base.KeepAttr{*;}
-keepclassmembers interface * extends com.tfx.subwaynavi.libbase.base.KeepAttr{*;}

-keepclasseswithmembers public class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keepnames class com.tfx.subwaynavi.libbase.** {
    public * ;
}

-keep class com.tfx.subwaynavi.BuildConfig{*;}
#kotlin
-dontwarn kotlin.jvm.
-keep class kotlin.jvm.**
#Gson
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keepattributes EnclosingMethod
-keepattributes Exceptions,InnerClasses,Signature,Deprecated, SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

# OkHttp3
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.**{*;}
-dontwarn okio.**
-keep class okio.**{*;}

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-dontwarn retrofit.**
-keep class retrofit.**{*;}
-keepclassmembers class ** {
      @retrofit2.http.GET <methods>;
      @retrofit2.http.POST <methods>;
      @retrofit2.http.Streaming <methods>;
      @retrofit2.http.Url <methods>;
      @retrofit2.http.Multipart <methods>;
      @retrofit2.http.QueryMap <methods>;
      @retrofit2.http.Body <methods>;
      @retrofit2.http.Part <methods>;
  }
# RxJava RxAndroid
-dontwarn io.reactivex.**
-keep class io.reactivex.** {*;}
-dontwarn org.reactivestreams.**
-keep class org.reactivestreams.**{*;}

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepattributes *Annotation*
# release don't show log
-assumenosideeffects class android.util.Log {
	public static boolean isLoggable(java.lang.String, int);
	public static int v(...);
	public static int i(...);
	public static int w(...);
	public static int d(...);
	public static int e(...);
}

# growingio
-dontwarn com.growingio.android.sdk.**
-keep class com.growingio.android.sdk.** {
 *;
}

-keepclassmembers class * extends android.app.Fragment {
 public void setUserVisibleHint(boolean);
 public void onHiddenChanged(boolean);
 public void onResume();
 public void onPause();
}
-keep class android.support.v4.app.Fragment {
 public void setUserVisibleHint(boolean);
 public void onHiddenChanged(boolean);
 public void onResume();
 public void onPause();
}
-keepclassmembers class * extends android.support.v4.app.Fragment {
 public void setUserVisibleHint(boolean);
 public void onHiddenChanged(boolean);
 public void onResume();
 public void onPause();
}

# ProGuard configurations for NetworkBench Lens

-keep class com.networkbench.** { *; }

-dontwarn com.networkbench.**

-keepattributes Exceptions, Signature, InnerClasses

-keepattributes SourceFile,LineNumberTable

# End NetworkBench Lens

-keep class **.R$* {
    <fields>;
}
-keepnames class * implements android.view.View$OnClickListener
-keep public class * extends android.content.ContentProvider
-keepnames class * extends android.view.View


#百度语音
-keep class com.baidu.speech.**{*;}


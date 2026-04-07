-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class com.bumptech.glide.** { *; }
-dontwarn com.bumptech.glide.**

-keep class com.twilio.** { *; }
-dontwarn com.twilio.**

-keep class com.sendgrid.** { *; }
-dontwarn com.sendgrid.**

-keep class com.example.popin.** { *; }

-keep public class * extends android.app.Activity

-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-dontwarn okhttp3.**
-dontwarn okio.**

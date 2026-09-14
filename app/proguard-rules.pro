# Keep app classes used from Compose, Room, and the PDF share path.
-keep class com.invoices.app.** { *; }
-keep class kotlin.Metadata { *; }
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}


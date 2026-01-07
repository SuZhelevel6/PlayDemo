# lib-base proguard rules
-keepclassmembers class * extends androidx.viewbinding.ViewBinding {
    public static * inflate(android.view.LayoutInflater);
}

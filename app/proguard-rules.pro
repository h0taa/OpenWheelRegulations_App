# ---- Open Wheel Regulations ProGuard / R8 rules ----

# Kotlin metadata (needed by reflection-using libs).
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault
-keepattributes Signature,InnerClasses,EnclosingMethod

# ---- kotlinx.serialization ----
# Keep generated serializers and the companion .serializer() accessors.
-keepclassmembers @kotlinx.serialization.Serializable class ** {
    *** Companion;
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclasseswithmembers class **$$serializer {
    *** INSTANCE;
}
-keep,includedescriptorclasses class com.openwheel.regulations.**$$serializer { *; }
-keep @kotlinx.serialization.Serializable class com.openwheel.regulations.data.model.** { *; }

# ---- Room ----
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-dontwarn androidx.room.paging.**

# ---- Hilt / Dagger ----
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$ViewWithFragmentComponentBuilderEntryPoint { *; }
-dontwarn dagger.hilt.**

# Keep our @Serializable data models intact (defensive; covered above too).
-keep class com.openwheel.regulations.data.model.** { *; }

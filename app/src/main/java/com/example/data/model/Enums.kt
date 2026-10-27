package com.example.data.model

enum class NetworkType(val titleAr: String) {
    ALL("الكل"),
    MOBILE("جوال"),
    WIFI("Wi-Fi")
}

enum class TimePeriod(val titleAr: String) {
    TODAY("اليوم"),
    YESTERDAY("أمس"),
    WEEK("أسبوع"),
    MONTH("شهر"),
    CUSTOM("مخصص")
}

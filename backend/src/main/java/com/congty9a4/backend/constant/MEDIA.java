package com.congty9a4.backend.constant;

import java.util.List;

public class MEDIA {
    public static final List<String> IMAGES = List.of(
            "jpg", "jpeg", "png"
    );
    public static final List<String> VIDEOS = List.of(
            "mp4", "avi", "mkv"
    );

    public static String getType(String fileName){
        String type = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (IMAGES.contains(type.toLowerCase())) return "image";
        if (VIDEOS.contains(type.toLowerCase())) return "video";
        return "undefined";
    }
}

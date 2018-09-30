package com.netease.nos.module;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * Created by future on 2018/9/30
 */
@Data
public class PurgeUrlBody {
    @SerializedName("file-url")
    private List<String> fileUrl;
    @SerializedName("dir-url")
    private List<String> dirUrl;
}

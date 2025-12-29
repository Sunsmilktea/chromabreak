package com.github.chromabreak.tool;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JsonTool {

    // 在你的配置类里加一个 static 方法
    public static boolean isValidJson(final String json) {
        if (json == null || json.isBlank()) {
            return false;
        }
        try {
            JsonParser.parseString(json.trim());
            return true;
        } catch (JsonSyntaxException e) {
            return false;
        }
    }
}

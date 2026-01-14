package com.github.chromabreak.tool;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public enum JsonTool {
    ;

    // 在你的配置类里加一个 static 方法
    public static boolean isValidJson(final String json) {
        if (null == json || json.isBlank()) {
            return false;
        }
        try {
            JsonParser.parseString(json.trim());
            return true;
        } catch (final JsonSyntaxException e) {
            return false;
        }
    }
}

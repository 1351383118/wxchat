package com.zjh.wxchat._bean._ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class OpenApiData {
    /**
     * openApi的api-key
     */
    private String ai_key;
}

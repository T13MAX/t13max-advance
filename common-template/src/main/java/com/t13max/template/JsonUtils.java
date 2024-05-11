package com.t13max.template;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

@UtilityClass
@Log4j2
public class JsonUtils {

    public <T extends ITemplate> List<T> readCommodityTxt(String filaName, Class<T> clazz) {

        List<T> iTemplates = null;

        try {

            InputStream resourceAsStream = JsonUtils.class.getClassLoader().getResourceAsStream(filaName);
            if (resourceAsStream == null) {
                log.error("JsonUtils, 加载{}失败", clazz);
                return Collections.emptyList();
            }

            InputStreamReader isr = new InputStreamReader(resourceAsStream);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder stringBuilder = new StringBuilder();
            String lineTxt = null;

            //将文件内容全部拼接到 字符串s
            while ((lineTxt = br.readLine()) != null) {
                stringBuilder.append(lineTxt);
            }
            iTemplates = JSON.parseArray(stringBuilder.toString(), clazz);
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return iTemplates;
    }

    public JSONArray readJsonArray(String filaName) {

        JSONArray result = null;

        try {

            InputStream resourceAsStream = JsonUtils.class.getClassLoader().getResourceAsStream(filaName);
            if (resourceAsStream == null) {
                log.error("JsonUtils, 加载{}失败", filaName);
                return new JSONArray();
            }

            InputStreamReader isr = new InputStreamReader(resourceAsStream);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder stringBuilder = new StringBuilder();
            String lineTxt = null;

            //将文件内容全部拼接到 字符串s
            while ((lineTxt = br.readLine()) != null) {
                stringBuilder.append(lineTxt);
            }
            result = JSON.parseArray(stringBuilder.toString());
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
package com.leyou.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtilsTest {

    @Test
    public void testSerializeAndParseMap() {
        Map<String, String> source = new HashMap<>();
        source.put("username", "leyou_user");
        source.put("role", "admin");

        String json = JsonUtils.serialize(source);
        Assert.assertNotNull(json);

        Map<String, String> parsed = JsonUtils.parseMap(json, String.class, String.class);
        Assert.assertNotNull(parsed);
        Assert.assertEquals("leyou_user", parsed.get("username"));
        Assert.assertEquals("admin", parsed.get("role"));
        Assert.assertNull(JsonUtils.serialize(null));
        Assert.assertEquals("plain-string", JsonUtils.serialize("plain-string"));
    }

    @Test
    public void testParseList() {
        List<Integer> numbers = Arrays.asList(10, 20, 30);
        String json = JsonUtils.serialize(numbers);
        Assert.assertEquals("[10,20,30]", json);

        List<Integer> parsed = JsonUtils.parseList(json, Integer.class);
        Assert.assertEquals(numbers, parsed);
    }

    @Test
    public void testNativeReadAndInvalidJson() {
        String json = "[{\"id\":1,\"name\":\"手机\"},{\"id\":2,\"name\":\"电脑\"}]";
        List<Map<String, Object>> result = JsonUtils.nativeRead(json, new TypeReference<List<Map<String, Object>>>() {});
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("手机", result.get(0).get("name"));

        Assert.assertNull(JsonUtils.parse("{invalid-json}", Map.class));
    }
}

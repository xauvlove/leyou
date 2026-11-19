package com.leyou.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class NumberUtilsTest {

    @Test
    public void testIsDigitAndIsInt() {
        Assert.assertTrue(NumberUtils.isDigit("123456"));
        Assert.assertFalse(NumberUtils.isDigit("12a45"));
        Assert.assertFalse(NumberUtils.isDigit(""));
        Assert.assertFalse(NumberUtils.isDigit(null));

        Assert.assertTrue(NumberUtils.isInt(10.0));
        Assert.assertFalse(NumberUtils.isInt(10.5));
    }

    @Test
    public void testScaleAndSearchNumber() {
        Assert.assertEquals(3.14, NumberUtils.scale(3.14159, 2), 0.0001);
        Assert.assertEquals(3.15, NumberUtils.scale(3.146, 2), 0.0001);

        Double[] found = NumberUtils.searchNumber("price: 199.5, discount: 20", "price: (\\d+\\.\\d+), discount: (\\d+)");
        Assert.assertEquals(2, found.length);
        Assert.assertEquals(Double.valueOf(199.5), found[0]);
        Assert.assertEquals(Double.valueOf(20.0), found[1]);
    }

    @Test
    public void testGenerateCode() {
        String code = NumberUtils.generateCode(6);
        Assert.assertNotNull(code);
        Assert.assertEquals(6, code.length());
        Assert.assertTrue(NumberUtils.isDigit(code));
    }
}

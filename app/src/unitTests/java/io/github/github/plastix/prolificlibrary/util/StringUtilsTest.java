package io.github.github.plastix.prolificlibrary.util;

import org.junit.Assert;
import org.junit.Test;

import io.github.plastix.prolificlibrary.util.StringUtils;

public class StringUtilsTest {

    @Test
    public void nullReturnsFalse() {
        Assert.assertEquals(StringUtils.isNotNullOrEmpty(null), false);
    }

    @Test
    public void emptyReturnsFalse() {
        Assert.assertEquals(StringUtils.isNotNullOrEmpty(""), false);
    }

    @Test
    public void charReturnsTrue() {
        Assert.assertEquals(StringUtils.isNotNullOrEmpty("a"), true);
    }

    @Test
    public void spaceReturnsTrue() {
        Assert.assertEquals(StringUtils.isNotNullOrEmpty(" "), true);
    }

    @Test
    public void newLineReturnsTrue() {
        Assert.assertEquals(StringUtils.isNotNullOrEmpty("\n"), true);
    }
}

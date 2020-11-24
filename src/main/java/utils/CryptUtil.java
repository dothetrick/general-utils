package utils;

import org.apache.commons.codec.digest.DigestUtils;

public class CryptUtil {
    public String md5(String str) {
        return DigestUtils.md5Hex(str);
    }
}

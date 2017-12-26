package com.example.zhouyanglei.myapplication;

import com.blankj.utilcode.util.EncryptUtils;
import com.im.qtec.utils.EncryptAES;

import org.junit.Test;
import static org.junit.Assert.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        EncryptUtils.AES_Transformation = "AES/ECB/PKCS7Padding";
        Security.addProvider(new BouncyCastleProvider());
        byte[] encryptContent = EncryptUtils.encryptAES("12345678".getBytes(), "1234567812345678".getBytes());
        byte[] decrypt = EncryptUtils.decryptAES(encryptContent, "1234567812345678".getBytes());
        assertEquals("12345678".getBytes()[2], decrypt[2]);
    }

    @Test
    public void isCorrect() throws Exception {
        EncryptUtils.AES_Transformation = "AES/CFB/NoPadding";
        byte[] encryptContent = EncryptUtils.encryptAES("1".getBytes(),new byte[]{1,2,3,4,1,2,3,4,1,2,3,4,1,2,3,4});
        byte[] decrypt = EncryptUtils.decryptAES(encryptContent, new byte[]{1,2,3,4,1,2,3,4,1,2,3,4,1,2,3,4});
        assertEquals("1".getBytes()[0], decrypt[0]);
    }
}
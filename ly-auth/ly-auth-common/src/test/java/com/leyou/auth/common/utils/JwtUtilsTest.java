package com.leyou.auth.common.utils;

import com.leyou.auth.common.pojo.UserInfo;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;
import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtUtilsTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testGenerateAndParseTokenWithKeyObjects() throws Exception {
        File pubFile = new File(tempFolder.getRoot(), "rsa.pub");
        File priFile = new File(tempFolder.getRoot(), "rsa.pri");
        RsaUtils.generateKey(pubFile.getAbsolutePath(), priFile.getAbsolutePath(), "leyou-test-secret");

        PublicKey publicKey = RsaUtils.getPublicKey(pubFile.getAbsolutePath());
        PrivateKey privateKey = RsaUtils.getPrivateKey(priFile.getAbsolutePath());

        UserInfo expected = new UserInfo(1001L, "testuser");
        String token = JwtUtils.generateToken(expected, privateKey, 30);
        Assert.assertNotNull(token);

        UserInfo actual = JwtUtils.getInfoFromToken(token, publicKey);
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getUsername(), actual.getUsername());
    }

    @Test
    public void testGenerateAndParseTokenWithKeyBytes() throws Exception {
        File pubFile = new File(tempFolder.getRoot(), "rsa_bytes.pub");
        File priFile = new File(tempFolder.getRoot(), "rsa_bytes.pri");
        RsaUtils.generateKey(pubFile.getAbsolutePath(), priFile.getAbsolutePath(), "leyou-bytes-secret");

        byte[] pubBytes = Files.readAllBytes(pubFile.toPath());
        byte[] priBytes = Files.readAllBytes(priFile.toPath());

        UserInfo expected = new UserInfo(2002L, "sequcc");
        String token = JwtUtils.generateToken(expected, priBytes, 30);

        UserInfo actual = JwtUtils.getInfoFromToken(token, pubBytes);
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getUsername(), actual.getUsername());
    }

    @Test(expected = ExpiredJwtException.class)
    public void testExpiredTokenThrowsException() throws Exception {
        File pubFile = new File(tempFolder.getRoot(), "rsa_exp.pub");
        File priFile = new File(tempFolder.getRoot(), "rsa_exp.pri");
        RsaUtils.generateKey(pubFile.getAbsolutePath(), priFile.getAbsolutePath(), "leyou-exp-secret");

        PublicKey publicKey = RsaUtils.getPublicKey(pubFile.getAbsolutePath());
        PrivateKey privateKey = RsaUtils.getPrivateKey(priFile.getAbsolutePath());

        UserInfo userInfo = new UserInfo(3003L, "expired_user");
        String expiredToken = JwtUtils.generateToken(userInfo, privateKey, -5);
        JwtUtils.getInfoFromToken(expiredToken, publicKey);
    }
}

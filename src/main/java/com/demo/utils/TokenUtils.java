package com.demo.utils;

import java.io.IOException;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

public class TokenUtils {

	private static final String SECRET = "AE79909B06A84BBEBF292A01388B5140";

	/**
	 * Payload标准字段 iss : The issuer of the token，token 是给谁的 sub : The subject of
	 * the token，token 主题 exp : Expiration Time。 token 过期时间，Unix 时间戳格式 iat :
	 * IssuedAt。 token 创建时间， Unix 时间戳格式 jti : JWT ID。针对当前 token 的唯一标识 aud :
	 * Audience（接收方） nbf ： Not before token使用时间不得早于设置时间
	 * 
	 * @param args
	 * @throws IOException
	 */
	/**
	 * 生成token
	 * 
	 * @param jti
	 *            token ID
	 * @param iss
	 *            发行着
	 * @param sub
	 *            主题
	 * @param adu
	 *            接收者
	 * @param iat
	 *            创建时间
	 * @param exp
	 *            过期时间
	 * @param nbf
	 *            不早于
	 * @return 返回token字符串，格式：xxx.xxx.xxx
	 */
	public static String generateToken(String jti) {
		Date date = new Date();
		String token = JWT.create().withJWTId(jti)
				.withIssuedAt(date)
				.withExpiresAt(new Date(date.getTime() + 1000 * 60 * 10))// 过期10分钟
				.withNotBefore(date).sign(Algorithm.HMAC256(SECRET.getBytes()));
		return token;
	}

	/**
	 * 验证token格式,正确则返回数据对象
	 * 
	 * @param token
	 * @return
	 */
	public static JWT verifyToken(String token) {
		JWTVerifier verifier = JWT
				.require(Algorithm.HMAC256(SECRET.getBytes())).build();
		JWT jwt = (JWT) verifier.verify(token);
		return jwt;
	}

	public static void main(String[] args) throws IOException {
		String token = generateToken("111");
		System.out.println(token);
		JWT jwt = verifyToken(token);
		System.out.println(jwt.getId());
	}
}

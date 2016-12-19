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
	 * Payload��׼�ֶ� iss : The issuer of the token��token �Ǹ�˭�� sub : The subject of
	 * the token��token ���� exp : Expiration Time�� token ����ʱ�䣬Unix ʱ�����ʽ iat :
	 * IssuedAt�� token ����ʱ�䣬 Unix ʱ�����ʽ jti : JWT ID����Ե�ǰ token ��Ψһ��ʶ aud :
	 * Audience�����շ��� nbf �� Not before tokenʹ��ʱ�䲻����������ʱ��
	 * 
	 * @param args
	 * @throws IOException
	 */
	/**
	 * ����token
	 * 
	 * @param jti
	 *            token ID
	 * @param iss
	 *            ������
	 * @param sub
	 *            ����
	 * @param adu
	 *            ������
	 * @param iat
	 *            ����ʱ��
	 * @param exp
	 *            ����ʱ��
	 * @param nbf
	 *            ������
	 * @return ����token�ַ�������ʽ��xxx.xxx.xxx
	 */
	public static String generateToken(String jti) {
		Date date = new Date();
		String token = JWT.create().withJWTId(jti)
				.withIssuedAt(date)
				.withExpiresAt(new Date(date.getTime() + 1000 * 60 * 10))// ����10����
				.withNotBefore(date).sign(Algorithm.HMAC256(SECRET.getBytes()));
		return token;
	}

	/**
	 * ��֤token��ʽ,��ȷ�򷵻����ݶ���
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

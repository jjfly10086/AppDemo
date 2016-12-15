package com.demo.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.utils.MD5Utils;

/**
 * 自定义shiro md5 salt 密码匹配规则
 * 
 * @author admin
 *
 */
public class Md5SaltCredentialsMatcher extends HashedCredentialsMatcher {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public Md5SaltCredentialsMatcher() {
		super();
		setHashAlgorithmName(Md5Hash.ALGORITHM_NAME);
	}

	/**
	 * 重写doCredentialsMatch方法
	 */
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token,
			AuthenticationInfo info) {
		if (null != token && null != info) {
			if (token instanceof UsernamePasswordToken) {
				String plainPass = new String(
						((UsernamePasswordToken) token).getPassword());// 明文密码
				String pass = (String)info.getCredentials();
				logger.info("明文密码：" + plainPass + "---" + "密文密码：" + pass);
				if(MD5Utils.verify(plainPass, pass)){//验证通过
					return true;
				}
			}
		}
		return false;
	}
}

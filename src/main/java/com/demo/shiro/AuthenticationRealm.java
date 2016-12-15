package com.demo.shiro;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.bean.AuthBean;
import com.demo.bean.RoleBean;
import com.demo.bean.UserBean;
import com.demo.service.IUserService;

/**
 * 
 * @author admin AuthorizingRealmΪAuthenticatingRealm���࣬
 *         AuthenticatingRealm����CredentialsMatcher��������
 *         ������CredentialsMatcher����doCredentialsMatch������
 *         ��ˣ��Զ�����̳�CredentialsMatcher��������
 *         ����дdoCredentialsMatch����,�����Զ�����ע�뵽��ǰ��Ķ��������У���ʵ���Զ�������ƥ�����
 */
public class AuthenticationRealm extends AuthorizingRealm {

	private Logger logger = LoggerFactory.getLogger(AuthenticationRealm.class);
	@Resource
	private IUserService userService;

	/**
	 * ��֤ �÷����ĵ���ʱ��ΪLoginController.doLogin()������ִ��Subject.login()ʱ
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		UserBean user = userService.findUserByUsername(username);
		if (user == null) {
			logger.debug("δ�ҵ����˺�");
			// δ�ҵ��û�
			throw new UnknownAccountException("û���ҵ����˺�");

		}
		/**
		 * ����AuthenticatingRealmʹ��CredentialsMatcher��������ƥ��
		 */
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
				user.getUserName(), user.getUserPass(), getClass().getName());
		return info;
	}

	/**
	 * ��Ȩ Ϊ��ǰ��¼��Subject�����ɫ��Ȩ��
	 * 
	 * @see �÷����ĵ���ʱ��Ϊ����Ȩ��Դ������ʱ
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		String username = (String) principals.getPrimaryPrincipal();
		UserBean user = userService.findUserByUsername(username);
		List<RoleBean> roleList = userService.queryRoleListByUserId(user
				.getId());
		if (roleList != null && roleList.size() > 0) {
			for (RoleBean role : roleList) {
				List<AuthBean> authList = userService
						.queryAuthListByRoleId(role.getId());
				role.setAuthList(authList);
			}
			user.setRoleList(roleList);
		}
		// ��ɫ���ļ���
		Set<String> roles = new HashSet<String>();
		// Ȩ�����ļ���
		Set<String> auths = new HashSet<String>();

		if (user.getRoleList() != null && user.getRoleList().size() > 0) {
			for (RoleBean role : user.getRoleList()) {
				roles.add(role.getRoleName());
				for (AuthBean auth : role.getAuthList()) {
					auths.add(auth.getAuthName());
				}
			}
		}
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.addRoles(roles);
		authorizationInfo.addStringPermissions(auths);
		return authorizationInfo;
	}

}

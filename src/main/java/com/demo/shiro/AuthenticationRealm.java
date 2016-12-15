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
 * @author admin AuthorizingRealm为AuthenticatingRealm子类，
 *         AuthenticatingRealm中有CredentialsMatcher对象属性
 *         ，其中CredentialsMatcher具有doCredentialsMatch方法，
 *         因此，自定义类继承CredentialsMatcher或其子类
 *         ，重写doCredentialsMatch方法,将此自定义类注入到当前类的对象属性中，可实现自定义密码匹配规则
 */
public class AuthenticationRealm extends AuthorizingRealm {

	private Logger logger = LoggerFactory.getLogger(AuthenticationRealm.class);
	@Resource
	private IUserService userService;

	/**
	 * 认证 该方法的调用时机为LoginController.doLogin()方法中执行Subject.login()时
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		UserBean user = userService.findUserByUsername(username);
		if (user == null) {
			logger.debug("未找到该账号");
			// 未找到用户
			throw new UnknownAccountException("没有找到该账号");

		}
		/**
		 * 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
		 */
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
				user.getUserName(), user.getUserPass(), getClass().getName());
		return info;
	}

	/**
	 * 授权 为当前登录的Subject授予角色和权限
	 * 
	 * @see 该方法的调用时机为需授权资源被访问时
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
		// 角色名的集合
		Set<String> roles = new HashSet<String>();
		// 权限名的集合
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

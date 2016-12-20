package com.demo.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.demo.redis.IRedisService;
import com.demo.utils.BeanUtils;
import com.demo.utils.RSAUtils;
import com.demo.utils.TokenUtils;

/**
 * Servlet Filter implementation class AppFilter
 */
@WebFilter("/AppFilter")
public class AppFilter implements Filter {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * Default constructor. 
     */
    public AppFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		//1.loginController����Ҫ���ˣ�ֱ�ӷ���
		if(req.getRequestURI().contains("/app/login")){
			
		}else if(req.getRequestURI().contains("/app")){
			logger.info("��������"+req.getRequestURI());
			//��֤token�Ƿ�Ϸ�
			String token = req.getHeader("Authorization");
			if(null == token){
				res.setCharacterEncoding("utf-8");
				res.setContentType("text/html");
				res.setStatus(401);
				PrintWriter out = res.getWriter();
				out.write("{\"code\":1001,\"msg\":\"ȱ����֤��Ϣ\",\"data\":null}");
				out.flush();
				out.close();
				logger.info("����"+req.getRequestURI()+" ����֤��Ϣ");
				return ;
			}
			try{
				JWT jwt = TokenUtils.verifyToken(token);
			}catch(Exception e){
				logger.info("��֤����ͷ�쳣",e);
				res.setCharacterEncoding("utf-8");
				res.setContentType("text/html");
				res.setStatus(500);
				PrintWriter out = res.getWriter();
				out.write("{\"code\":1001,\"msg\":\"��֤��Ϣ����ȷ\",\"data\":null}");
				out.flush();
				out.close();
				return ;
			}
		}
		// pass the request along the filter chain
		chain.doFilter(request, response);
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		//�����ù�����ʱ��ʼ����Կ�Ե�Redis��������
		Map<String,String> keyPairMap = RSAUtils.generateKeyPair();
		IRedisService redisService = (IRedisService) BeanUtils.getBean("redisService");
		redisService.add("publicKey",keyPairMap.get("publicKey"));
		redisService.add("privateKey",keyPairMap.get("privateKey"));
		logger.info("��ʼ����Կ�Գɹ�---------------------------------------");
	}

}

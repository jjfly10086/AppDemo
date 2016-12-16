package com.demo.filter;

import java.io.IOException;
import java.io.PrintWriter;

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
import org.springframework.http.HttpStatus;

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
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		if(req.getRequestURI().contains("/app/login")){
			
		}else if(req.getRequestURI().contains("/app")){
			logger.info("过滤请求："+req.getRequestURI());
			//验证token是否合法
			String token = req.getHeader("Authorization");
			if(null == token){
				res.setCharacterEncoding("utf-8");
				res.setContentType("text/json");
				res.setStatus(401);
				PrintWriter out = res.getWriter();
				out.write("{'flag':1,'error':'缺少验证信息'}");
				out.flush();
				out.close();
				logger.info("请求："+req.getRequestURI()+" 无验证信息");
				return ;
			}
			if(null == TokenUtils.verifyToken(token)){
				res.setCharacterEncoding("utf-8");
				res.setContentType("text/json");
				res.setStatus(401);
				PrintWriter out = res.getWriter();
				out.write("{'flag':1,'error':'验证不通过'}");
				out.flush();
				out.close();
				logger.info("请求："+req.getRequestURI()+" 验证信息不正确");
				return ;
			}
			logger.info(req.getRemoteAddr()+" "+req.getRemoteHost()+req.getRemotePort()+req.getRemoteUser());
		}
		// pass the request along the filter chain
		chain.doFilter(request, response);
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

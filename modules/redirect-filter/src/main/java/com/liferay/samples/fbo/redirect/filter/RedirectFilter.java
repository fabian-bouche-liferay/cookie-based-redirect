package com.liferay.samples.fbo.redirect.filter;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.servlet.filters.BasePortalFilter;
import com.liferay.samples.fbo.redirect.filter.configuration.RedirectFilterConfiguration;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabian-Liferay
 */
@Component(
		immediate = true,
		configurationPid = "com.liferay.samples.fbo.redirect.filter.configuration.RedirectFilterConfiguration",
        property = {
        		"dispatcher=FORWARD",
        		"dispatcher=REQUEST",
        		"servlet-context-name=",
                "servlet-filter-name=New Site Redirect Filter",
                "url-pattern=/*"
        },		
		service = Filter.class
		)
public class RedirectFilter extends BasePortalFilter {

	private final static Logger LOG = LoggerFactory.getLogger(RedirectFilter.class);

	@Override
	public void processFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		LOG.debug("Do RedirectFilter {}", request.getRequestURL());

		LOG.debug("Is RedirectFilter enabled? {}", this._redirectFilterConfiguration.enabled());
		if(this._redirectFilterConfiguration.enabled()) {

			LOG.debug("Virtual host: {}", request.getServerName());
			if(virtualHostIsTargetted(request)) {
			
				String cookieName = this._redirectFilterConfiguration.redirectTriggerCookieName();
				String cookieValue = getCookie(cookieName, request);
				
				if(cookieValue != null) {

					LOG.debug("Has cookie [{}] with value: {}", cookieName, cookieValue);

					String regex = this._redirectFilterConfiguration.redirectTriggerCookieValue();
					
					if(cookieValue.matches(regex)) {
					
						LOG.debug("Redirecting user to: {}", this._redirectFilterConfiguration.redirectFilterUrl());
						response.sendRedirect(this._redirectFilterConfiguration.redirectFilterUrl());
						return;
						
					}
					
				}
				
			}
				
		}
		chain.doFilter(request, response);

	}

	private boolean virtualHostIsTargetted(HttpServletRequest httpServletRequest) {
		
		String[] virtualHosts = this._redirectFilterConfiguration.redirectFilterTargetVirtualHosts();
		for(int i = 0; i < virtualHosts.length; i++) {
			String virtualHost = virtualHosts[i];
			if(httpServletRequest.getServerName().equals(virtualHost)) return true;
		}
		
		return false;
	}

	private String getCookie(String cookieName, HttpServletRequest httpServletRequest) {
		
		Cookie[] cookies = httpServletRequest.getCookies();

		if(cookies == null) return null;

		for(int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if(cookie.getName().equals(cookieName)) return cookie.getValue();
		}

		LOG.debug("Does not have cookie {}", cookieName);

		return null;
		
	}
	
	private volatile RedirectFilterConfiguration _redirectFilterConfiguration;
	
	@Activate
	@Modified
	private void activate(Map<String, String> properties) {

		this._redirectFilterConfiguration = ConfigurableUtil.createConfigurable(RedirectFilterConfiguration.class, properties);
		
	}
}
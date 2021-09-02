package com.liferay.samples.fbo.redirect.filter.configuration;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(
		category = "third-party", scope = ExtendedObjectClassDefinition.Scope.SYSTEM
	)
@Meta.OCD(
	    id = "com.liferay.samples.fbo.redirect.filter.configuration.RedirectFilterConfiguration",
	    localization = "content/Language", name = "redirect-filter-configuration-name"
	)
public interface RedirectFilterConfiguration {

	@Meta.AD(deflt = "false", name = "redirect-filter-enabled", required = false)
	public boolean enabled();
	
	@Meta.AD(deflt = "", name = "redirect-trigger-cookie-name", required = false)
	public String redirectTriggerCookieName();

	@Meta.AD(deflt = "", name = "redirect-trigger-cookie-value", required = false)
	public String redirectTriggerCookieValue();
	
	@Meta.AD(deflt = "", name = "redirect-filter-url", required = false)
	public String redirectFilterUrl();

	@Meta.AD(deflt = "", name = "redirect-filter-target-virtual-hosts", required = false)
	public String[] redirectFilterTargetVirtualHosts();
	
}

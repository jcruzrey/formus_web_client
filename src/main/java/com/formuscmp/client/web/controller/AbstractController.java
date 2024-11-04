/**
 * 
 */
package com.formuscmp.client.web.controller;

import com.formuscmp.client.web.service.CmpService;
import org.springframework.util.MultiValueMap;
import com.formuscmp.client.web.service.AbstractService.ResourceType;
import com.mio.formas.response.ResponseResource;

/**
 * Default methods to integrate your controller logic
 */
public abstract class AbstractController {

	private final String defaultLocale;
	private final CmpService cmpService;
	
	/**
	 * Constructor
	 * @param cmpService CMP Api Rest Calls service
	 * @param defaultLocale Locale samples en_US or es_MX
	 */
	public AbstractController(final CmpService cmpService, final String defaultLocale) {
		this.cmpService = cmpService;
		this.defaultLocale = defaultLocale;
	}

	/**
	 * Execute call to Formus CMP Rest Api 
	 * @param commandName Method to call
	 * @param module Module name
	 * @param resourceName form or basket name
	 * @param version Version of your form or basket
	 * @param resourceType form or basket
	 * @return ResponseResource
	 */
	public ResponseResource restCall(final String commandName, final String module,
			final String resourceName, final String version, final ResourceType resourceType, final MultiValueMap<String, String> form, final boolean ajax) {
		return cmpService.restCall(commandName, module, resourceName, defaultLocale, version, resourceType, form, ajax);
		
	}
}

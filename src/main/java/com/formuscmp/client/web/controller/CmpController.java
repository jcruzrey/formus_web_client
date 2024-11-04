/**
 * 
 */
package com.formuscmp.client.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.formuscmp.client.web.service.CmpService;
import com.formuscmp.client.web.service.AbstractService.ResourceType;
import com.mio.formas.response.ResponseResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Sample controller to integrate Formus Case Management Platform with your applications
 * This is a sample controller, this is the reason why we used @RestController
 * change this type to @Controller, currently you will se the CMP response on your explorer as JSON.
 */
@Controller
public class CmpController extends AbstractController{

	/**
	 * Constructor
	 * @param cmpService Service for executing calls to Formus CMP 
	 * @param defaultLocale String locale of asset resources
	 */
	public CmpController(@Autowired CmpService cmpService, @Value("${app.language.default}") String defaultLocale) {
		super(cmpService, defaultLocale);
	}
	
	@GetMapping(value = { "/","/index" })
	public String getIndex() {
		return "/cmp/pages/index";
	}
	/**
	 * Sample end point to get resources from CMP
	 * @param resourceType form or basket
	 * @param module Module name in which your assets are grouped by
	 * @param resourceName Name of the form or basket to retrieve
	 * @param version which version you need your assets
	 * @param commandName the method of your form or basket to execute
	 * @return For testing purposes ResponseResource
	 */
	@GetMapping("/v2/{resourceType}/{module}/{resourceName}/{version}/{commandName}")
	public String getMethodName(Model model, @PathVariable String resourceType, @PathVariable String module,
			@PathVariable String resourceName, @PathVariable String version,
			@PathVariable String commandName) {
		ResponseResource responseResource = restCall(commandName, module,
				resourceName, version, ResourceType.valueOf(resourceType), null, false);
		model.addAttribute("model", responseResource);
		return "/cmp/pages/"+resourceType;
	}
	
	/**
	 * Sample end point to get resources from CMP
	 * @param model MVC model
	 * @param resourceType form or basket
	 * @param module Module name in which your assets are grouped by
	 * @param resourceName Name of the form or basket to retrieve
	 * @param version which version you need your assets
	 * @param commandName the method of your form or basket to execute
	 * @param formData submitted values
	 * @return ResponseResource
	 */
	@PostMapping(value = "/v2/{resourceType}/{module}/{resourceName}/{version}/{commandName}", consumes = "application/x-www-form-urlencoded; charset=UTF-8", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseResource postMethodName(Model model, @PathVariable String resourceType, @PathVariable String module,
			@PathVariable String resourceName, @PathVariable String version,
			@PathVariable String commandName, @RequestBody MultiValueMap<String, String> formData) {
		

		return restCall(commandName, module,
				resourceName, version, ResourceType.valueOf(resourceType), formData, true);
	}
}

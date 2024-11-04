/**
 * 
 */
package com.formuscmp.client.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.formuscmp.client.web.exception.CmpResponseErrorHandler;
import com.mio.comun.excepcion.ExcepcionApp;
import com.mio.formas.request.RequestResource;
import com.mio.formas.response.ResponseResource;

/**
 * Formus CMP Rest Api service calls
 */
public abstract class AbstractService {

	private final RestClient restClient;
	private final String cmpBaseUrl;
	private final String appBaseUrl;
	private final String cmpToken;
	private final String originApi;
	private final String deviceFormat;
	private final String assetLibrary;
	
	public enum ResourceType{
		basket("basket"),
		form("form");
		
		private final String type;       

	    private ResourceType(String type) {
	    	this.type = type;
	    }

	    public boolean equalsName(String type) {
	        return this.type.equals(type);
	    }

	    public String toString() {
	       return this.type;
	    }
	}
	
	/**
	 * Contructor
	 * @param cmpToken token assigned during registration
	 * @param cmpBaseURI Formus CMP endpoint url
	 * @param appBaseURI App hosting integration url
	 * @param origenApi the origin of call
	 * @param format expected format, depending on calling device OS
	 */
	public AbstractService(final String cmpToken, final String cmpBaseUrl, 
			final String appBaseUrl, final String originApi, 
			final String deviceFormat, final String assetLibrary) {
		this.cmpToken = cmpToken;
		this.cmpBaseUrl = cmpBaseUrl;
		this.appBaseUrl = appBaseUrl;
		this.originApi = originApi;
		this.deviceFormat = deviceFormat;
		this.assetLibrary = assetLibrary;
		this.restClient = restClient();
	}

	/**
	 * Execute call to Case Management Platform
	 * @param commandName Execution method
	 * @param module Module name
	 * @param assetLibrary Asset library assigned to you during registration
	 * @param resourceName Name of form or basket
	 * @param locale Default language
	 * @param version Version of your asset
	 * @param resourceType form or basket
	 * @return
	 */
	public ResponseResource restCall(final String commandName, final String module,
			final String resourceName, final String defaultLocale, 
			final String version, final ResourceType resourceType, final MultiValueMap<String, String> form, final boolean ajax){
		RequestResource requestResource = new RequestResource();

		requestResource.setCommandName(commandName);
		requestResource.setModule(module);
		requestResource.setAssetLibrary(assetLibrary);
		requestResource.setResourceName(resourceName);
		requestResource.setLocale(defaultLocale);
		requestResource.setVersion(version);
		requestResource.setResourceType(resourceType.toString());
		requestResource.setFormat(deviceFormat);
		requestResource.setBaseUri(appBaseUrl);
		requestResource.setOrigin(originApi);
		if (form!=null)
			requestResource.setParameters( form.entrySet().stream()
			    .collect(Collectors.toMap(                      
			            Entry::getKey,                          
			            e -> String.join(",", e.getValue()))));
		
		return restClient.post()
			    .uri("/form")
			    .contentType(MediaType.APPLICATION_JSON)
			    .body(requestResource)
			    .retrieve()
			    .onStatus(new CmpResponseErrorHandler(ajax))
			    .body(ResponseResource.class);

	}
	
	/**
	 * Calling client
	 * @return RestClient
	 */
	private RestClient restClient() {
		   List<MediaType> listMediaType = new ArrayList<MediaType>();
		   listMediaType.add(MediaType.APPLICATION_JSON);
		   
	  return RestClient.builder()
		      .baseUrl(cmpBaseUrl)
		      .defaultHeader("AUTHORIZATION", cmpToken)
		      .defaultHeader("content-type", "application/json")
		      .defaultHeader("accept", "application/json")
		      .messageConverters(converters -> {
		    	  MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
	                converter.setSupportedMediaTypes(listMediaType);
	                converters.add(converter);
	            })
		      .build();
	}
}

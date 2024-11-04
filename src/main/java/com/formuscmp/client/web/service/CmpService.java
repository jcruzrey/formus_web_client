/**
 * 
 */
package com.formuscmp.client.web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Connect to Formus Case Management Platform Api
 */
@Service
public class CmpService extends AbstractService{
	
	/**
	 * 
	 * @param cmpToken end user assigned token after registration in formus.ws
	 * @param cmpBaseUrl Case Management Platform endpoint url
	 * @param appBaseUrl Application url hosting 
	 * @param originApi
	 * @param deviceFormat
	 * @param assetLibrary
	 */
	public CmpService(@Value("${formus.cmp.token}") final String cmpToken,
			@Value("${formus.cmp.url}") final String cmpBaseUrl, @Value("${app.base.url}") final String appBaseUrl,
			@Value("${app.origin.api}") final String originApi, @Value("${app.device.format}") final String deviceFormat,
			@Value("${app.asset.library}") final String assetLibrary) {
		super(cmpToken, cmpBaseUrl, appBaseUrl, originApi, deviceFormat, assetLibrary);
	}

}

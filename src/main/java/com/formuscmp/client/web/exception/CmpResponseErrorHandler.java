/**
 * 
 */
package com.formuscmp.client.web.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mio.comun.excepcion.ErrorMessage;
import com.mio.comun.excepcion.ExcepcionApp;

import jakarta.ws.rs.NotFoundException;

/**
 * Api Rest Integration Error handling
 */
public class CmpResponseErrorHandler implements ResponseErrorHandler {

	private final boolean isAjaxRequest;
	public CmpResponseErrorHandler(boolean isAjaxRequest) {
		super();
		this.isAjaxRequest=isAjaxRequest;
	}

	@Override
	public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
		return httpResponse.getStatusCode().is5xxServerError() || 
	            httpResponse.getStatusCode().is4xxClientError();
	}

	@Override
	public void handleError(ClientHttpResponse httpResponse) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		if (httpResponse.getStatusCode().is5xxServerError() && isAjaxRequest) {
            //Handle SERVER_ERROR
			ErrorMessage errorMessage = mapper.readValue(httpResponse.getBody(), ErrorMessage.class);
            throw new ExcepcionApp(errorMessage.getStatus(),errorMessage.getCode(), errorMessage.getMessage(), null, null);
        } else if (httpResponse.getStatusCode().is4xxClientError() && isAjaxRequest) {
            //Handle CLIENT_ERROR
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
    			ErrorMessage errorMessage = mapper.readValue(httpResponse.getBody(), ErrorMessage.class);
                throw new ExcepcionApp(errorMessage.getStatus(),errorMessage.getCode(), errorMessage.getMessage(), null, null);
            }
        }else if (httpResponse.getStatusCode().is5xxServerError()) {
            //Handle SERVER_ERROR
            throw new HttpClientErrorException(httpResponse.getStatusCode());
        } else if (httpResponse.getStatusCode().is4xxClientError()) {
            //Handle CLIENT_ERROR
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException();
            }
        }

	}

}

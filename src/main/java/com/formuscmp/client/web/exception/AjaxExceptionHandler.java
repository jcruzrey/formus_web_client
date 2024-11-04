/**
 * 
 */
package com.formuscmp.client.web.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.mio.comun.excepcion.ErrorMessage;
import com.mio.comun.excepcion.ExcepcionApp;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * 
 */
@ControllerAdvice
public class AjaxExceptionHandler {	
	@ExceptionHandler({ExcepcionApp.class})
	@ResponseBody
    public Response handleRuntimeException(ExcepcionApp ex) {
        return Response.status(ex.getStatus()).entity(new ErrorMessage(ex)).type(MediaType.APPLICATION_JSON).build();
    }
	
}

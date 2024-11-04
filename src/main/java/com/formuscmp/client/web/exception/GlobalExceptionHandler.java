/**
 * 
 */
package com.formuscmp.client.web.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 
 */
@ControllerAdvice
public class GlobalExceptionHandler {	
	@ExceptionHandler({RuntimeException.class})
    public String handleRuntimeException(RuntimeException exception, Model model) {
		model.addAttribute("exception", exception);
        return "/cmp/pages/error";
    }
	
}

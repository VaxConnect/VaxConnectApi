package com.salesianos.triana.VaxConnectApi.administration.error;

import com.salesianos.triana.VaxConnectApi.error.model.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(AdministrationErrorController.ERROR_PATH)
public class AdministrationErrorController extends AbstractErrorController {

    static final String ERROR_PATH = "/error/administration";

    public String getErrorPath() {
        return ERROR_PATH;
    }
    public AdministrationErrorController(ErrorAttributes errorAttributes)  {
        super(errorAttributes, Collections.emptyList());
    }
    @RequestMapping
    public ResponseEntity<ApiError> error(HttpServletRequest request){
        Map<String, Object> mapErrorAttributes = this.getErrorAttributes(request,
                ErrorAttributeOptions.of(
                        ErrorAttributeOptions.Include.BINDING_ERRORS,
                        ErrorAttributeOptions.Include.EXCEPTION,
                        ErrorAttributeOptions.Include.MESSAGE,
                        ErrorAttributeOptions.Include.STACK_TRACE));
        ApiError apiError = ApiError.fromErrorAttributes(mapErrorAttributes);
        HttpStatus status = this.getStatus(request);
        return ResponseEntity.status(status).body(apiError);
    }


}

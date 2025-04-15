package f66.springboot_mvc_starter.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ApiResponse extends ResponseEntity<Object> {

    public ApiResponse(Object body, HttpStatus status) {

        super(body, status);
    }

    public static ApiResponse status(HttpStatus status) {

        return new ApiResponse(null, status);
    }

    public static ApiResponse statusOk() {

        return new ApiResponse(null, HttpStatus.OK);
    }

    public static ApiResponse statusCreated() {

        return new ApiResponse(null, HttpStatus.CREATED);
    }

    public static ApiResponse statusBadRequest() {

        return new ApiResponse(null, HttpStatus.BAD_REQUEST);
    }

    public static ApiResponse statusUnauthorized() {

        return new ApiResponse(null, HttpStatus.UNAUTHORIZED);
    }

    public static ApiResponse statusForbidden() {

        return new ApiResponse(null, HttpStatus.FORBIDDEN);
    }

    public static ApiResponse statusNotFound() {

        return new ApiResponse(null, HttpStatus.NOT_FOUND);
    }

    public static ApiResponse statusInternalServerError() {

        return new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ApiResponse body(Map<String, Object> body, HttpStatus status) {

        return new ApiResponse(body, status);
    }

    public static ApiResponse bodyOk(Map<String, Object> body) {

        return new ApiResponse(body, HttpStatus.OK);
    }

    public static ApiResponse bodyCreated(Map<String, Object> body) {

        return new ApiResponse(body, HttpStatus.CREATED);
    }

    public static ApiResponse bodyBadRequest(Map<String, Object> body) {

        return new ApiResponse(body, HttpStatus.BAD_REQUEST);
    }
}

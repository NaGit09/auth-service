package org.example.authservice.utils;

import org.example.authservice.model.dto.APIResponse;
import org.example.authservice.model.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;

public class GenerateResponse {
    public static ResponseEntity<?> generateError(Integer status, String message) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(status, message)
        );
    }

    public static ResponseEntity<?> generateSuccess(Integer status, String message, Object data) {
        return ResponseEntity.ok().body(
                new APIResponse<>(
                        status, message, data
                )
        );
    }
}

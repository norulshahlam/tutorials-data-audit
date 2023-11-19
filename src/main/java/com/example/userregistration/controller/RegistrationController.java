package com.example.userregistration.controller;

import com.example.userregistration.entity.UserEntity;
import com.example.userregistration.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Tag(name = "Registration module")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Bad Request",
                content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized Access",
                content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "The server has not found anything matching the URI given",
                content = @Content),
        @ApiResponse(responseCode = "405", description = "Method not Allowed",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content),
        @ApiResponse(responseCode = "503", description = "Service Unavailable",
                content = @Content)})
public class RegistrationController {

    private final UserService userService;

    @ApiResponse(responseCode = "200", description = "Successfully registered",
            content = @Content(mediaType = "application/json"))
    @Operation(summary = "Register user",
            description = "This endpoint will register users based on the given input data")
    @PostMapping("registerUser")
    public ResponseEntity<UserEntity> registerUser(@Valid @RequestBody @NotBlank UserEntity request) {

        log.info("in RegistrationController::registerUser");
        UserEntity registrationRequest = userService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(registrationRequest);
    }
    @ApiResponse(responseCode = "200", description = "User edited",
            content = @Content(mediaType = "application/json"))
    @Operation(summary = "Edit existing user",
            description = "This endpoint will edit existing user")
    @PostMapping("editUser")
    public ResponseEntity<UserEntity> editUser(@Valid @RequestBody @NotBlank UserEntity request) {
        log.info("in RegistrationController::editUser");
        UserEntity registrationRequest = userService.editUser(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(registrationRequest);
    }
}

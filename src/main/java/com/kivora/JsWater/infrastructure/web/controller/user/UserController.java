package com.kivora.JsWater.infrastructure.web.controller.user;

import com.kivora.JsWater.application.usecase.user.CreateUserUseCase;
import com.kivora.JsWater.application.usecase.user.LoginUseCase;
import com.kivora.JsWater.application.usecase.user.ResetPasswordUseCase;
import com.kivora.JsWater.application.usecase.user.ListUsersUseCase;
import com.kivora.JsWater.application.usecase.user.AdminResetUserPasswordUseCase;
import com.kivora.JsWater.application.usecase.user.ChangeUserStatusUseCase;
import com.kivora.JsWater.domain.model.user.UserRole;
import com.kivora.JsWater.infrastructure.security.jwt.TokenBlacklistService;
import com.kivora.JsWater.infrastructure.security.ratelimiting.LoginRateLimiter;
import com.kivora.JsWater.infrastructure.web.dto.user.RegisterUserResponse;
import com.kivora.JsWater.infrastructure.web.dto.user.UserRequest;
import com.kivora.JsWater.infrastructure.web.dto.user.UserResponse;
import com.kivora.JsWater.infrastructure.web.dto.user.ResetPasswordRequest;
import com.kivora.JsWater.infrastructure.web.dto.user.AdminResetPasswordRequest;
import com.kivora.JsWater.infrastructure.web.dto.user.UserAdminResponse;
import com.kivora.JsWater.domain.model.user.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4300")
@RestController
@RequestMapping("/api/auth")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Auth", description = "Autenticação e gestão de utilizadores")
public class UserController {

    private final LoginUseCase login;
    private final CreateUserUseCase createUser;
    private final TokenBlacklistService tokenBlacklistService;
    private final LoginRateLimiter loginRateLimiter;
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final AdminResetUserPasswordUseCase adminResetUserPasswordUseCase;
    private final ChangeUserStatusUseCase changeUserStatusUseCase;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(LoginUseCase login, CreateUserUseCase createUser, TokenBlacklistService tokenBlacklistService, LoginRateLimiter loginRateLimiter, ResetPasswordUseCase resetPasswordUseCase, ListUsersUseCase listUsersUseCase, AdminResetUserPasswordUseCase adminResetUserPasswordUseCase, ChangeUserStatusUseCase changeUserStatusUseCase) {
        this.login = login;
        this.createUser = createUser;
        this.tokenBlacklistService = tokenBlacklistService;
        this.loginRateLimiter = loginRateLimiter;
        this.resetPasswordUseCase = resetPasswordUseCase;
        this.listUsersUseCase = listUsersUseCase;
        this.adminResetUserPasswordUseCase = adminResetUserPasswordUseCase;
        this.changeUserStatusUseCase = changeUserStatusUseCase;
    }

        @Operation(
            summary = "Fazer login",
            description = "Autentica o utilizador com username e password e devolve o token JWT."
        )
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas ou pedido mal formado")
        })

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody UserRequest request, HttpServletRequest httpRequest) {
        String key = httpRequest.getRemoteAddr() + ":" + request.username();

        if (loginRateLimiter.isBlocked(key)) {
            logger.warn("Login bloqueado por rate limiting para: {}", key);
            throw new com.kivora.JsWater.domain.exception.AuthenticationException("Muitas tentativas de login. Tente novamente mais tarde.");
        }

        try {
            UserResponse response = login.execute(request.username(), request.password());
            loginRateLimiter.reset(key);
            return response;
        } catch (com.kivora.JsWater.domain.exception.AuthenticationException ex) {
            loginRateLimiter.registerFailure(key);
            throw ex;
        }
    }

        
        @Operation(
            summary = "Registar utilizador",
            description = "Cria um novo utilizador com role USER."
        )
        @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Utilizador criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
        })
        @PostMapping("/register")
        public RegisterUserResponse register(@Valid @RequestBody UserRequest request) {
        var user = createUser.execute(request.username(), request.password(), UserRole.USER);
        return new RegisterUserResponse(user.getName(), user.getRole().name());
    }

        @Operation(
            summary = "Logout",
            description = "Revoga o token JWT actual e termina a sessão do utilizador."
        )
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout efectuado com sucesso")
        })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.revoke(token);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                logger.info("Logout efectuado para utilizador: {}", auth.getName());
            } else {
                logger.info("Logout efectuado para token sem contexto de autenticação.");
            }
        }
        return ResponseEntity.ok().build();
    }

        @Operation(
            summary = "Alterar a própria password",
            description = "Permite ao utilizador autenticado alterar a sua password, validando a password actual."
        )
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password alterada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "400", description = "Password actual inválida ou dados inválidos")
        })
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        resetPasswordUseCase.execute(username, request.currentPassword(), request.newPassword());

        return ResponseEntity.ok().build();
    }

        @Operation(
            summary = "Listar utilizadores",
            description = "Lista todos os utilizadores registados (apenas ADMIN)."
        )
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de utilizadores"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
        })
    @GetMapping("/users")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public java.util.List<UserAdminResponse> listUsers() {
        return listUsersUseCase.execute().stream()
                .map(UserAdminResponse::from)
                .toList();
    }

        @Operation(
            summary = "Reset de password (ADMIN)",
            description = "Permite ao ADMIN redefinir a password de qualquer utilizador informado pelo username."
        )
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password redefinida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
        })
    @PostMapping("/admin/reset-password")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> adminResetPassword(@Valid @RequestBody AdminResetPasswordRequest request) {
        adminResetUserPasswordUseCase.execute(request.username(), request.newPassword());
        return ResponseEntity.ok().build();
    }

        @Operation(
            summary = "Alterar status do utilizador",
            description = "Activa ou desactiva um utilizador (apenas ADMIN)."
        )
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status do utilizador actualizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
        })
    @PostMapping("/users/{id}/status")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changeStatus(
            @PathVariable("id") java.util.UUID id,
            @RequestParam("status") UserStatus status
    ) {
        changeUserStatusUseCase.execute(id, status);
        return ResponseEntity.ok().build();
    }
}

package com.kivora.JsWater.infrastructure.web.controller.notification;

import com.kivora.JsWater.application.usecase.notification.CreateNotificationUseCase;
import com.kivora.JsWater.application.usecase.notification.GetUnreadNotificationCountUseCase;
import com.kivora.JsWater.application.usecase.notification.ListNotificationsUseCase;
import com.kivora.JsWater.application.usecase.notification.MarkAllNotificationsAsReadUseCase;
import com.kivora.JsWater.application.usecase.notification.MarkNotificationAsReadUseCase;
import com.kivora.JsWater.domain.model.notification.Notification;
import com.kivora.JsWater.domain.model.notification.NotificationCategory;
import com.kivora.JsWater.domain.model.notification.NotificationStatus;
import com.kivora.JsWater.domain.model.notification.NotificationType;
import com.kivora.JsWater.infrastructure.web.dto.notification.NotificationResponse;
import com.kivora.JsWater.infrastructure.web.dto.common.PageResponse;
import com.kivora.JsWater.domain.repository.UserRepository;
import com.kivora.JsWater.domain.model.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Gestão de notificações de sistema")
public class NotificationController {

    private final ListNotificationsUseCase listNotificationsUseCase;
    private final GetUnreadNotificationCountUseCase getUnreadNotificationCountUseCase;
    private final MarkNotificationAsReadUseCase markNotificationAsReadUseCase;
    private final MarkAllNotificationsAsReadUseCase markAllNotificationsAsReadUseCase;
    private final CreateNotificationUseCase createNotificationUseCase;
        private final UserRepository userRepository;

    public NotificationController(
            ListNotificationsUseCase listNotificationsUseCase,
            GetUnreadNotificationCountUseCase getUnreadNotificationCountUseCase,
            MarkNotificationAsReadUseCase markNotificationAsReadUseCase,
            MarkAllNotificationsAsReadUseCase markAllNotificationsAsReadUseCase,
                        CreateNotificationUseCase createNotificationUseCase,
                        UserRepository userRepository
    ) {
        this.listNotificationsUseCase = listNotificationsUseCase;
        this.getUnreadNotificationCountUseCase = getUnreadNotificationCountUseCase;
        this.markNotificationAsReadUseCase = markNotificationAsReadUseCase;
        this.markAllNotificationsAsReadUseCase = markAllNotificationsAsReadUseCase;
        this.createNotificationUseCase = createNotificationUseCase;
                this.userRepository = userRepository;
    }

        private User getAuthenticatedUser() {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth == null) {
                        throw new IllegalStateException("No authenticated user in context");
                }
                String username = auth.getName();
                return userRepository.findByUsername(username)
                                .orElseThrow(() -> new IllegalStateException("User not found: " + username));
        }

    @Operation(
            summary = "Listar notificações do utilizador",
            description = "Lista notificações de um utilizador, com filtros opcionais por status e categoria."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de notificações carregada com sucesso")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
        public PageResponse<NotificationResponse> list(
                        @RequestParam(name = "status", required = false) NotificationStatus status,
                        @RequestParam(name = "category", required = false) NotificationCategory category,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "20") int size
        ) {
                User user = getAuthenticatedUser();

                if (size <= 0) {
                        size = 20;
                }
                if (page < 0) {
                        page = 0;
                }

                List<Notification> all = listNotificationsUseCase.execute(user.getId(), status, category);

                long totalElements = all.size();
                int totalPages = totalElements == 0 ? 0 : (int) Math.ceil((double) totalElements / size);

                int fromIndex = page * size;
                if (fromIndex >= all.size()) {
                        return new PageResponse<>(List.of(), page, size, totalElements, totalPages);
                }
                int toIndex = Math.min(fromIndex + size, all.size());

                List<NotificationResponse> content = all.subList(fromIndex, toIndex)
                                .stream()
                                .map(NotificationResponse::from)
                                .toList();

                return new PageResponse<>(content, page, size, totalElements, totalPages);
    }

    @Operation(
            summary = "Contar notificações não lidas",
            description = "Devolve o número de notificações não lidas de um utilizador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contador obtido com sucesso")
    })
    @GetMapping("/unread-count")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
        public long getUnreadCount() {
                User user = getAuthenticatedUser();
                return getUnreadNotificationCountUseCase.execute(user.getId());
    }

    @Operation(
            summary = "Marcar notificação como lida",
            description = "Marca uma notificação específica como lida."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Notificação marcada como lida")
    })
    @PostMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markAsRead(@PathVariable("id") UUID id) {
        markNotificationAsReadUseCase.execute(id);
    }

    @Operation(
            summary = "Marcar todas as notificações como lidas",
            description = "Marca todas as notificações não lidas de um utilizador como lidas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Notificações marcadas como lidas")
    })
    @PostMapping("/read-all")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
        public void markAllAsRead() {
                User user = getAuthenticatedUser();
                markAllNotificationsAsReadUseCase.execute(user.getId());
    }

    public record CreateNotificationRequest(
            @NotNull UUID userId,
            @NotBlank String title,
            @NotBlank String message,
            @NotNull NotificationCategory category,
            @NotNull NotificationType type,
            String metadata
    ) {}

    @Operation(
            summary = "Criar notificação",
            description = "Cria uma nova notificação para um utilizador (uso interno ou painel de admin)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Notificação criada com sucesso")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationResponse create(@RequestBody CreateNotificationRequest request) {
        Notification notification = createNotificationUseCase.execute(
                request.userId(),
                request.title(),
                request.message(),
                request.category(),
                request.type(),
                request.metadata()
        );
        return NotificationResponse.from(notification);
    }
}

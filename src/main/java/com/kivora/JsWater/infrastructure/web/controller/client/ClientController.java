package com.kivora.JsWater.infrastructure.web.controller.client;

import com.kivora.JsWater.application.usecase.client.GetClientByIdUseCase;
import com.kivora.JsWater.application.usecase.client.RegisterClientUseCase;
import com.kivora.JsWater.application.usecase.client.SearchClientsUseCase;
import com.kivora.JsWater.application.usecase.client.UpdateClientAddressUseCase;
import com.kivora.JsWater.application.usecase.client.ChangeClientStatusUseCase;
import com.kivora.JsWater.application.usecase.client.UpdateClientUseCase;
import com.kivora.JsWater.application.usecase.client.DeleteClientUseCase;
import com.kivora.JsWater.application.usecase.invoice.GetClientDebtSummaryUseCase;
import com.kivora.JsWater.domain.model.client.ClientStatus;
import com.kivora.JsWater.domain.model.client.Client;
import com.kivora.JsWater.infrastructure.web.dto.client.ClientResponse;
import com.kivora.JsWater.infrastructure.web.dto.client.RegisterClientRequest;
import com.kivora.JsWater.infrastructure.web.dto.client.UpdateClientAddressRequest;
import com.kivora.JsWater.infrastructure.web.dto.client.UpdateClientRequest;
import com.kivora.JsWater.infrastructure.web.dto.invoice.ClientDebtSummaryResponse;
import com.kivora.JsWater.infrastructure.web.dto.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4300")
@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "Gestão de clientes")
public class ClientController {

    private final RegisterClientUseCase registerClientUseCase;
        private final GetClientByIdUseCase getClientByIdUseCase;
        private final SearchClientsUseCase searchClientsUseCase;
        private final UpdateClientAddressUseCase updateClientAddressUseCase;
        private final GetClientDebtSummaryUseCase getClientDebtSummaryUseCase;
        private final ChangeClientStatusUseCase changeClientStatusUseCase;
        private final UpdateClientUseCase updateClientUseCase;
        private final DeleteClientUseCase deleteClientUseCase;

        public ClientController(RegisterClientUseCase registerClientUseCase,
                                                        GetClientByIdUseCase getClientByIdUseCase,
                                                        SearchClientsUseCase searchClientsUseCase,
                                                        UpdateClientAddressUseCase updateClientAddressUseCase,
                                                        GetClientDebtSummaryUseCase getClientDebtSummaryUseCase,
                                                        ChangeClientStatusUseCase changeClientStatusUseCase,
                                                        UpdateClientUseCase updateClientUseCase,
                                                        DeleteClientUseCase deleteClientUseCase) {
        this.registerClientUseCase = registerClientUseCase;
                this.getClientByIdUseCase = getClientByIdUseCase;
                this.searchClientsUseCase = searchClientsUseCase;
                this.updateClientAddressUseCase = updateClientAddressUseCase;
                this.getClientDebtSummaryUseCase = getClientDebtSummaryUseCase;
                this.changeClientStatusUseCase = changeClientStatusUseCase;
                this.updateClientUseCase = updateClientUseCase;
                this.deleteClientUseCase = deleteClientUseCase;
    }

    @Operation(
            summary = "Registar novo cliente",
            description = "Cria um cliente com nome completo, documento e contacto"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponse register(
            @RequestBody @Valid RegisterClientRequest request
    ) {
        Client client = registerClientUseCase.execute(
                request.fullName(),
                request.documentType(),
                request.documentNumber(),
                request.phoneNumber(),
                request.bairro(),
                request.localidade(),
                request.rua(),
                request.referencia()
        );
        return ClientResponse.from(client);
    }

        @Operation(
                        summary = "Obter cliente por ID",
                        description = "Devolve os dados de um cliente pelo seu ID"
        )
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
                        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        })
        @GetMapping("/{id}")
        @PreAuthorize("hasAnyRole('ADMIN','USER')")
        public ClientResponse getById(@PathVariable String id) {
                Client client = getClientByIdUseCase.execute(id);
                return ClientResponse.from(client);
        }


        @Operation(
                        summary = "Listar clientes",
                        description = "Lista clientes com filtros opcionais por status (ACTIVE/INACTIVE) e zona (bairro)"
        )
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lista de clientes")
        })
            @GetMapping
            @PreAuthorize("hasAnyRole('ADMIN','USER')")
            public PageResponse<ClientResponse> list(
                                @RequestParam(name = "status", required = false) ClientStatus status,
                                @RequestParam(name = "zone", required = false) String bairro,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size", defaultValue = "20") int size
                ) {
                        java.util.List<Client> all = searchClientsUseCase.execute(status, bairro);

                        if (size <= 0) {
                                size = 20;
                        }
                        if (page < 0) {
                                page = 0;
                        }

                        long totalElements = all.size();
                        int totalPages = totalElements == 0 ? 0 : (int) Math.ceil((double) totalElements / size);

                        int fromIndex = page * size;
                        if (fromIndex >= all.size()) {
                                return new PageResponse<>(java.util.List.of(), page, size, totalElements, totalPages);
                        }
                        int toIndex = Math.min(fromIndex + size, all.size());

                        java.util.List<ClientResponse> content = all.subList(fromIndex, toIndex)
                                        .stream()
                                        .map(ClientResponse::from)
                                        .toList();

                        return new PageResponse<>(content, page, size, totalElements, totalPages);
                }


        @Operation(
                        summary = "Actualizar endereço do cliente",
                        description = "Regista ou altera o endereço (bairro/localidade/rua/referencia) de um cliente"
        )
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Endereço actualizado"),
                        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        })
        @PutMapping("/{id}/address")
        @PreAuthorize("hasRole('ADMIN')")
        public ClientResponse updateAddress(@PathVariable("id") java.util.UUID id,
                                                                                @RequestBody @Valid UpdateClientAddressRequest request) {
                Client client = updateClientAddressUseCase.execute(
                                id,
                                request.bairro(),
                                request.localidade(),
                                request.rua(),
                                request.referencia()
                );

                return ClientResponse.from(client);
        }

        @Operation(
                        summary = "Resumo de dívida do cliente",
                        description = "Mostra o total em dívida, valor do mês actual e valor vencido de um cliente."
        )
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Resumo calculado com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        })
        @GetMapping("/{id}/debt")
        @PreAuthorize("hasAnyRole('ADMIN','USER')")
        public ClientDebtSummaryResponse getDebt(@PathVariable("id") java.util.UUID id) {
                GetClientDebtSummaryUseCase.Result result = getClientDebtSummaryUseCase.execute(id);

                        var amountToPayNow = result.currentMonthAmount().add(result.overdueAmount());
                        var remainingBalance = result.totalOutstanding().subtract(amountToPayNow);
                        if (remainingBalance.signum() < 0) {
                                remainingBalance = java.math.BigDecimal.ZERO;
                        }

                return new ClientDebtSummaryResponse(
                                id,
                                result.totalOutstanding(),
                                                result.currentMonthAmount(),
                                                result.overdueAmount(),
                                                amountToPayNow,
                                                remainingBalance
                );
        }

        @Operation(
                        summary = "Alterar status do cliente",
                        description = "Activa ou desactiva um cliente (por exemplo, para cortar/reativar água)."
        )
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Status do cliente actualizado"),
                        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        })
        @PostMapping("/{id}/status")
        @PreAuthorize("hasRole('ADMIN')")
        public ClientResponse changeStatus(@PathVariable("id") java.util.UUID id,
                                                                                @RequestParam("status") ClientStatus status) {
                Client client = changeClientStatusUseCase.execute(id, status);
                return ClientResponse.from(client);
        }

        @Operation(
                summary = "Actualizar dados do cliente",
                description = "Actualiza o nome e número de telefone do cliente."
        )
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Cliente actualizado com sucesso"),
                @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        })
        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ClientResponse update(@PathVariable("id") java.util.UUID id,
                                   @RequestBody @Valid UpdateClientRequest request) {
            Client client = updateClientUseCase.execute(
                    id,
                    request.fullName(),
                    request.phoneNumber()
            );
            return ClientResponse.from(client);
        }

        @Operation(
                summary = "Eliminar cliente",
                description = "Remove o cliente do sistema permanentemente."
        )
        @ApiResponses({
                @ApiResponse(responseCode = "204", description = "Cliente eliminado com sucesso"),
                @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        })
        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void delete(@PathVariable("id") java.util.UUID id) {
            deleteClientUseCase.execute(id);
        }
}

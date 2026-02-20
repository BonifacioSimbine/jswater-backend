package com.kivora.JsWater.infrastructure.web.controller.meter;

import com.kivora.JsWater.application.usecase.meter.GetMeterByIdUseCase;
import com.kivora.JsWater.application.usecase.meter.RegisterMeterUseCase;
import com.kivora.JsWater.application.usecase.meter.ListMetersUseCase;
import com.kivora.JsWater.domain.model.meter.Meter;
import com.kivora.JsWater.domain.valueobject.client.ClientId;
import com.kivora.JsWater.domain.repository.ClientRepository;
import com.kivora.JsWater.infrastructure.web.dto.meter.MeterResponse;
import com.kivora.JsWater.infrastructure.web.dto.meter.RegisterMeterRequest;
import com.kivora.JsWater.infrastructure.web.dto.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meters")
@Tag(name = "Meters", description = "Gestão de contadores de água")
public class MeterController {

    private final RegisterMeterUseCase registerMeterUseCase;
    private final GetMeterByIdUseCase getMeterByIdUseCase;
    private final ListMetersUseCase listMetersUseCase;
    private final ClientRepository clientRepository; // Injetei o repositório para buscar nomes

    public MeterController(RegisterMeterUseCase registerMeterUseCase,
                           GetMeterByIdUseCase getMeterByIdUseCase,
                           ListMetersUseCase listMetersUseCase,
                           ClientRepository clientRepository) {
        this.registerMeterUseCase = registerMeterUseCase;
        this.getMeterByIdUseCase = getMeterByIdUseCase;
        this.listMetersUseCase = listMetersUseCase;
        this.clientRepository = clientRepository;
    }

    @Operation(
            summary = "Registar contador",
            description = "Associa um contador a um cliente existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contador criado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public MeterResponse register(@RequestBody @Valid RegisterMeterRequest request) {
        Meter meter = registerMeterUseCase.execute(
                request.meterNumber(),
                new ClientId(request.clientId()),
                request.installationDate()
        );
        
        // Busca o nome do cliente recém-associado
        String clientName = clientRepository.findById(meter.getClientId())
                .map(c -> c.getFullName().getValue())
                .orElse("Desconhecido");

        return MeterResponse.from(meter, clientName);
    }

    @Operation(
            summary = "Obter contador por ID",
            description = "Devolve os dados de um contador pelo seu ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contador encontrado"),
            @ApiResponse(responseCode = "404", description = "Contador não encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public MeterResponse getById(@PathVariable String id) {
        Meter meter = getMeterByIdUseCase.execute(id);
        
        String clientName = clientRepository.findById(meter.getClientId())
                .map(c -> c.getFullName().getValue())
                .orElse("Desconhecido");

        return MeterResponse.from(meter, clientName);
    }

    @Operation(
            summary = "Listar contadores",
            description = "Lista todos os contadores registados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de contadores")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public PageResponse<MeterResponse> list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        java.util.List<Meter> all = listMetersUseCase.execute();

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

        java.util.List<MeterResponse> content = all.subList(fromIndex, toIndex)
                .stream()
                .map(meter -> {
                    String clientName = clientRepository.findById(meter.getClientId())
                            .map(c -> c.getFullName().getValue())
                            .orElse("Desconhecido");
                    return MeterResponse.from(meter, clientName);
                })
                .toList();

        return new PageResponse<>(content, page, size, totalElements, totalPages);
    }
}

package com.kivora.JsWater.infrastructure.web.controller.reading;

import com.kivora.JsWater.application.usecase.reading.GetReadingByIdUseCase;
import com.kivora.JsWater.application.usecase.reading.RegisterReadingUseCase;
import com.kivora.JsWater.application.usecase.reading.ListReadingsUseCase;
import com.kivora.JsWater.application.usecase.reading.DeleteReadingUseCase;
import com.kivora.JsWater.domain.exception.FieldException;
import com.kivora.JsWater.domain.model.reading.Reading;
import com.kivora.JsWater.domain.valueobject.meter.MeterId;
import com.kivora.JsWater.domain.valueobject.reading.ReadingValue;
import com.kivora.JsWater.infrastructure.web.dto.reading.ReadingResponse;
import com.kivora.JsWater.infrastructure.web.dto.reading.RegisterReadingRequest;
import com.kivora.JsWater.infrastructure.web.dto.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/readings")
@Tag(name = "Readings", description = "Leituras de consumo de água")
public class ReadingController {

    private final RegisterReadingUseCase registerReadingUseCase;
    private final GetReadingByIdUseCase getReadingByIdUseCase;
    private final ListReadingsUseCase listReadingsUseCase;
        private final DeleteReadingUseCase deleteReadingUseCase;

    public ReadingController(RegisterReadingUseCase registerReadingUseCase,
                                                         GetReadingByIdUseCase getReadingByIdUseCase,
                                                         ListReadingsUseCase listReadingsUseCase,
                                                         DeleteReadingUseCase deleteReadingUseCase) {
        this.registerReadingUseCase = registerReadingUseCase;
        this.getReadingByIdUseCase = getReadingByIdUseCase;
        this.listReadingsUseCase = listReadingsUseCase;
                this.deleteReadingUseCase = deleteReadingUseCase;
    }

    @Operation(
            summary = "Registar Leitura",
            description = "Regista a leitura actual de um contador"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Leitura registada"),
            @ApiResponse(responseCode = "409", description = "Leitura já existe para o período")
    })

    @PostMapping("register")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ReadingResponse register(@RequestBody @Valid RegisterReadingRequest registerReadingRequest) {
        try{
        Reading reading = registerReadingUseCase.execute(
                new MeterId(registerReadingRequest.meterId()),
                new ReadingValue(registerReadingRequest.currentReading())
        );

        return ReadingResponse.from(reading);
      }catch (FieldException ex) {
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                ex.getMessage()
        );
      }
    }

    @Operation(
            summary = "Obter leitura por ID",
            description = "Devolve os dados de uma leitura pelo seu ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Leitura encontrada"),
            @ApiResponse(responseCode = "404", description = "Leitura não encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ReadingResponse getById(@PathVariable String id) {
        Reading reading = getReadingByIdUseCase.execute(id);
        return ReadingResponse.from(reading);
    }

    @Operation(
            summary = "Listar leituras",
            description = "Lista todos as leituras registadas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de leituras")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public PageResponse<ReadingResponse> list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        java.util.List<Reading> all = listReadingsUseCase.execute();

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

        java.util.List<ReadingResponse> content = all.subList(fromIndex, toIndex)
                .stream()
                .map(ReadingResponse::from)
                .toList();

        return new PageResponse<>(content, page, size, totalElements, totalPages);
    }

        @Operation(
                        summary = "Eliminar leitura",
                        description = "Remove a leitura do sistema permanentemente."
        )
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Leitura eliminada com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Leitura não encontrada")
        })
        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void delete(@PathVariable("id") java.util.UUID id) {
                deleteReadingUseCase.execute(id);
        }
}

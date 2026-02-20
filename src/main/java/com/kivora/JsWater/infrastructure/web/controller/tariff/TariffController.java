package com.kivora.JsWater.infrastructure.web.controller.tariff;

import com.kivora.JsWater.application.usecase.tariff.GetActiveTariffForPeriodUseCase;
import com.kivora.JsWater.application.usecase.tariff.RegisterTariffUseCase;
import com.kivora.JsWater.application.usecase.tariff.ListTariffsUseCase;
import com.kivora.JsWater.application.usecase.tariff.DeactivateTariffUseCase;
import com.kivora.JsWater.domain.model.tariff.Tariff;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;
import com.kivora.JsWater.infrastructure.web.dto.tariff.RegisterTariffRequest;
import com.kivora.JsWater.infrastructure.web.dto.tariff.TariffResponse;
import com.kivora.JsWater.infrastructure.web.dto.tariff.DeactivateTariffRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/tariffs")
@Tag(name = "Tariffs", description = "Gestão de tarifas de água")
public class TariffController {

    private final RegisterTariffUseCase registerTariffUseCase;
    private final GetActiveTariffForPeriodUseCase getActiveTariffForPeriodUseCase;
        private final ListTariffsUseCase listTariffsUseCase;
        private final DeactivateTariffUseCase deactivateTariffUseCase;

    public TariffController(RegisterTariffUseCase registerTariffUseCase,
                                                        GetActiveTariffForPeriodUseCase getActiveTariffForPeriodUseCase,
                                                        ListTariffsUseCase listTariffsUseCase,
                                                        DeactivateTariffUseCase deactivateTariffUseCase) {
        this.registerTariffUseCase = registerTariffUseCase;
        this.getActiveTariffForPeriodUseCase = getActiveTariffForPeriodUseCase;
                this.listTariffsUseCase = listTariffsUseCase;
                this.deactivateTariffUseCase = deactivateTariffUseCase;
    }

    @Operation(
            summary = "Registar tarifa",
            description = "Cria uma nova tarifa activa a partir de uma data de início"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarifa criada")
    })
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public TariffResponse register(@RequestBody @Valid RegisterTariffRequest request) {
        Tariff tariff = registerTariffUseCase.execute(
                request.validFrom(),
                request.pricePerCubicMeter()
        );

        return TariffResponse.from(tariff);
    }

    @Operation(
            summary = "Obter tarifa activa para um período",
            description = "Devolve a tarifa activa para o período indicado (formato yyyy-MM). Se não for enviado, usa o período actual."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarifa encontrada"),
            @ApiResponse(responseCode = "404", description = "Nenhuma tarifa activa encontrada para o período")
    })
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public TariffResponse getActiveForPeriod(@RequestParam(name = "period", required = false) String period) {
        InvoicePeriod invoicePeriod = (period == null || period.isBlank())
                ? InvoicePeriod.current()
                : InvoicePeriod.of(YearMonth.parse(period));

        Tariff tariff = getActiveTariffForPeriodUseCase.execute(invoicePeriod);

        return TariffResponse.from(tariff);
    }

    @Operation(
            summary = "Listar tarifas",
            description = "Lista todas as tarifas registadas (activas e inactivas)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de tarifas")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public java.util.List<TariffResponse> listAll() {
        return listTariffsUseCase.execute().stream()
                .map(TariffResponse::from)
                .toList();
    }

    @Operation(
            summary = "Desactivar tarifa",
            description = "Encerra uma tarifa a partir de um período final (yyyy-MM), marcando-a como INACTIVE."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarifa desactivada"),
            @ApiResponse(responseCode = "404", description = "Tarifa não encontrada"),
            @ApiResponse(responseCode = "400", description = "Período inválido ou tarifa já inactiva")
    })
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public TariffResponse deactivate(
            @PathVariable("id") java.util.UUID id,
            @RequestBody @Valid DeactivateTariffRequest request
    ) {
        Tariff tariff = deactivateTariffUseCase.execute(id, request.endPeriod());
        return TariffResponse.from(tariff);
    }
}

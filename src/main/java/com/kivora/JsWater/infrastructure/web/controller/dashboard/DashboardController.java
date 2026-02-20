package com.kivora.JsWater.infrastructure.web.controller.dashboard;

import com.kivora.JsWater.application.usecase.invoice.GetDashboardOverviewUseCase;
import com.kivora.JsWater.infrastructure.web.dto.dashboard.DashboardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Resumo geral do sistema")
public class DashboardController {

    private final GetDashboardOverviewUseCase getDashboardOverviewUseCase;

    public DashboardController(GetDashboardOverviewUseCase getDashboardOverviewUseCase) {
        this.getDashboardOverviewUseCase = getDashboardOverviewUseCase;
    }

    @Operation(
            summary = "Dashboard geral",
            description = "Devolve, numa só chamada, o resumo mensal de facturação, top devedores, dívida por zona e KPIs gerais."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dashboard carregado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public DashboardResponse getDashboard(
            @RequestParam("month") String month,
            @RequestParam(name = "topLimit", defaultValue = "10") int topLimit
    ) {
        YearMonth ym = YearMonth.parse(month);
        GetDashboardOverviewUseCase.Result result = getDashboardOverviewUseCase.execute(ym, topLimit);
        return DashboardResponse.from(result);
    }
}

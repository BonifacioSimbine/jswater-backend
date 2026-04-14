package com.kivora.JsWater.infrastructure.config;

import com.kivora.JsWater.application.usecase.client.*;
import com.kivora.JsWater.application.usecase.expense.*;
import com.kivora.JsWater.application.usecase.invoice.*;
import com.kivora.JsWater.application.usecase.meter.*;
import com.kivora.JsWater.application.usecase.reading.*;
import com.kivora.JsWater.application.usecase.user.*;
import com.kivora.JsWater.application.usecase.tariff.*;
import com.kivora.JsWater.application.usecase.notification.*;
import com.kivora.JsWater.domain.repository.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UseCaseConfig {



    @Bean
    public RegisterExpenseUseCase registerExpenseUseCase(
            ExpenseRepository expenseRepository
    ) {
        return new RegisterExpenseUseCase(expenseRepository);
    }

    @Bean
    public ListExpensesUseCase listExpensesUseCase(
            ExpenseRepository expenseRepository
    ) {
        return new ListExpensesUseCase(expenseRepository);
    }

    // ========================= USER =========================

    @Bean
    public ListUsersUseCase listUsersUseCase(UserRepository userRepository) {
        return new ListUsersUseCase(userRepository);
    }

    @Bean
    public AdminResetUserPasswordUseCase adminResetUserPasswordUseCase(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return new AdminResetUserPasswordUseCase(userRepository, passwordEncoder);
    }

    @Bean
    public ChangeUserStatusUseCase changeUserStatusUseCase(
            UserRepository userRepository
    ) {
        return new ChangeUserStatusUseCase(userRepository);
    }

    // ========================= CLIENT =========================

    @Bean
    public RegisterClientUseCase registerClientUseCase(ClientRepository clientRepository) {
        return new RegisterClientUseCase(clientRepository);
    }

    @Bean
    public GetClientByIdUseCase getClientByIdUseCase(ClientRepository clientRepository) {
        return new GetClientByIdUseCase(clientRepository);
    }

    @Bean
    public SearchClientsUseCase searchClientsUseCase(ClientRepository clientRepository) {
        return new SearchClientsUseCase(clientRepository);
    }

    @Bean
    public UpdateClientAddressUseCase updateClientAddressUseCase(ClientRepository clientRepository) {
        return new UpdateClientAddressUseCase(clientRepository);
    }

    @Bean
    public ChangeClientStatusUseCase changeClientStatusUseCase(ClientRepository clientRepository) {
        return new ChangeClientStatusUseCase(clientRepository);
    }

    @Bean
    public UpdateClientUseCase updateClientUseCase(ClientRepository clientRepository) {
        return new UpdateClientUseCase(clientRepository);
    }

    @Bean
    public DeleteClientUseCase deleteClientUseCase(ClientRepository clientRepository) {
        return new DeleteClientUseCase(clientRepository);
    }

    // ========================= METER =========================

    @Bean
    public RegisterMeterUseCase registerMeterUseCase(
            MeterRepository meterRepository,
            ClientRepository clientRepository
    ) {
        return new RegisterMeterUseCase(meterRepository, clientRepository);
    }

    @Bean
    public GetMeterByIdUseCase getMeterByIdUseCase(MeterRepository meterRepository) {
        return new GetMeterByIdUseCase(meterRepository);
    }

    @Bean
    public ListMetersUseCase listMetersUseCase(MeterRepository meterRepository) {
        return new ListMetersUseCase(meterRepository);
    }

    // ========================= READING =========================

    @Bean
    public RegisterReadingUseCase registerReadingUseCase(
            MeterRepository meterRepository,
            ReadingRepository readingRepository,
            ClientRepository clientRepository
    ) {
        return new RegisterReadingUseCase(meterRepository, readingRepository, clientRepository);
    }

    @Bean
    public ListReadingsUseCase listReadingsUseCase(ReadingRepository readingRepository) {
        return new ListReadingsUseCase(readingRepository);
    }

    @Bean
    public GetReadingByIdUseCase getReadingByIdUseCase(ReadingRepository readingRepository) {
        return new GetReadingByIdUseCase(readingRepository);
    }

    @Bean
    public DeleteReadingUseCase deleteReadingUseCase(ReadingRepository readingRepository) {
        return new DeleteReadingUseCase(readingRepository);
    }

    // ========================= INVOICE =========================

    @Bean
    public GenerateInvoiceUseCase generateInvoiceUseCase(
            ReadingRepository readingRepository,
            MeterRepository meterRepository,
            ClientRepository clientRepository,
            TariffRepository tariffRepository,
            InvoiceRepository invoiceRepository
    ) {
        return new GenerateInvoiceUseCase(
                readingRepository,
                meterRepository,
                clientRepository,
                tariffRepository,
                invoiceRepository
        );
    }

    @Bean
    public ListInvoicesUseCase listInvoicesUseCase(InvoiceRepository invoiceRepository) {
        return new ListInvoicesUseCase(invoiceRepository);
    }

    @Bean
    public GetInvoiceByIdUseCase getInvoiceByIdUseCase(InvoiceRepository invoiceRepository) {
        return new GetInvoiceByIdUseCase(invoiceRepository);
    }

    @Bean
    public GetClientStatementUseCase getClientStatementUseCase(
            InvoiceRepository invoiceRepository
    ) {
        return new GetClientStatementUseCase(invoiceRepository);
    }

    @Bean
    public GetClientDebtSummaryUseCase getClientDebtSummaryUseCase(
            InvoiceRepository invoiceRepository
    ) {
        return new GetClientDebtSummaryUseCase(invoiceRepository);
    }

    @Bean
    public GetClientPendingInvoicesUseCase getClientPendingInvoicesUseCase(
            InvoiceRepository invoiceRepository
    ) {
        return new GetClientPendingInvoicesUseCase(invoiceRepository);
    }

    @Bean
    public GetMonthlyBillingReportUseCase getMonthlyBillingReportUseCase(
            InvoiceRepository invoiceRepository
    ) {
        return new GetMonthlyBillingReportUseCase(invoiceRepository);
    }

    @Bean
    public GetTopDebtorsReportUseCase getTopDebtorsReportUseCase(
            InvoiceRepository invoiceRepository,
            ClientRepository clientRepository
    ) {
        return new GetTopDebtorsReportUseCase(invoiceRepository, clientRepository);
    }

    @Bean
    public GetZoneDebtReportUseCase getZoneDebtReportUseCase(
            InvoiceRepository invoiceRepository,
            ClientRepository clientRepository
    ) {
        return new GetZoneDebtReportUseCase(invoiceRepository, clientRepository);
    }

    @Bean
    public GetBillingDetailReportUseCase getBillingDetailReportUseCase(
            InvoiceRepository invoiceRepository,
            ClientRepository clientRepository,
            ReadingRepository readingRepository,
            GetClientDebtSummaryUseCase getClientDebtSummaryUseCase,
            GetActiveTariffForPeriodUseCase getActiveTariffForPeriodUseCase
    ) {
        return new GetBillingDetailReportUseCase(
                invoiceRepository,
                clientRepository,
                readingRepository,
                getClientDebtSummaryUseCase,
                getActiveTariffForPeriodUseCase
        );
    }

    @Bean
    public GetDashboardOverviewUseCase getDashboardOverviewUseCase(
            GetMonthlyBillingReportUseCase getMonthlyBillingReportUseCase,
            GetTopDebtorsReportUseCase getTopDebtorsReportUseCase,
            GetZoneDebtReportUseCase getZoneDebtReportUseCase,
            ClientRepository clientRepository,
            InvoiceRepository invoiceRepository,
            MeterRepository meterRepository
    ) {
        return new GetDashboardOverviewUseCase(
                getMonthlyBillingReportUseCase,
                getTopDebtorsReportUseCase,
                getZoneDebtReportUseCase,
                clientRepository,
                invoiceRepository,
                meterRepository
        );
    }

    @Bean
    public RegisterFineUseCase registerFineUseCase(
            ClientRepository clientRepository,
            InvoiceRepository invoiceRepository
    ) {
        return new RegisterFineUseCase(clientRepository, invoiceRepository);
    }

    @Bean
    public PayInvoiceUseCase payInvoiceUseCase(
            InvoiceRepository invoiceRepository
    ) {
        return new PayInvoiceUseCase(invoiceRepository);
    }

    @Bean
    public GetFinancialReportUseCase getFinancialReportUseCase(
            InvoiceRepository invoiceRepository,
            ClientRepository clientRepository,
            ExpenseRepository expenseRepository
    ) {
        return new GetFinancialReportUseCase(
                invoiceRepository,
                clientRepository,
                expenseRepository
        );
    }

    // ========================= TARIFF =========================

    @Bean
    public RegisterTariffUseCase registerTariffUseCase(TariffRepository tariffRepository) {
        return new RegisterTariffUseCase(tariffRepository);
    }

    @Bean
    public GetActiveTariffForPeriodUseCase getActiveTariffForPeriodUseCase(
            TariffRepository tariffRepository
    ) {
        return new GetActiveTariffForPeriodUseCase(tariffRepository);
    }

    @Bean
    public ListTariffsUseCase listTariffsUseCase(
            TariffRepository tariffRepository
    ) {
        return new ListTariffsUseCase(tariffRepository);
    }

    @Bean
    public DeactivateTariffUseCase deactivateTariffUseCase(
            TariffRepository tariffRepository
    ) {
        return new DeactivateTariffUseCase(tariffRepository);
    }

    // ========================= NOTIFICATION =========================

    @Bean
    public ListNotificationsUseCase listNotificationsUseCase(
            NotificationRepository notificationRepository
    ) {
        return new ListNotificationsUseCase(notificationRepository);
    }

    @Bean
    public GetUnreadNotificationCountUseCase getUnreadNotificationCountUseCase(
            NotificationRepository notificationRepository
    ) {
        return new GetUnreadNotificationCountUseCase(notificationRepository);
    }

    @Bean
    public MarkNotificationAsReadUseCase markNotificationAsReadUseCase(
            NotificationRepository notificationRepository
    ) {
        return new MarkNotificationAsReadUseCase(notificationRepository);
    }

    @Bean
    public MarkAllNotificationsAsReadUseCase markAllNotificationsAsReadUseCase(
            NotificationRepository notificationRepository
    ) {
        return new MarkAllNotificationsAsReadUseCase(notificationRepository);
    }

    @Bean
    public CreateNotificationUseCase createNotificationUseCase(
            NotificationRepository notificationRepository
    ) {
        return new CreateNotificationUseCase(notificationRepository);
    }
}

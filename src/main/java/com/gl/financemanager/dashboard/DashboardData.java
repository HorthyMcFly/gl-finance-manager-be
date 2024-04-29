package com.gl.financemanager.dashboard;

import com.gl.financemanager.balance.BalanceDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardData {

    private BalanceDto balance;

    private IncomeExpenseSummary incomeExpenseSummary;

    private AssetSummary assetSummary;

    private LoanSummary loanSummary;

}

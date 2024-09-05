package com.gl.financemanager.dashboard;

import com.gl.financemanager.asset.AssetService;
import com.gl.financemanager.balance.BalanceService;
import com.gl.financemanager.expense.service.ExpenseService;
import com.gl.financemanager.income.IncomeService;
import com.gl.financemanager.loan.LoanService;
import com.gl.financemanager.period.PeriodService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class DashboardService {

  private BalanceService balanceService;
  private PeriodService periodService;
  private IncomeService incomeService;
  private ExpenseService expenseService;
  private AssetService assetService;
  private LoanService loanService;

  public DashboardData getDashboardDataForLoggedInUser() {
    var balance = balanceService.getBalanceForLoggedInUser();
    var activePeriod = periodService.getActivePeriod();
    if (activePeriod == null) {
      return null;
    }

    var totalIncome = incomeService.getIncomesForLoggedInUserByPeriodId(activePeriod.getId())
        .stream()
        .reduce(
            new BigDecimal(0),
            (acc, current) -> acc.add(current.getAmount()), BigDecimal::add);
    var totalExpense = expenseService.getExpensesForLoggedInUserByPeriodId(activePeriod.getId())
        .stream()
        .reduce(
            new BigDecimal(0),
            (acc, current) -> acc.add(current.getAmount()), BigDecimal::add);
    var incomeExpenseSummary = new IncomeExpenseSummary(totalIncome, totalExpense);

    var totalAssetValue = assetService.getAssetsForLoggedInUser()
        .stream()
        .reduce(new BigDecimal(0),
            (acc, current) -> acc.add(current.getAmount()), BigDecimal::add);
    var assetSummary = new AssetSummary(totalAssetValue);

    var totalLoanValue = loanService.getLoansForLoggedInUser()
        .stream()
        .reduce(new BigDecimal(0),
            (acc, current) -> acc.add(current.getAmount()), BigDecimal::add);
    var loanSummary = new LoanSummary(totalLoanValue);

    return new DashboardData(balance, incomeExpenseSummary, assetSummary, loanSummary);
  }

}

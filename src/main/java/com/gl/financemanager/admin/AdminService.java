package com.gl.financemanager.admin;

import com.gl.financemanager.asset.AssetRepository;
import com.gl.financemanager.auth.FmUser;
import com.gl.financemanager.auth.UserRepository;
import com.gl.financemanager.balance.BalanceRepository;
import com.gl.financemanager.expense.Expense;
import com.gl.financemanager.expense.ExpenseCategoryRepository;
import com.gl.financemanager.expense.ExpenseRepository;
import com.gl.financemanager.income.Income;
import com.gl.financemanager.income.IncomeRepository;
import com.gl.financemanager.loan.LoanRepository;
import com.gl.financemanager.period.FmPeriod;
import com.gl.financemanager.period.PeriodRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserRepository userRespository;
    private final PeriodRepository periodRepository;
    private final AssetRepository assetRepository;
    private final IncomeRepository incomeRepository;
    private final BalanceRepository balanceRepository;
    private final LoanRepository loanRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;

    private final PasswordEncoder passwordEncoder;


    public List<FmUser> getUsers() {
        return userRespository.findAll();
    }

    public FmUser createUser(FmUser newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getUsername()));
        return userRespository.saveAndFlush(newUser);
    }

    public FmUser modifyUser(FmUser modifiedUser) {
        if (modifiedUser.getPassword().equals("reset")) {
            modifiedUser.setPassword(passwordEncoder.encode(modifiedUser.getUsername()));
        } else {
          var existingUser = this.userRespository.findById(modifiedUser.getId());
          existingUser.ifPresent(fmUser -> modifiedUser.setPassword(fmUser.getPassword()));
        }
        return userRespository.saveAndFlush(modifiedUser);
    }

    @Transactional
    public void closeActivePeriod() {
        var activePeriod = periodRepository.findByActive(true);
        if (activePeriod == null) {
            throw new RuntimeException();
        }
        var monthIndex = activePeriod.getStartDate().getMonth().getValue() - 1;
        var interestPayingAssets = assetRepository.findAllByInterestPaymentMonth(monthIndex);
        // all new incomes to be added to the period being closed
        var interestIncomes = interestPayingAssets.stream()
            .map(interestPayingAsset -> Income.builder()
                .fmUser(interestPayingAsset.getFmUser())
                .fmPeriod(activePeriod)
                .amount(interestPayingAsset.getAmount()
                    .multiply(interestPayingAsset.getInterestRate())
                    .scaleByPowerOfTen(-2))
                .source(interestPayingAsset.getName())
                .comment("Kamat")
                .build())
            .toList();
        incomeRepository.saveAll(interestIncomes);

        // the amount values of these assets need to be converted to investment balance,
        // and the Asset entities themselves need to be deleted
        var matureAssets = assetRepository
            .findAllByMaturityDateBetween(activePeriod.getStartDate(),
                activePeriod.getEndDate());
        var userInvestmentBalanceIncrementMap = new HashMap<Integer, BigDecimal>();
        matureAssets.forEach(matureAsset -> {
            var userId = matureAsset.getFmUser().getId();
            var incrementAmount = matureAsset.getAssetType().getType().equals("BOND") ?
                matureAsset.getAmount() :
                matureAsset.getAmount().add(
                    matureAsset.getAmount()
                        .multiply(matureAsset.getInterestRate().scaleByPowerOfTen(-2))
                );
          userInvestmentBalanceIncrementMap
              .merge(userId, incrementAmount, BigDecimal::add);
        });
        // balance entities updated for all users with mature assets
        var modifiedUserBalances = userInvestmentBalanceIncrementMap.entrySet().stream()
            .map((entry) -> {
                var userBalance = balanceRepository.findByFmUserId(entry.getKey());
                userBalance.setInvestmentBalance(userBalance.getInvestmentBalance().add(entry.getValue()));
                return userBalance;
            })
            .toList();
        assetRepository.deleteAll(matureAssets);
        balanceRepository.saveAll(modifiedUserBalances);

        var newStartDate = activePeriod.getStartDate().plusMonths(1)
            .with(TemporalAdjusters.firstDayOfMonth());
        var newPeriod = FmPeriod.builder()
            .active(true)
            .name(newStartDate.toString().substring(0, 7))
            .startDate(newStartDate)
            .endDate(activePeriod.getEndDate().plusMonths(1)
                .with(TemporalAdjusters.lastDayOfMonth()))
            .build();

        // saved new active period required for loan operations
        activePeriod.setActive(false);
        periodRepository.save(newPeriod);

        var allLoans = loanRepository.findAll();
        var fixExpenseCategoryOpt = expenseCategoryRepository.findByCategory("FIX");
        assert fixExpenseCategoryOpt.isPresent();
        var fixExpenseCategory = fixExpenseCategoryOpt.get();
        var newMonthlyRepaymentExpenses = allLoans.stream()
            .map(loan -> {
                BigDecimal expenseAmount;
                // if loan is expired
                if (loan.getMonthlyRepayment().compareTo(loan.getAmount()) >= 0) {
                    expenseAmount = loan.getAmount();
                    loan.setAmount(BigDecimal.ZERO);
                } else {
                    expenseAmount = loan.getMonthlyRepayment();
                    var loanAmountAfterMonthlyRepayment = loan.getAmount().subtract(expenseAmount);
                    loan.setAmount(loanAmountAfterMonthlyRepayment
                        .add(loanAmountAfterMonthlyRepayment
                            .multiply(loan.getInterestRate()).scaleByPowerOfTen(-2)
                        )
                    );
                }
                return Expense.builder()
                    .fmUser(loan.getFmUser())
                    .fmPeriod(newPeriod)
                    .amount(expenseAmount)
                    .recipient("Hiteltörlesztő")
                    .loan(loan)
                    .expenseCategory(fixExpenseCategory)
                    .comment(loan.getName())
                    .build();
            })
            .toList();

        expenseRepository.saveAll(newMonthlyRepaymentExpenses);
        newMonthlyRepaymentExpenses.forEach(newMonthlyRepaymentExpense -> {
            var userBalance = balanceRepository
                .findByFmUserId(newMonthlyRepaymentExpense.getFmUser().getId());
            userBalance.setBalance(userBalance.getBalance()
                .subtract(newMonthlyRepaymentExpense.getAmount()));
        });
        allLoans.forEach(loan -> {
            if (loan.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                loanRepository.delete(loan);
            } else {
                loanRepository.save(loan);
            }
        });

    }
}

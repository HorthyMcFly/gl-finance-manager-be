package com.gl.financemanager.admin;

import com.gl.financemanager.asset.AssetRepository;
import com.gl.financemanager.auth.FmUser;
import com.gl.financemanager.auth.UserRepository;
import com.gl.financemanager.balance.BalanceRepository;
import com.gl.financemanager.expense.entity.Expense;
import com.gl.financemanager.expense.repository.ExpenseCategoryRepository;
import com.gl.financemanager.expense.repository.ExpenseRepository;
import com.gl.financemanager.income.Income;
import com.gl.financemanager.income.IncomeRepository;
import com.gl.financemanager.loan.LoanRepository;
import com.gl.financemanager.period.FmPeriod;
import com.gl.financemanager.period.PeriodRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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


    public List<FmUserDto> getUsers() {
        return userRespository.findAll().stream().map(AdminService::fmUserToDto).toList();
    }

    @Transactional
    public FmUserDto createUser(FmUserDto fmUserDto) {
        if (fmUserDto.getId() != null) {
            throw new RuntimeException();
        }
        var existingUser = userRespository.findByUsername(fmUserDto.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException();
        }
        var newUser = AdminService.fmUserFromDto(fmUserDto);
        newUser.setPassword(passwordEncoder.encode(newUser.getUsername()));
        return AdminService.fmUserToDto(userRespository.save(newUser));
    }

    @Transactional
    public FmUserDto modifyUser(FmUserDto fmUserDto) {
        var existingUserOpt = this.userRespository.findById(fmUserDto.getId());
        var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        assert existingUserOpt.isPresent();
        var existingUser = existingUserOpt.get();

        // admins can't modify themselves
        if (loggedInUsername.equals(existingUser.getUsername())) {
            throw new RuntimeException();
        }

        if (fmUserDto.isResetPassword()) {
            existingUser.setPassword(passwordEncoder.encode(existingUser.getUsername()));
        }
        existingUser.setAdmin(fmUserDto.isAdmin());
        existingUser.setActive(fmUserDto.isActive());
        return AdminService.fmUserToDto(userRespository.save(existingUser));
    }

    @Transactional
    public void createFirstPeriod() {
        var existingPeriods = periodRepository.findAll();
        if (!existingPeriods.isEmpty()) {
            throw new RuntimeException();
        }
        var currentDate = LocalDate.now();
        var firstPeriod = FmPeriod.builder()
            .name(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM")))
            .startDate(currentDate.with(TemporalAdjusters.firstDayOfMonth()))
            .endDate(currentDate.with(TemporalAdjusters.lastDayOfMonth()))
            .active(true)
            .build();
        periodRepository.save(firstPeriod);
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
                .editable(false)
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
                            .multiply(loan.getInterestRate().divide(new BigDecimal(12), RoundingMode.UP)
                                .scaleByPowerOfTen(-2))
                        )
                    );
                }
                return Expense.builder()
                    .fmUser(loan.getFmUser())
                    .fmPeriod(newPeriod)
                    .amount(expenseAmount)
                    .recipient("Hitel törlesztő")
                    .loan(loan)
                    .expenseCategory(fixExpenseCategory)
                    .comment(loan.getName())
                    .editable(false)
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
                var loanExpenses = expenseRepository.findAllByLoanId(loan.getId());
                loanExpenses.forEach(loanExpense -> {
                    loanExpense.setLoan(null);
                    loanExpense.setComment("Hitel visszafizetve");
                });
                expenseRepository.saveAll(loanExpenses);
                loanRepository.delete(loan);
            } else {
                loanRepository.save(loan);
            }
        });

    }

    static FmUserDto fmUserToDto(FmUser fmUser) {
        return FmUserDto.builder()
            .id(fmUser.getId())
            .username(fmUser.getUsername())
            .admin(fmUser.isAdmin())
            .active(fmUser.isActive())
            .build();
    }

    static FmUser fmUserFromDto(FmUserDto fmUserDto) {
        return FmUser.builder()
            .id(fmUserDto.getId())
            .username(fmUserDto.getUsername())
            .admin(fmUserDto.isAdmin())
            .active(fmUserDto.isActive())
            .build();
    }
}

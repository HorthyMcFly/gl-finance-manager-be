INSERT INTO FM_USER (ID, USERNAME, PASSWORD, ADMIN, ACTIVE)
VALUES (1, 'testuser','$2a$10$ZFyxOSmXjMZhhWgg3azxFuX8D9tVESO/Z9J3JVwTRSdJqIo9vdAli', 0, 1);

INSERT INTO FM_USER (ID, USERNAME, PASSWORD, ADMIN, ACTIVE)
VALUES (2, 'testadmin','$2a$10$ZFyxOSmXjMZhhWgg3azxFuX8D9tVESO/Z9J3JVwTRSdJqIo9vdAli', 1, 1);

INSERT INTO FM_USER (ID, USERNAME, PASSWORD, ADMIN, ACTIVE)
VALUES (3, 'disableduser','$2a$10$ZFyxOSmXjMZhhWgg3azxFuX8D9tVESO/Z9J3JVwTRSdJqIo9vdAli', 0, 0);

INSERT INTO BALANCE (ID, FM_USER, BALANCE, INVESTMENT_BALANCE)
VALUES (1, 1, 1000000, 2000000);

INSERT INTO FM_PERIOD (ID, NAME, START_DATE, END_DATE, ACTIVE)
VALUES(1, '2024-03', '2024-03-01', '2024-03-31', 0);

INSERT INTO FM_PERIOD (ID, NAME, START_DATE, END_DATE, ACTIVE)
VALUES(2,'2024-04', '2024-04-01', '2024-04-30', 1);

INSERT INTO INCOME (ID, FM_USER, FM_PERIOD, AMOUNT, SOURCE, COMMENT)
VALUES(1, 1, 2, 100000, 'teszt forrás 1', 'első');

INSERT INTO INCOME (ID, FM_USER, FM_PERIOD, AMOUNT, SOURCE, COMMENT)
VALUES(2, 1, 2, 200000, 'teszt forrás 2', 'második');

INSERT INTO EXPENSE (ID, FM_USER, FM_PERIOD, LOAN, AMOUNT, RECIPIENT, EXPENSE_CATEGORY, COMMENT)
VALUES(1, 1, 2, null, 100000, 'teszt cél 1', 1, 'első');

INSERT INTO EXPENSE (ID, FM_USER, FM_PERIOD, LOAN, AMOUNT, RECIPIENT, EXPENSE_CATEGORY, COMMENT)
VALUES(2, 1, 2, null, 200000, 'teszt cél 2', 4, 'második');


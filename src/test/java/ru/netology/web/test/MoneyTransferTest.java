package ru.netology.web.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;

class MoneyTransferTest {

  DashboardPage dashboardPage;

  @BeforeEach
  void setUp() {
    open("http://localhost:9999/");
    var LoginPage = new LoginPage();
    var authInfo = DataHelper.getAuthInfo();
    var verificationPage = LoginPage.validLogin(authInfo);
    var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
    dashboardPage = verificationPage.validVerify(verificationCode);
  }

  @Test
  public void shouldTransferMoneyBetweenCards() {
    var firstCardInfo = getFirstCardInfo();
    var secondCardInfo = getSecondCardInfo();
    var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
    var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
    var amount = generateValidAmount(firstCardBalance);
    var expectedBalanceFirstCard = firstCardBalance - amount;
    var expectedBalanceSecondCard = secondCardBalance + amount;
    var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
    dashboardPage = transferPage.makeValidTransfer((String.valueOf(amount)), firstCardInfo);
    var currentBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
    var currentBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);
    assertEquals(expectedBalanceFirstCard, currentBalanceFirstCard);
    assertEquals(expectedBalanceSecondCard, currentBalanceSecondCard);
  }

  @Test
  public void shouldNotTransferWhenAmountIsOverLimit() {
    var firstCardInfo = getFirstCardInfo();
    var secondCardInfo = getSecondCardInfo();
    var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
    var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
    var amount = generateInvalidAmount(firstCardBalance);
    var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
    transferPage.transfer((String.valueOf(amount)), firstCardInfo);
    transferPage.findErrorMessage("На карте недостаточно средств для выполнения данной операции.");
    var currentBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
    var currentBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);
    assertEquals(firstCardBalance, currentBalanceFirstCard);
    assertEquals(secondCardBalance, currentBalanceSecondCard);
  }
}


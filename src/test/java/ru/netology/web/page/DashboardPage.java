package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
  private SelenideElement heading = $("[data-test-id=dashboard]");
  private ElementsCollection cards = $$(".list__item div");
  private final String balanceStart = "баланс: ";
  private final String balanceFinish = " р.";

  public DashboardPage() {
    heading.shouldBe(visible);
  }

  public int getCardBalance(DataHelper.CardInfo cardInfo) {
    var text = cards.findBy(attribute("data-test-id", cardInfo.getId())).getText();
    return extractBalance(text);
  }

  private int extractBalance(String text) {
    var start = text.indexOf(balanceStart);
    var finish = text.indexOf(balanceFinish);
    var value = text.substring(start + balanceStart.length(), finish);
    return Integer.parseInt(value);
  }

  public TransferPage selectCardToTransfer(DataHelper.CardInfo cardInfo) {
    cards.findBy(attribute("data-test-id", cardInfo.getId())).$("button").click();
    return new TransferPage();
  }
}

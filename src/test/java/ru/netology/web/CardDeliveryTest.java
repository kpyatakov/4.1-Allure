package ru.netology.web;


import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.data.DataGenerator.getUserInfo;


public class CardDeliveryTest {

    DataGenerator.UserInfo user = getUserInfo();

    @BeforeAll
    static void setUpAll() {SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {SelenideLogger.removeListener("allure");
    }

    @Test
    public void shouldTest() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;

        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        // String date = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=date] input").setValue(user.getMeetingDate());
        $("[data-test-id=name] input").setValue(user.getFullName());
        $("[data-test-id=phone] input").setValue(user.getMobilePhone());
        $("[data-test-id=agreement]").click();
        $x("//*[text()=\"Запланировать\"]").click();
        $(byClassName("notification__title")).should(ownText("Успешно!"));
        $(byClassName("notification__content")).should(ownText("Встреча успешно запланирована на " + user.getMeetingDate()), Duration.ofSeconds(15));

        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(user.getDateRescheduling());
        $x("//*[text()=\"Запланировать\"]").click();

        $("[data-test-id='replan-notification'] > .notification__title")
                .shouldHave(exactText("Необходимо подтверждение"));

        $("[data-test-id='replan-notification'] > .notification__content")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));

        $x("//*[text()=\"Перепланировать\"]").click();
        $(byClassName("notification__title")).should(ownText("Успешно!"));
        $(byClassName("notification__content")).should(ownText("Встреча успешно запланирована на " + user.getDateRescheduling()), Duration.ofSeconds(15));

    }

}
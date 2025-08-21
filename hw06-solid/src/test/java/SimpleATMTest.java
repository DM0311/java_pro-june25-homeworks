import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.java.pro.ATM;
import ru.otus.java.pro.Banknote;
import ru.otus.java.pro.SimpleATM;
import ru.otus.java.pro.exceptions.IncorrectSumException;
import ru.otus.java.pro.exceptions.NotEnoughMoneyException;

public class SimpleATMTest {

    private ATM atm = new SimpleATM(
            List.of(Banknote.RUB_100, Banknote.RUB_1000, Banknote.RUB_500, Banknote.RUB_100, Banknote.RUB_5000));

    @Test
    @DisplayName("Проверка корректности баланса")
    void getBalance() {
        long expectedBalance = 6700l;
        long actualBalance = atm.getBalance();
        assertThat(actualBalance).isEqualTo(expectedBalance);
    }

    @Test
    @DisplayName("Проверка корректности внесения средств")
    void deposite() {
        long expectedBalance = 13400l;
        atm.deposit(
                List.of(Banknote.RUB_100, Banknote.RUB_1000, Banknote.RUB_500, Banknote.RUB_100, Banknote.RUB_5000));
        long actualBalance = atm.getBalance();
        assertThat(actualBalance).isEqualTo(expectedBalance);
    }

    @Test
    @DisplayName("Передача отрицательной суммы приводит к ошибке")
    void withdrawNegativeSum() {
        Throwable throwable = Assertions.catchThrowable(() -> atm.withdraw(-1L));
        assertThat(throwable).isInstanceOf(IncorrectSumException.class);
    }

    @Test
    @DisplayName("Выдача суммы большей чем есть в терминале")
    void withdrawSumBiggerThenExists() {
        Throwable throwable = Assertions.catchThrowable(() -> atm.withdraw(20000L));
        assertThat(throwable).isInstanceOf(NotEnoughMoneyException.class);
    }

    @Test
    @DisplayName("Передача суммы выдачи не бьющейся по имеющимся банкнотам приводит к ошибке")
    void sumNotSuitableForExistBanknotes() {
        Throwable throwable = Assertions.catchThrowable(() -> atm.withdraw(4050));
        assertThat(throwable).isInstanceOf(NotEnoughMoneyException.class);
    }
}

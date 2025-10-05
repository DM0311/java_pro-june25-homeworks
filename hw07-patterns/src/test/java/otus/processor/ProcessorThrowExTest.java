package otus.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.homework.ProcessorThrowEx;

public class ProcessorThrowExTest {
    ProcessorThrowEx processor;
    Message message;

    @BeforeEach
    void setUp() {
        processor = new ProcessorThrowEx();
        ProcessorThrowEx processor = new ProcessorThrowEx();
        var id = 100L;
        var data = "33";
        var field13 = new ObjectForMessage();
        var field13Data = new ArrayList<String>();
        field13Data.add(data);
        field13.setData(field13Data);
        message = new Message.Builder(id).field10("field10").field13(field13).build();
    }

    private void withMockedDateTime(LocalDateTime dateTime, Runnable testCode) {
        try (MockedStatic<LocalDateTime> mockedDateTime = mockStatic(LocalDateTime.class)) {
            mockedDateTime.when(LocalDateTime::now).thenReturn(dateTime);
            testCode.run();
        }
    }

    @Test
    @DisplayName("Четная секунда")
    void shouldThrowExceptionWithEvenSecond() {
        LocalDateTime evenSecondTime = LocalDateTime.of(2025, 9, 10, 10, 10, 10);

        withMockedDateTime(evenSecondTime, () -> {
            assertThrows(RuntimeException.class, () -> processor.process(message));
        });
    }

    @Test
    @DisplayName("Нечетная секунда")
    void shouldNotThrowExceptionWithOddSecond() {
        LocalDateTime oddSecondTime = LocalDateTime.of(2025, 9, 10, 10, 10, 11);

        withMockedDateTime(oddSecondTime, () -> {
            Message result = processor.process(message);
            assertEquals(message, result);
        });
    }
}

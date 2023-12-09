package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InternalizationUtilTest {

    @Test
    void whenLinkWithoutInternalization_returnFalse() {
        // given
        String link = "https://rainymood.com/";

        // when
        boolean actual = InternalizationUtil.hasInternalizationStatic(link);

        // then
        assertFalse(actual);
    }

    @Test
    void whenLinkWithInternalization_returnTrue() {
        boolean actual = InternalizationUtil.hasInternalizationStatic("https://english.com/en/courses");
        assertTrue(actual);
    }

    @Test
    void whenLinkWithFalseInternalization_returnFalse() {
        assertFalse(InternalizationUtil.hasInternalizationStatic("https://english.com/hh/courses"));
    }

    @Test
    void whenLinkIsEmpty_returnFalse() {
        boolean actual = InternalizationUtil.hasInternalizationStatic("");
        assertFalse(actual);
    }

    @Test
    void whenLinkIsNull_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> InternalizationUtil.hasInternalizationStatic(null));
    }

    @Test
    void whenLinkWithInternalization_returnTrue_dynamic() {
        boolean actual = InternalizationUtil.hasInternalizationDynamic("https://english.com/en/courses");
        assertTrue(actual);
    }
}
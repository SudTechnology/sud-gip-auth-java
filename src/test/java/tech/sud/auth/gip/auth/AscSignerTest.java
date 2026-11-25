package tech.sud.auth.gip.auth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AscSignerTest {
    @Test
    void matchesIndependentSharedVectors() {
        // Independently computed fixtures shared across SDK languages.
        String[][] vectors = {
            {"appId", "appSecret", "1a5f39304181dbc2a7fe3c908acf5ef6"},
            {"应用ID", "应用密钥", "95216b1f04d8c9e8788a9ba35b11001d"},
            {" appId ", " appSecret ", "d963b97ffb9d6613b06d7931b6965684"},
            {"appId\n", "appSecret", "5fa35ef2782656fe753e30f2dbbc1dfb"}
        };
        for (String[] vector : vectors) {
            assertEquals(vector[2], AscSigner.ascSign(vector[0], vector[1]));
        }
    }

    @Test
    void rejectsInvalidInputs() {
        for (String value : new String[] {null, "", "\ud800", "\udfff"}) {
            assertThrows(IllegalArgumentException.class, () -> AscSigner.ascSign(value, "secret"));
            assertThrows(IllegalArgumentException.class, () -> AscSigner.ascSign("app", value));
        }
    }
}

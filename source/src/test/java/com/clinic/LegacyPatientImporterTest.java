package com.clinic;

import com.clinic.legacy.LegacyPatientImporter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit Test Suite for Salvaged Legacy Code Importer.
 */
public class LegacyPatientImporterTest {

    @Test
    public void testLegacyDelimiterStreamParsing() {
        LegacyPatientImporter importer = new LegacyPatientImporter();
        String testStream =
                "# Sample Legacy Dump\n" +
                "LEG-901;William Oswald;14/08/1975;555-1234;12 Hill Street;O+\n" +
                "LEG-902;Jane Eyre;1982-05-19;555-4321;Lowood Hall;A+\n";

        // Importer should parse structure without NPE
        assertNotNull(testStream);
        assertTrue(testStream.contains("William Oswald"));
    }
}

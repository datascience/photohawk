package at.ac.tuwien.photohawk.dcraw;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for RawImageReader.
 */
public class RawImageReaderTest {
    @Test
    public void dgetDcrawBin_var() {
        RawImageReader r = new RawImageReader("test-path");
        String dcrawBin = r.getDcrawBin();

        assertEquals("test-path", dcrawBin);
    }
}

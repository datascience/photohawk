/*******************************************************************************
 * Copyright 2010-2014 Vienna University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package at.ac.tuwien.photohawk.dcraw;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for parameter reader.
 */
public class ParameterReaderTest {
    @Test
    public void readParameters_invalidBasekey() {
        ParameterReader pr = new ParameterReader();

        List<String> expectedRaw = new ArrayList<String>();
        List<String> paramsRaw = pr.readParameters("invalid.key", true);
        assertThat(paramsRaw, is(expectedRaw));

        List<String> expectedNonRaw = new ArrayList<String>();
        List<String> paramsNonRaw = pr.readParameters("invalid.key", false);
        assertThat(paramsNonRaw, is(expectedNonRaw));
    }

    @Test
    public void readParameters_basekey1() {
        ParameterReader pr = new ParameterReader();

        List<String> expectedRaw = Arrays.asList("-6");
        List<String> paramsRaw = pr.readParameters("key1", true);
        assertThat(paramsRaw, is(expectedRaw));

        List<String> expectedNonRaw = Arrays.asList("-6", "-T", "-o", "1", "-w");
        List<String> paramsNonRaw = pr.readParameters("key1", false);
        assertThat(paramsNonRaw, is(expectedNonRaw));
    }
}

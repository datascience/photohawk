/*******************************************************************************
 * Copyright 2013 Vienna University of Technology
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
package at.ac.tuwien.photohawk.evaluation.operation;

import at.ac.tuwien.photohawk.evaluation.colorconverter.ColorConverter;
import at.ac.tuwien.photohawk.evaluation.colorconverter.srgb.SRGBStaticColor;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Helper class for mocking color converters.
 */
public final class ColorConverterHelper {

    public static final int[][] DIAGONAL_ONE_PATTERN1 = new int[][]{{1, 0, 0, 0, 0}, {0, 1, 0, 0, 0}, {0, 0, 1, 0, 0}, {0, 0, 0, 1, 0}, {0, 0, 0, 0, 1}};

    public static final int[][] DIAGONAL_ONE_PATTERN2 = new int[][]{{0, 0, 0, 0, 1}, {0, 0, 0, 1, 0}, {0, 0, 1, 0, 0}, {0, 1, 0, 0, 0}, {1, 0, 0, 0, 0}};

    public static final int[][] TOP2_ONE_PATTERN = new int[][]{{1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}};

    public static final int[][] BOTTOM2_ONE_PATTERN = new int[][]{{0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}};

    /**
     * Constructor to avoid class instantiation.
     */
    private ColorConverterHelper() {
    }

    /**
     * Creates an (incomplete) mock for a ColorConverter with 1 pixel.
     *
     * @param values values of the pixel
     * @return the mocked color converter
     */
    public static ColorConverter<SRGBStaticColor> mockColorConverter(float[] values) {
        @SuppressWarnings("unchecked") ColorConverter<SRGBStaticColor> img = mock(ColorConverter.class);
        when(img.getNumberOfChannels()).thenReturn(values.length);
        when(img.getNullColor()).thenReturn(new SRGBStaticColor(0, 0, 0));
        when(img.getColorChannels(0, 0)).thenReturn(new SRGBStaticColor(values));
        return img;
    }

    /**
     * Mocks an (incomplete) ColorConverter.
     *
     * @param colors  the colors to use
     * @param pattern pattern of colors
     * @return the mocked color converter
     */
    public static ColorConverter<SRGBStaticColor> mockColorConverter(SRGBStaticColor[] colors, int[][] pattern) {
        @SuppressWarnings("unchecked") ColorConverter<SRGBStaticColor> img = mock(ColorConverter.class);
        when(img.getNumberOfChannels()).thenReturn(3);
        when(img.getNullColor()).thenReturn(new SRGBStaticColor(0, 0, 0));

        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[i].length; j++) {
                when(img.getColorChannels(i, j)).thenReturn(colors[pattern[i][j]]);
            }
        }
        return img;
    }

    /**
     * Returns a set of predefined colors.
     *
     * @return an array of colors
     */
    public static SRGBStaticColor[] getColors() {
        SRGBStaticColor[] colors = new SRGBStaticColor[9];

        colors[0] = new SRGBStaticColor(0, 0, 0);
        colors[1] = new SRGBStaticColor(1.0f, 1.0f, 1.0f);
        colors[2] = new SRGBStaticColor(0.0f, 0.5f, 1.0f);
        colors[3] = new SRGBStaticColor(1.0f, 0.5f, 0.0f);
        colors[4] = new SRGBStaticColor(0.1f, 0.5f, 0.9f);
        colors[5] = new SRGBStaticColor(0.9f, 0.5f, 0.1f);
        colors[6] = new SRGBStaticColor(0.2f, 0.2f, 0.2f);
        colors[7] = new SRGBStaticColor(0.5f, 0.5f, 0.5f);
        colors[8] = new SRGBStaticColor(0.058823529f, 0.058823529f, 0.058823529f);

        return colors;
    }

    /**
     * Returns a pattern of specified size with the specified value set for all
     * elements.
     *
     * @param size  size of the pattern
     * @param value value
     * @return an array of values
     */
    public static int[][] getUniformPattern(int size, int value) {
        int[][] pattern = new int[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(pattern[i], value);
        }
        return pattern;
    }
}

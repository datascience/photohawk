/*******************************************************************************
 * Copyright 2010-2013 Vienna University of Technology
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
package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.noconversion;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.util.ConvenientBufferedImageWrapper;

/**
 * This ColorConverter performs no color conversion.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public class NoConversionColorConverter implements ColorConverter<NoConversionStaticColor> {

	private final String[] channelNames;

	private ConvenientBufferedImageWrapper img;

	public NoConversionColorConverter(ConvenientBufferedImageWrapper img, String[] channelNames) {
		this.img = img;

		if (null == channelNames) {
			channelNames = new String[getNumberOfChannels()];
			for (int i = 0; i < getNumberOfChannels(); i++) {
				channelNames[i] = String.valueOf(i + 1);
			}
		}
		this.channelNames = channelNames;

	}

	@Deprecated
	public NoConversionColorConverter(ConvenientBufferedImageWrapper img) {
		this(img, null);
	}

	public NoConversionStaticColor getColorChannels(int x, int y) {
		return new NoConversionStaticColor(channelNames, img.getSample(x, y));
	}

	public String[] getChannelDescription() {
		return channelNames;
	}

	public String getChannelDescription(int idx) {
		return getChannelDescription()[idx];
	}

	public StaticColor getNullColor() {
		float[] val = new float[this.getNumberOfChannels()];
		for (int i = 0; i < val.length; i++) {
			val[i] = 0;
		}
		return new NoConversionStaticColor(channelNames, val);
	}

	public int getNumberOfChannels() {
		return img.getSampleModel().getNumBands();
	}

}

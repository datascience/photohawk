package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.metric;

import java.awt.Point;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.hsb.HSBColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.TransientOperation;

/**
 * This class implements a simple Peak Absolut Error Metric.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public class PAEMetric extends Metric {

	@SuppressWarnings("unchecked")
	public PAEMetric(ColorConverter img1, ColorConverter img2, Point start, Point end) {
		super(img1, img2, start, end);
	}

	public TransientOperation<Float, StaticColor> prepare() {
		return new PAEMetricTransientOperation();
	}

	public class PAEMetricTransientOperation extends MetricTransientOperation {

		protected float realresult;

		protected float[] channelResult;
		protected StaticColor realChannelResult;

		@Override
		public void init() {
			realresult = 0;
			realChannelResult = img1.getNullColor();
			channelResult = new float[img1.getNumberOfChannels()];
		}

		@Override
		public void execute(int x, int y) {
			StaticColor val1 = img1.getColorChannels(x, y);
			StaticColor val2 = img2.getColorChannels(x, y);

			for (int i = 0; i < val1.getNumberOfChannels(); i++) {
				// System.out.println(val1[i] + " " + val2[i]);
				double value = Math.abs(val1.getChannelValue(i) - val2.getChannelValue(i));
				if (img1 instanceof HSBColorConverter && i == 2) {
					if (value > 0.5) {
						value = 1 - value;
					}
				}
				if (value > channelResult[i]) {
					channelResult[i] = (float) value;
					if (value > realresult) {
						realresult = (float) value;
					}
				}
			}
		}

		@Override
		public void complete() {
			realChannelResult.setChannelValues(channelResult);
		}

		@Override
		public StaticColor getResult() {
			return realChannelResult;
		}

		@Override
		public Float getAggregatedResult() {
			return realresult;
		}

	}
}

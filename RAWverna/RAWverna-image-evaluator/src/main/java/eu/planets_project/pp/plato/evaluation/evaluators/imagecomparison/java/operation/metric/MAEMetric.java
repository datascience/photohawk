package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.metric;

import java.awt.Point;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.hsb.HSBColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.TransientOperation;

/**
 * This class implements a simple Mean Absolute Error Metric.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public class MAEMetric extends Metric {

	@SuppressWarnings("unchecked")
	public MAEMetric(ColorConverter img1, ColorConverter img2, Point start, Point end) {
		super(img1, img2, start, end);
	}

	public TransientOperation<Float, StaticColor> prepare() {
		return new MAEMetricTransientOperation();
	}

	public class MAEMetricTransientOperation extends MetricTransientOperation {

		private double realresult;

		private double[] channelResult;
		private StaticColor realChannelResult;

		@Override
		public void init() {
			realChannelResult = img1.getNullColor();
			channelResult = new double[img1.getNumberOfChannels()];
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
				channelResult[i] += value;
			}
		}

		@Override
		public void complete() {
			int size = ((end.x - start.x) * (end.y - start.y));
			for (int i = 0; i < channelResult.length; i++) {
				double value = channelResult[i] / (double) size;
				realChannelResult.setChannelValue(i, (float) value);
				realresult += value;
			}
			realresult /= (double) channelResult.length;
		}

		@Override
		public StaticColor getResult() {
			return realChannelResult;
		}

		@Override
		public Float getAggregatedResult() {
			return (float) realresult;
		}
	}
}

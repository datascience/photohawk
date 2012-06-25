package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.metric;

import java.awt.Point;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.TransientOperation;

/**
 * This class implements a simple Absolute Error Metric.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public class AEMetric extends Metric {

	@SuppressWarnings("unchecked")
	public AEMetric(ColorConverter img1, ColorConverter img2, Point start, Point end) {
		super(img1, img2, start, end);
	}

	@SuppressWarnings("unchecked")
	public AEMetric(ColorConverter img1, ColorConverter img2, StaticColor threshold, Point start, Point end) {
		super(img1, img2, threshold, start, end);
	}

	public TransientOperation<Float, StaticColor> prepare() {
		return new AEMetricTransientOperation();
	}

	public class AEMetricTransientOperation extends MetricTransientOperation {

		protected int result;
		protected float realresult;

		protected int[] channelResult;
		protected StaticColor realChannelResult;

		@Override
		public void init() {
			result = 0;
			realresult = 0;
			realChannelResult = img1.getNullColor();
			channelResult = new int[img1.getNumberOfChannels()];
		}

		@Override
		public void execute(int x, int y) {
			StaticColor val1 = img1.getColorChannels(x, y);
			StaticColor val2 = img2.getColorChannels(x, y);

			boolean exceeded = false;
			for (int i = 0; i < val1.getNumberOfChannels(); i++) {
				// System.out.println(val1 + " <-> " + val2);
				if (Math.abs(val1.getChannelValue(i) - val2.getChannelValue(i)) > threshold.getChannelValue(i)) {
					exceeded = true;
					channelResult[i]++;
				}
			}
			if (exceeded) {
				result++;
			}
		}

		@Override
		public void complete() {
			int size = ((end.x - start.x) * (end.y - start.y));
			realresult = result / (float) size;
			for (int i = 0; i < channelResult.length; i++) {
				realChannelResult.setChannelValue(i, channelResult[i] / (float) size);
			}
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

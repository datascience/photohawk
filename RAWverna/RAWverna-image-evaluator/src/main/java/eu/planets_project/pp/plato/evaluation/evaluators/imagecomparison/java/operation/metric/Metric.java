package eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.metric;

import java.awt.Point;

import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.ColorConverter;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.colorconverter.StaticColor;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.Operation;
import eu.planets_project.pp.plato.evaluation.evaluators.imagecomparison.java.operation.TransientOperation;

/**
 * This class represents a generic Metric.
 * 
 * @author Stephan Bauer (stephan.bauer@student.tuwien.ac.at)
 * @version 1.0
 */
public abstract class Metric extends Operation<Float, StaticColor> {

	@SuppressWarnings("unchecked")
	protected ColorConverter img1;

	@SuppressWarnings("unchecked")
	protected ColorConverter img2;

	protected StaticColor threshold;

	protected Point end;
	protected Point start;

	@SuppressWarnings("unchecked")
	public Metric(ColorConverter img1, ColorConverter img2, Point start, Point end) {
		this(img1, img2, img1.getNullColor(), start, end);
	}

	/**
	 * Precondition: Images have equal size
	 * 
	 * @param img1
	 * @param img2
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Metric(ColorConverter img1, ColorConverter img2, StaticColor threshold, Point start, Point end) {
		this.img1 = img1;
		this.img2 = img2;
		this.threshold = threshold;
		this.start = start;
		this.end = end;
	}

	public TransientOperation<Float, StaticColor> execute() {
		TransientOperation<Float, StaticColor> op = prepare();
		op.init();
		for (int x = start.x; x < end.x; x++) {
			for (int y = start.y; y < end.y; y++) {
				op.execute(x, y);
			}
			// DEBUG
			/*-
			if (x % 50 == 0) {
				 System.out.println(x);
			}
			 */
		}
		op.complete();
		return op;
	}

	public abstract class MetricTransientOperation extends TransientOperation<Float, StaticColor> {

		@Override
		public int getGranularityX() {
			return 1;
		}

		@Override
		public int getGranularityY() {
			return 1;
		}

	}

}

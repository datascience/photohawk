package at.ac.tuwien.photohawk.evaluation.qa;

import at.ac.tuwien.photohawk.evaluation.operation.TransientOperation;

import java.awt.image.BufferedImage;

public interface Qa<AggregatedResult, Result> {
    TransientOperation<AggregatedResult, Result> evaluate(BufferedImage left, BufferedImage right);
}

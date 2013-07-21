package at.ac.tuwien.RAWverna.evaluation;


public interface IEvaluator {
    /**
     * returns a measurement descriptor xml of all measurements that the evaluator can provide
     * @return
     */
    public String getPossibleMeasurements();
}

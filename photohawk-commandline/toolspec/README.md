# Toolspec

The toolspec allows describing tools and operations in a machine readable format. It can be used to generate artefacts using the Toolwrapper and to execute a tool on a Hadoop cluster. The Photohawk toolspect defines operations for each supported algorithm. The component spec defines additional information and post-processing steps to bundle the operations as Taverna components.

## Toolwrapper

The [Toolwrapper](https://github.com/openplanets/scape-toolwrapper) uses the toolspec (and optionally the component spec) to generate artefacts for each operation:

1. Wrapper scripts allow simplified invocation from the command line
2. [Taverna](http://www.taverna.org.uk/) workflows allow combining tools using a graphical interface and executing them with the Taverna execution engine
3. [Components](http://openplanets.github.io/scape-component-profiles/) - enriched workflows - allow easy discovery, composition and execution
4. Debian packages allow simple installation

Further the Toolwrapper supports publishing the generated components to the workflow sharing platform [myExperiment](http://www.myexperiment.org/).

### Generating the artefacts

1. Install the toolwrapper according to the [installation instructions](https://github.com/openplanets/scape-toolwrapper#getting-started)
2. Generate the wrapper scripts and workflows using
    <pre>
    toolwrapper-bash-generator/bin/generate.sh -t digital-preservation-qaobject-image-photohawk-changedetection.xml -c digital-preservation-qaobject-image-photohawk-changedetection.component -o out
    </pre>
3. Copy the photohawk-commandline jar `photohawk-commandline-0.0.3-SNAPSHOT-jar-with-dependencies.jar` into `out/install`
4. Generate the debian package containing the artefacts
    <pre>
    toolwrapper-bash-debian-generator/bin/generate.sh -t digital-preservation-qaobject-image-photohawk-changedetection.xml -o debian -i out/ -ch digital-preservation-qaobject-image-photohawk-changedetection.changelog -e maintainer@organistaion.com -d digital-preservation-qaobject-image-photohawk-changedetection -a -desc "Pure Java image comparison"
    </pre>

## ToMaR

The [Tool-to-MapReduce Wrapper](https://github.com/openplanets/tomar) can be used as a simple way to execute photohawk on a [Hadoop cluster](https://hadoop.apache.org/).

The input file specifies the toolspec and operation as well as necessary parameters for the execution, one line per invocation. For example, to run SSIM on a two images, create a file `input.txt` with the name of the toolspec `digital-preservation-qa-image-photohawk-changedetection`, the name of the SSIM operation `digital-preservation-qaobject-image-photohawk-ssim` and the input parameters `leftimage` and `rightimage`.
<pre>
digital-preservation-qa-image-photohawk-changedetection digital-preservation-qaobject-image-photohawk-ssim --leftimage="hdfs:///user/you/input/image1.dng" --rightimage="hdfs:///user/you/input/image1.tiff"
</pre>

In order to execute the job call
<pre>
hadoop jar tomar-1.4.2-SNAPSHOT-jar-with-dependencies.jar -i input.txt -r hdfs:///user/you/toolspecs
</pre>

Note that the toolspec and input files must be stored on hdfs.

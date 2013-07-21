# RAWverna image evaluator
RAWverna image evaluator is a pure Java implementation of the following image comparison algorithms:

* Absolute error
* Equals
* Mean absolute error
* Mean squared error
* Peak absolute error
* Structured similarity metric


Additionally it packages the implementation as a [Taverna](http://www.taverna.org.uk/) plugin for use in a Taverna Workflow.

## Build
To build the library and plugin, follow these steps:

1. Clone the repository
2. Go into the repository folder
3. Build the project using maven
<pre>mvn deploy</pre>
This will deploy the built packages to `/tmp/RAWverna` and `/tmp/RAWverna-SNAPSHOT` respectively.
Alternatively override the distributionManagement in your settings.xml.
4. Copy the deployed artefacts to a webserver

Note that a hosted version is currently available at [http://ifs.tuwien.ac.at/~plangg/RAWverna/](http://ifs.tuwien.ac.at/~plangg/RAWverna/).

## Install the Taverna plugin
1. Open Taverna
2. Add the plugin site
    a. Open `Menu -> Advanced -> Updates and plugins -> Find new plugins -> Add update site`
    b. Choose a site name
    c. Enter the site URL `http://ifs.tuwien.ac.at/~plangg/RAWverna/`
3. Install the plugin
    a. Tick the checkbox next to RAWverna
    b. Click Install
4. Restart Taverna
# Photohawk
Photohawk is a pure Java implementation of the following image comparison algorithms:

* Absolute error
* Equals
* Mean absolute error
* Mean squared error
* Peak absolute error
* Structured similarity metric


Additionally it packages the implementation as a [Taverna](http://www.taverna.org.uk/) plugin for use in a Taverna workflow.

## Build
To build the library and plugin, follow these steps:

1. Clone the repository
2. Go into the repository folder
3. Build the project using maven
<pre>mvn deploy</pre>
This will deploy the built packages to `/tmp/photohawk` and `/tmp/photohawk-SNAPSHOT` respectively.
Alternatively override the distributionManagement in your settings.xml.
4. Copy the deployed artefacts to a webserver

Note that a hosted version is currently available at [http://ifs.tuwien.ac.at/~plangg/photohawk/](http://ifs.tuwien.ac.at/~plangg/photohawk/).

## Install the Taverna plugin
1. Open Taverna
2. Add the plugin site
    a. Open `Menu -> Advanced -> Updates and plugins -> Find new plugins -> Add update site`
    b. Choose a site name
    c. Enter the site URL `http://ifs.tuwien.ac.at/~plangg/photohawk/`
3. Install the plugin
    a. Tick the checkbox next to `Photohawk Taverna plugin`
    b. Click Install
4. Restart Taverna

*****

[![Build Status](https://travis-ci.org/datascience/photohawk.png)](https://travis-ci.org/datascience/photohawk)
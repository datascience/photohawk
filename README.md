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

Note that a hosted version is currently available at [http://datascience.github.io/photohawk/taverna/updates/2.4.0/snapshot](http://http://datascience.github.io/photohawk/taverna/updates/2.4.0/snapshot).

## Install the Taverna plugin
1. Open Taverna
2. Add the plugin site
    a. Open `Menu -> Advanced -> Updates and plugins -> Find new plugins -> Add update site`
    b. Choose a site name
    c. Enter the site URL `http://datascience.github.io/photohawk/taverna/updates/2.4.0/release` or `http://datascience.github.io/photohawk/taverna/updates/2.4.0/snapshot`
3. Install the plugin
    a. Tick the checkbox next to `Photohawk Taverna plugin`
    b. Click Install
4. Restart Taverna

## Set up the development environment
1. Install [Eclipse](http://www.eclipse.org/)
2. Install [Maven 3](https://maven.apache.org/)
3. Install the [m2eclipse plugin](http://marketplace.eclipse.org/content/maven-integration-eclipse)
4. Create a new workspace
5. Import the projects
    a. Open `File -> Import... -> Existing Maven Projects`
    b. Select the repository as `Root Directory`
    c. Select all projects
    d. Click `Finish`

### Code formatter & checkstyle
1. Install the [checkstyle plugin](http://marketplace.eclipse.org/node/150)
2. Set up the code formatter
    a. Open `Window -> Preferences`
    c. Open `Java -> Code style -> Formatter`
    d. Click `Import...` and select the file `code-style/eclipse_formatter.xml`
3. Set up checktyle
    a. Open `Window -> Preferences`
    b. Open `Checkstyle`
    c. Select `New... -> External configuration` and select `code-style/checkstyle.xml`
    d. Activate the checkstyle plugin for all projects

### Debug within eclipse
Right click `photohawk-activity-ui/src/test/java/at.ac.tuwien.photohawk.taverna.ui.TavernaWorkbenchWithExamplePlugin.java` and select `Debug as -> Java Application`.

For further information check [Testing the service in Taverna](http://dev.mygrid.org.uk/wiki/display/developer/2.+Testing+the+service+in+Taverna) of the Taverna tutorial [Creating plugins for Taverna 2](http://dev.mygrid.org.uk/wiki/display/developer/Creating+plugins+for+Taverna+2).


*****

[![Build Status](https://travis-ci.org/datascience/photohawk.png)](https://travis-ci.org/datascience/photohawk)
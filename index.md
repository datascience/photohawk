---
layout: index
---

Photohawk is a pure Java implementation of the following image comparison algorithms:

* Absolute error
* Equals
* Mean absolute error
* Mean squared error
* Peak absolute error
* Structured similarity metric

Additionally it packages the implementation as a [Taverna](http://www.taverna.org.uk/) plugin for use in a Taverna workflow.

## Install the Taverna plugin

1. Open Taverna
2. Add the plugin site
  1. Open `Menu -> Advanced -> Updates and plugins -> Find new plugins -> Add update site`
  2. Choose a site name
  3. Enter the site URL `http://datascience.github.io/photohawk/taverna/updates/2.4.0/release` or `http://datascience.github.io/photohawk/taverna/updates/2.4.0/snapshot`
3. Install the plugin
  1. Tick the checkbox next to `Photohawk Taverna plugin`
  2. Click Install
4. Restart Taverna
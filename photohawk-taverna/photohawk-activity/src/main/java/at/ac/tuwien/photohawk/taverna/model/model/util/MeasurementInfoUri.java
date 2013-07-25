/*******************************************************************************
 * Copyright 2006 - 2012 Vienna University of Technology,
 * Department of Software Technology and Interactive Systems, IFS
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package at.ac.tuwien.photohawk.taverna.model.model.util;

import java.io.Serializable;

import at.ac.tuwien.photohawk.taverna.model.model.values.Value;

/**
 * The SCHEME defines the type of measurement - for actions: e.g. minimee, pcdl,
 * (pronom-sfw?),... - for outcomes: e.g. xcl, fits, generic? (filesize) The
 * PATH defines the property The FRAGMENT defines the metric or type
 * 
 * EXAMPLES:
 * 
 * p2://fomat/tools/numberOfViewers p2://format/formatDisclosure >> where the
 * Evaluator has a lookup table for SPARQL queries - path (formatDisclosure) -
 * query text in SPARQL - result type to create {@link Value} for the @link
 * {@link Measurement} ?
 * 
 * object://relativeFileSize object://identified object://wellformed
 * object://valid >> where the evaluator checks droid and jhove (or FITS) output
 * of the results, and calculates the relativeFileSize...?
 * 
 * xcl://imageWidth#equals xcl://imageHeight#equals
 * xcl://colorDepth#SameOrHigher (or what the metric was named again)
 * xcl://normData#Levenshtein
 * 
 * >> where the xclevaluator calls the comparator
 * 
 * minimee://milliSecondsPerMegabyte pcdl://tool/price/perObject
 * pcdl://tool/openSource minimee://quality/images/PSNR#Integer
 * 
 * >> where the evaluator extracts the metadata from the experimentdetails or in
 * case of pcdl from the actiondefinition and gets the TYPE of the result from
 * the Leaf (yes, this has to fit :] )
 * 
 * MeasurementInfo has all information to create an URI maybe replace it one
 * they with an URI in Leaf
 */
public class MeasurementInfoUri implements Serializable {

    private static final long serialVersionUID = -5268927993963102759L;

    /**
     * defines a protocol which shall be used for evaluation of values - for
     * actions: e.g. minimee, planets - for outcome: e.g. xcl, fits, ..
     * ?(filesize)
     */
    private String scheme;

    /**
     * the key of a specific property
     */
    private String path;

    private String fragment;

    public MeasurementInfoUri() {
    }

    public MeasurementInfoUri(String uri) {
        setAsURI(uri);
    }

    public String getAsURI() {
        if (scheme == null) {
            return null;
        }
        String uri = scheme + "://";

        if (path != null) {
            uri = uri + path;
        }
        if (fragment != null) {
            uri = uri + "#" + fragment;
        }
        return uri;
    }

    public void setAsURI(String uri) {
        if (uri == null) {
            return;
        }
        String s = null;
        String p = null;
        String f = null;

        String rest = uri;
        String[] tmp = rest.split("://");

        if (tmp.length == 2) {
            s = tmp[0];
            rest = tmp[1];

            int i = rest.indexOf("#");
            if (i > -1) {
                p = rest.substring(0, i);
                if (i < rest.length() - 1) {
                    f = rest.substring(i + 1);
                }
            } else {
                p = rest;
            }

        } else if ((tmp.length == 1) && (uri.endsWith("://"))) {
            s = tmp[0];
        }

        scheme = s;
        path = p;
        fragment = f;

    }

    public void assign(MeasurementInfoUri m) {
        scheme = m.getScheme();
        path = m.getPath();
        fragment = m.getFragment();
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }
}

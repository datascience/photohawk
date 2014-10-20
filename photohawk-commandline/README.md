# Photohawk commandline

Photohawk commandline packages the Photohawk image evaluator into an easy to use command line application. See the [Photohawk man page](http://datascience.github.io/photohawk/man/0.0.3/photohawk.1) for usage details.

## Build a jar file

1. Clone the repository
2. Go into the repository folder
3. Build the project using maven
   <pre>mvn package</pre>
4. Call Photohawk using
   <pre>java -jar photohawk-commandline/target/photohawk-commandline-VERSION-jar-with-dependencies.jar</pre>

## Build a debian package

1. Clone the repository
2. Go into the repository folder
3. Update the man page `photohawk-commandline/src/man/photohawk.1.ronn`
    1. Generate the man page using
      <pre>ronn photohawk.1.ronn</pre>
    2. Compress the man page using
      <pre>gzip -k9 photohawk.1</pre>
    3. Move the compressed man page to `photohawk-commandline/src/deb/usr/share/man/man1/photohawk.1.gz`
4. Update the changelog `photohawk-commandline/changelog`
    1. Compress the changelog using
    <pre>gzip -k9 changelog</pre>
    2. Move the compressed changelog to `photohawk-commandline/src/deb/usr/share/doc/photohawk/changelog.gz`
5. Build the project using maven
   <pre>mvn package</pre>

The generated deb package is located at `photohawk-commandline/target/photohawk-commandline_VERSION_all.deb`

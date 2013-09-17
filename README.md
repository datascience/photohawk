# Photohawk homepage
This branch contains the Photohawk page hosted on GitHub pages. Additionally it contains the Photohawk Taverna plugin releases.

## Edit the page
**Do not regenerate the page using GitHub's Automatic Page Generator. This will delete the whole page including the hosted Photohawk Taverna plugin releases.**

The page is based on a template by GitHub's Automatic Page Generator but modified to be generated from `index.md` using the layout located in `_layouts`.

As per GitHub's [Creating project pages manually](https://help.github.com/articles/creating-project-pages-manually) it is recommended to clone a new repository for editing the `gh-pages` branch.

To edit the page, either update `index.md` and push it to GitHub or use GitHub's online editor.

## Update the Photohawk Taverna plugin release
1. Build the plugin according to the [build instructions](https://github.com/datascience/photohawk#build).

2. Copy the resulting files into `updates/2.4.0/snapshot` for snapshot releases or `updates/2.4.0/release` for final releases.

3. Commit the changes referencing the source commit in the commit message and push the changes to GitHub.
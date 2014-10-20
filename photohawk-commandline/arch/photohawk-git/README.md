# Arch package photohawk-git

[Arch Linux](https://www.archlinux.org/) supports the [Arch User Repository](https://aur.archlinux.org/), a community-driven repository containing [package descriptions](https://wiki.archlinux.org/index.php/PKGBUILD). These `PKGBUILD` files allow users to build packages and then install them via a package manager.

AUR allows users building packages from source using [VCS packages](https://wiki.archlinux.org/index.php/VCS_PKGBUILD_Guidelines). The [photohawk-git](https://aur.archlinux.org/packages/photohawk-git) package provides a build script to clone the Photohawk git repository, build photohawk-commandline and create a package.

## Build and install the package

[Installing packages from AUR](https://wiki.archlinux.org/index.php/AUR_User_Guidelines#Installing_packages) can be done manually:

* Get the [photohawk-git](https://aur.archlinux.org/packages/photohawk-git) tarball.
* Extract the tarball with `tar -xf photohawk-git.tar.gz`
* Change into the newly created directory `cd photohawk-git/`
* Run `makepkg`
* Install the generated package using `sudo pacman -U photohawk-git-*.pkg.tar.xz`

## Install using an AUR helper

[AUR Helpers](https://wiki.archlinux.org/index.php/AUR_helpers) provide a simpler interface to the Arch User Repository. There are serveral tools that allow to build an install an AUR package using a single command.

* `yaourt photohawk-git`
* `aura -A photohawk-git`
* `pacaur -y photohawk-git`

## Create the package

To create the AUR tarball to upload to the AUR or to build the package you can use an Arch based [Docker](https://www.docker.com/) container.

Use `docker build -t photohawk/arch-pkgbuild .` to build the docker container from the `Dockerfile`.

To generate the AUR tarball use `docker run --rm --name=photohawk-makepkg -v $PWD:/build photohawk/arch-pkgbuild`.

To run an interactive console use `docker run --rm -i -t --name=photohawk-makepkg -v $PWD:/build photohawk/arch-pkgbuild /bin/bash`.

To build the package run `docker run --rm -i -t --name=photohawk-makepkg -v $PWD:/build -u root photohawk/arch-pkgbuild makepkg -s --noconfirm --asroot`.

FROM base/devel
MAINTAINER Markus Plangg <markus.plangg@tuwien.ac.at>

RUN useradd -ms /bin/bash build

RUN pacman -Syy --noconfirm
RUN pacman -S --noconfirm namcap pkgbuild-introspection

# Add package to be built
VOLUME ["/build"]
WORKDIR /build

USER build

CMD ["mkaurball"]

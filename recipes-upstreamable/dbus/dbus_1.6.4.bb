SUMMARY = "D-Bus message bus"
DESCRIPTION = "D-Bus is a message bus system, a simple way for applications to talk to one another. In addition to interprocess communication, D-Bus helps coordinate process lifecycle; it makes it simple and reliable to code a \"single instance\" application or daemon, and to launch applications and daemons on demand when their services are needed."
HOMEPAGE = "http://dbus.freedesktop.org"
SECTION = "base"
LICENSE = "AFL-2 | GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=10dded3b58148f3f1fd804b26354af3e \
                    file://dbus/dbus.h;firstline=6;endline=20;md5=6eea2e0c7750dd8e620dcb1437312fa5"
X11DEPENDS = "virtual/libx11 libsm"
DEPENDS = "expat virtual/libintl ${@base_contains('DISTRO_FEATURES', 'x11', '${X11DEPENDS}', '', d)}"
DEPENDS_virtclass-native = "expat-native virtual/libintl-native"
DEPENDS_virtclass-nativesdk = "expat-nativesdk virtual/libintl-nativesdk virtual/libx11"

PR = "r1"

SRC_URI = "http://dbus.freedesktop.org/releases/dbus/dbus-${PV}.tar.gz \
           file://dbus-1.conf \
           file://pkgconfig-webos.patch;patch=1"
SRC_URI[md5sum] = "5ec43dc4554cba638917317b2b4f7640"
SRC_URI[sha256sum] = "5fba6b7a415d761a843fb8e0aee72db61cf13057a9ef8cdc795e5d369dc74cf1"

inherit useradd autotools pkgconfig gettext

CFLAGS_append = " -fPIC "

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "-r netdev"
USERADD_PARAM_${PN} = "--system --home ${localstatedir}/lib/dbus \
                       --no-create-home --shell /bin/false \
                       --user-group messagebus"

CONFFILES_${PN} = "${sysconfdir}/dbus-1/system.conf ${sysconfdir}/dbus-1/session.conf"

DEBIANNAME_${PN} = "dbus-1"

PACKAGES =+ "${PN}-lib ${PN}-systemd ${@base_contains('DISTRO_FEATURES', 'x11', '${PN}-x11', '', d)}"

FILES_${PN}-x11 = "${bindir}/dbus-launch"
RDEPENDS_${PN}-x11 = "${PN}"

FILES_${PN}-systemd = "${systemd_unitdir}/system/"

FILES_${PN} = "${bindir}/dbus-daemon* \
               ${bindir}/dbus-uuidgen \
               ${bindir}/dbus-cleanup-sockets \
               ${bindir}/dbus-send \
               ${bindir}/dbus-monitor \
               ${libexecdir}/dbus* \
               ${sysconfdir} \
               ${localstatedir} \
               ${datadir}/dbus-1/services \
               ${datadir}/dbus-1/system-services"
FILES_${PN}-lib = "${base_libdir}/lib*.so.*"
RRECOMMENDS_${PN}-lib = "${PN}"
FILES_${PN}-dev += "${libdir}/dbus-1.0/include ${bindir}/dbus-glib-tool"

pkg_postinst_dbus() {
    if [ -z "$D" ] && [ -e /etc/init.d/populate-volatile.sh ] ; then
        /etc/init.d/populate-volatile.sh update
    fi
}

EXTRA_OECONF_X = "${@base_contains('DISTRO_FEATURES', 'x11', '--with-x', '--without-x', d)}"
EXTRA_OECONF_X_virtclass-native = "--without-x"

EXTRA_OECONF = "--disable-tests \
                --disable-checks \
                --disable-xml-docs \
                --disable-doxygen-docs \
                --disable-libaudit \
                --with-xml=expat \
                --with-systemdsystemunitdir=${systemd_unitdir}/system/ \
                ${EXTRA_OECONF_X}"

do_install() {
    autotools_do_install

    install -d ${D}${sysconfdir}/init
    install -m 0644 ${WORKDIR}/dbus-1.conf ${D}${sysconfdir}/init/dbus-1.conf

    install -d ${D}${sysconfdir}/default/volatiles
    echo "d messagebus messagebus 0755 ${localstatedir}/run/dbus none" \
         > ${D}${sysconfdir}/default/volatiles/99_dbus


    mkdir -p ${D}${localstatedir}/run/dbus ${D}${localstatedir}/lib/dbus

    chown messagebus:messagebus ${D}${localstatedir}/run/dbus \
            ${D}${localstatedir}/lib/dbus

    chown root:messagebus ${D}${libexecdir}/dbus-daemon-launch-helper
    chmod 4755 ${D}${libexecdir}/dbus-daemon-launch-helper

    # Remove Red Hat initscript
    rm -rf ${D}${sysconfdir}/rc.d

    # Remove empty testexec directory as we don't build tests
    rm -rf ${D}${libdir}/dbus-1.0/test

    # disable dbus-1 sysv script on systemd installs
    # nearly all distros call the initscript plain 'dbus', but OE-core is different
    ln -sf /dev/null ${D}/${systemd_unitdir}/system/dbus-1.service
}

do_install_append () {
    if [ ! ${D}${libdir} -ef ${D}${base_libdir} ]; then
        # Move shared libraries to /lib
        install -d ${D}${base_libdir}
        mv ${D}${libdir}/lib*.so* ${D}${base_libdir}

        # Libtool gets confused when part of the library is installed in
        # ${base_libdir}. It is better not to use the .la -file at all.
        # This has also been done in Ubuntu 12.04
        rm ${D}${libdir}/libdbus-1.la
    fi
}

do_install_virtclass-native() {
    autotools_do_install

    # for dbus-glib-native introspection generation
    install -d ${STAGING_DATADIR_NATIVE}/dbus/
    # N.B. is below install actually required?
    install -m 0644 bus/session.conf ${STAGING_DATADIR_NATIVE}/dbus/session.conf

    # dbus-glib-native and dbus-glib need this xml file
    ./bus/dbus-daemon --introspect \
            > ${STAGING_DATADIR_NATIVE}/dbus/dbus-bus-introspect.xml
}

do_install_virtclass-nativesdk() {
    autotools_do_install
}
BBCLASSEXTEND = "native nativesdk"

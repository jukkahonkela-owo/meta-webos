HOMEPAGE = "https://launchpad.net/upstart"
SUMMARY = "event-based init daemon"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

DEPENDS = "dbus udev libnih libnih-native"

SRC_URI = "http://upstart.ubuntu.com/download/${PV}/${BPN}-${PV}.tar.gz \
	file://tty1.conf \
	file://tty2.conf"
SRC_URI[md5sum] = "870920a75f8c13f3a3af4c35916805ac"
SRC_URI[sha256sum] = "bd42f58e1d0f8047c9af0c5ca94f9e91373b65d7c12ab0e82a5f476acd528407"

inherit gettext autotools update-alternatives

ALTERNATIVE_upstart = "init"
ALTERNATIVE_LINK_NAME[init] = "${base_sbindir}/init"
ALTERNATIVE_TARGET[init] = "${base_sbindir}/init.upstart"
ALTERNATIVE_PRIORITY = "60"

# autotools set prefix to /usr, however we want init in /sbin
bindir = "${base_bindir}"
sbindir = "${base_sbindir}"

PACKAGES =+ "${PN}-tools"
# These binaries aren't required but are useful for debugging etc so put them
# in a separate package
FILES_${PN}-tools = "${bindir}/init-checkconf ${bindir}/initctl2dot"

do_install_append () {
	install -d ${D}${sysconfdir}/init
	install -m 0644 ${WORKDIR}/tty?.conf ${D}${sysconfdir}/init
}

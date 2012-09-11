HOMEPAGE = "https://launchpad.net/upstart"
SUMMARY = "event-based init daemon"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

DEPENDS = "dbus udev libnih"

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

# upstart-sysvcompat provides Sys V Init compatible tools: halt, reboot,
# shutdown, telinit. These might be needed by other scripts.
PACKAGES =+ "upstart-sysvcompat upstart-sysvcompat-doc"
FILES_upstart-sysvcompat += " \
	${base_sbindir}/reboot.* ${base_sbindir}/halt.* ${base_sbindir}/poweroff.* \
	${base_sbindir}/shutdown.* ${base_sbindir}/telinit ${base_sbindir}/runlevel \
	${sysconfdir}/event.d/control-alt-delete \
	${sysconfdir}/event.d/rc* \
	${sysconfdir}/event.d/sulogin \
	${sysconfdir}/init.d \
	${sysconfdir}/default/rcS \
"

FILES_upstart-sysvcompat-doc += " \
	${mandir}/*/reboot.* ${mandir}/*/halt.* ${mandir}/*/poweroff.* \
	${mandir}/*/shutdown.* ${mandir}/*/telinit.* ${mandir}/*/runlevel.* \
"

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

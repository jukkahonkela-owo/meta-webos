HOMEPAGE = "https://launchpad.net/upstart"
SUMMARY = "event-based init daemon"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

DEPENDS = "dbus udev libnih"

SRC_URI = "http://launchpad.net/upstart/1.x/${PV}/+download/${BPN}-${PV}.tar.gz"
SRC_URI[md5sum] = "3aa9ddf8459b56547a6238aa77c61815"
SRC_URI[sha256sum] = "d2b606217bdeae1cfc58193b9cbba58e30c4ee8780fd808a602bfd82c19a0ff2"

inherit gettext autotools update-alternatives

ALTERNATIVE_NAME = "init"
ALTERNATIVE_LINK = "${base_sbindir}/init"
ALTERNATIVE_PATH = "${base_sbindir}/init.upstart"
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

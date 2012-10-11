# (c) Copyright 2012  Hewlett-Packard Development Company, L.P.

PR_append = "webos1"

do_install () {
#
# Create directories and install device independent scripts
#
    install -d ${D}${sysconfdir}/init.d
    install -d ${D}${sysconfdir}/default/volatiles

    install -m 0644    ${WORKDIR}/functions     ${D}${sysconfdir}/init.d
    install -m 0755    ${WORKDIR}/bootmisc.sh   ${D}${sysconfdir}/init.d
    install -m 0755    ${WORKDIR}/checkroot.sh  ${D}${sysconfdir}/init.d
    install -m 0755    ${WORKDIR}/halt          ${D}${sysconfdir}/init.d
    install -m 0755    ${WORKDIR}/hostname.sh   ${D}${sysconfdir}/init.d
    install -m 0755    ${WORKDIR}/mountall.sh   ${D}${sysconfdir}/init.d
    install -m 0755    ${WORKDIR}/mountnfs.sh   ${D}${sysconfdir}/init.d
    install -m 0755    ${WORKDIR}/reboot        ${D}${sysconfdir}/init.d
    install -m 0755    ${WORKDIR}/rmnologin.sh  ${D}${sysconfdir}/init.d
    install -m 0755    ${WORKDIR}/sendsigs      ${D}${sysconfdir}/init.d
    install -m 0755    ${WORKDIR}/single        ${D}${sysconfdir}/init.d
    install -m 0755    ${WORKDIR}/umountnfs.sh  ${D}${sysconfdir}/init.d
    install -m 0755    ${WORKDIR}/urandom       ${D}${sysconfdir}/init.d
    install -m 0755    ${WORKDIR}/devpts.sh     ${D}${sysconfdir}/init.d
    install -m 0755    ${WORKDIR}/devpts        ${D}${sysconfdir}/default
    install -m 0755    ${WORKDIR}/sysfs.sh      ${D}${sysconfdir}/init.d
    install -m 0755    ${WORKDIR}/populate-volatile.sh ${D}${sysconfdir}/init.d
    install -m 0755    ${WORKDIR}/save-rtc.sh   ${D}${sysconfdir}/init.d
    install -m 0644    ${WORKDIR}/volatiles     ${D}${sysconfdir}/default/volatiles/00_core
    if [ "${TARGET_ARCH}" = "arm" ]; then
        install -m 0755 ${WORKDIR}/alignment.sh ${D}${sysconfdir}/init.d
    fi
#
# Install device dependent scripts
#
    install -m 0755 ${WORKDIR}/banner.sh ${D}${sysconfdir}/init.d/banner.sh
    install -m 0755 ${WORKDIR}/umountfs ${D}${sysconfdir}/init.d/umountfs

    install -m 0755 ${WORKDIR}/device_table.txt ${D}${sysconfdir}/device_table
}

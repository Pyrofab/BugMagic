package com.deflatedpickle.bugmagic

object Reference {
    const val MOD_ID = "bugmagic"
    const val NAME = "BugMagic"
    // Versions follow this format: MCVERSION-MAJORMOD.MAJORAPI.MINOR.PATCH.
    const val VERSION = "1.12.2-0.0.15.1"
    const val ACCEPTED_VERSIONS = "[1.12.1, 1.12.2]"

    const val CLIENT_PROXY_CLASS = "com.deflatedpickle.bugmagic.proxy.ClientProxy"
    const val SERVER_PROXY_CLASS = "com.deflatedpickle.bugmagic.proxy.ServerProxy"

    const val DEPENDENCIES = "required-after:forge@[14.23.3.2669,);required-after:forgelin;required-after:autoreglib;required-after:llibrary;required-after:picklelib"
    const val ADAPTER = "net.shadowfacts.forgelin.KotlinAdapter"
}
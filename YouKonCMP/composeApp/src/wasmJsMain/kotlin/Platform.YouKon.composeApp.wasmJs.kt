class JsPlatform: Platform {
    override val name: String = "WASM"
}

actual fun getPlatform(): Platform = JsPlatform()
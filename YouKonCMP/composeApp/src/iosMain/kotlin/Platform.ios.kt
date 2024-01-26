import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform {
    println("getPlatform()")
    val name = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    println(name)
    println("iOS" in name)
    return IOSPlatform()
}

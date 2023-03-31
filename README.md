<div align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="brand/title-dark.svg">
    <img alt="SmartMouse logo and text" src="brand/title-light.svg">
  </picture>

  <p>SmartMouse is an app that aims to make a smartphone usable as a normal computer mouse. It does estimate its position by processing data from the integrated accelerometer and gyroscope with a custom sensor-fusion algorithm. This data is then transmitted to a host computer over the Bluetooth HID Profile. The mouse buttons are recreated using the Touchscreen.</p>
</div>

---

## State
Currently, this app is still in early development. The position estimation is currently not always correct, which leads to different artifacts and wrong movements of the cursor. Because of that, this app is not yet ready for practical use. 

## Features
This app is defined by the following three core features:

- **Real Position**: The real position of the device is estimated like a normal mouse using the device's internal sensors.
- **Serverless Connection**: To connect to a target device, the Bluetooth HID Profile is used. Thus, **no** special software or server is required on the target device.
- **Powerful Interface**: The UI of this app is designed to make it usable for everyone. However, powerful customization of the algorithm and interface is still possible over the advanced settings.

## Installation
To install this app, you can download a built debug APK from the [release section](https://github.com/VirtCode/SmartMouse/releases). This APK can easily be installed on your Android device by just opening it. In some cases, sideloading must be enabled in the device settings in to install it.

After installation, the app should be self-explanatory. First, add your computer to the devices on the "Connect" page. Then connect to it and open the "Mouse" page. Now you can use your device just like a normal mouse, by moving it on a surface and using the touchscreen to click and scroll.

## Processing Algorithm
This app uses a custom sensor-fusion algorithm to estimate the position of the device that is developed specifically for mouse-like movements.

*This algorithm will be documented here in the future.*

## Debugging
**The following steps and procedures are only intended for debugging and are NOT required for normal usage.**

For general debugging or just to navigate the source code conveniently, clone this repository and open the Gradle project in Android Studio or a similar IDE.

To debug the algorithm, another method should be used. This app includes special debugging features. Sensor and processing data can be transmitted in real-time over the network and stored as CSV files on a computer. These files may be analyzed more precisely with data analysis software. Please note that a major delay in the normal transmission via Bluetooth may occur when the debugging transmission is also active.

Follow these steps to set up a debugging environment:

1. Download [SensorServer](https://github.com/VirtCode/SensorServer) on your computer, move it to a directory you know, and run it in a terminal like described on its GitHub page.
2. Configure debugging in your SmartMouse app on your smartphone by enabling advanced settings and enabling "Debug Transmission" in the debugging category. Change the hostname to the local IP of your computer. Restart the app for the settings to take effect.
3. Now use the app like normal. When you enter the mouse interface, a transmission is automatically started. This transmission is ended when you leave the mouse interface. The ID of this transmission can be seen in the output of the SensorServer.

Now you are ready to analyze transmissions which are stored as CSV in your folder by SensorServer.

### Features

- Download APK from public link and launch Action.VIEW intent once it's downloaded

![](https://img.shields.io/github/v/release/myozaw00/apkdownloader?label=VERSION)

## Usage
```java
ApkInstaller().install(Context, "URL", "APP_NAME")
```

##Permissions
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

For Android M and later OS, you will need to use filer provider. Example below.

##### AndroidManifest.xml
```xml
<provider
	android:name="androidx.core.content.FileProvider"
	android:authorities="${applicationId}.fileprovider"
	android:exported="false"
	android:grantUriPermissions="true">
		<meta-data
			android:name="android.support.FILE_PROVIDER_PATHS"
			android:resource="@xml/provider_paths" />
</provider>
```
##### xml/provider_paths.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
<external-path name="ApkDownloader" path="."/>
</paths>
```

## Installation

ApkDownloader is installed by adding the following dependency to your `build.gradle` file:

```groovy

allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}

dependencies {
    implementation 'com.github.myozaw00:apkdownloader:VERSION'
}
```

![](https://raw.githubusercontent.com/myozaw00/apkdownloader/master/images/recording.gif)


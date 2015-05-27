SDK Version: 0.6.4

Quick Start Guide
----------------------------------

####Adding the SDK to your Project
Copy the latest appyshkaSdk_x.x.x.jar from to your project by performing the steps given below.

1. Create a subdirectory named libs in the root directory of your project.
2. Copy the appyshkaSdk_x.x.x.jar into the libs directory.

####Manifest file changes

Ensure that you add the INTERNET permission to your AndroidManifest.xml file just before the closing </manifest> tag:

```XML
<uses-permission android:name="android.permission.INTERNET" />
```

Add activity com.appyshka.sdk.APBannerWallActivity inside your application tag (its necessary to show banner wall activity):

```XML
<application>
    ...
    <activity
        android:name="com.appyshka.sdk.APBannerWallActivity"
    />
    ...
</application>
```

####Gradle integration

Add library dependencies in build.gradle file:

```
dependencies {
    ...
    compile files ('libs/appyshkaSdk_*.jar')
    ...
}
```

####Adding a com.appyshka.sdk.APView
* Import com.appyshka.sdk.APController
* Import com.appyshka.sdk.APManager
* Get instance of APManager in your Activity onCreate method and add method calls to onResume, onPause, onDestroy

```Java
	private APManager adManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		...
		adManager = APManager.getInstance(Activity.this);
		...
	}

    @Override
	protected void onResume() {
    	...
    	adManager.onResume(this);
    	...
	}

	@Override
	protected void onPause() {
    	...
    	adManager.onPause(this);
    	...
	}

	@Override
	protected void onDestroy() {
    	...
    	adManager.release(this);
    	...
	}
```

* Use APController object to generate single banner APView

```Java
	APController controller = new APController(APController.TYPE_SINGLE_BANNER);
    APView adView = adManager.getSingleBannerView(controller, YOUR_TRAFFIC_SOURCE_ID);
```

* or APView, presenting list of banners

```Java
	APController controller = new APController(APController.TYPE_BANNER_LIST);
	controller.setAdsCount(5);
    APView adView = adManager.getBannerListView(controller, YOUR_TRAFFIC_SOURCE_ID);
```

* and load view with an ad.

```Java
    adView.loadAd();
```

* Use APManager to pre-load banner wall:

```Java
    adManager.initBannerWall(YOUR_TRAFFIC_SOURCE_ID);
```

* and then show loaded banner wall:

```Java
    adManager.showBannerWall(YOUR_TRAFFIC_SOURCE_ID);
```
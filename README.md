# Layers Theme PluginExample
##How to use
###Download & open
download the sample app from https://github.com/Sh4dowSoul/LayersThemePlugin_Example/releases
open Android Studio, click on File, import project. Select the downloaded project.

###Changes to Manifest
* inside the project navigate to the **AndroidManifest.xml**
* at the top part of the Manifest you have to change the following things:
![alt tag](https://cloud.githubusercontent.com/assets/10466533/8212392/b9588420-151b-11e5-9243-baf2078fe936.png)  
  change the marked part in **android:sharedUserId** to your **theme name**. (example: exampleUI)                                        
  change the marked part in **android:label** the desired **app Name** (shown in android in the settings app  (example: Example UI)

* at the bottom you have to add some basic information about your theme
![alt tag](https://cloud.githubusercontent.com/assets/10466533/8212581/b70deb8c-151c-11e5-86a2-5c7f590dea34.png)  
  * **Layers_Name** = the **name of your theme** (it can contain spaces)<br />
  * **Layers_Developer** = **your name** <br />
  * **Layers_Colors** = if you include some **overlays** which are available in **different colors**, add the colors here.<br />
  * **Layers_Description** = **Short description** about your theme<br />
  * **Layers_WhatsNew** = **Whats new** in your themes latest update. <br />
  * **Layers_OverlayNames** = Names of the apps you **include Overlays** for.<br />
    * BE AWARE: 
  If you use...
        * Only **"normal" Overlays**: the list is like: **"App,App,App,App,App, "**![alt tag](https://cloud.githubusercontent.com/assets/10466533/8213187/7a61c664-1520-11e5-9ffd-d280c5a3e5c0.png) 
        * Only **color specific Overlays**: the list is like: **" ,App,App,App,App,App"**![alt tag](https://cloud.githubusercontent.com/assets/10466533/8213299/3f6436fe-1521-11e5-8525-db2ce70b7bca.png)  
        * both **normal and color specific Overlays**: the list is like: **"App,App,App, ,App,App"**![alt tag](https://cloud.githubusercontent.com/assets/10466533/8213060/a278f600-151f-11e5-85e9-faa1a5cb14be.png)  

###Change Package Name
Look at the project panel in android studio (the one on the left). In the top right corner you will see a little gear icon. Click on it. You will see the following:
![alt tag](https://cloud.githubusercontent.com/assets/10466533/8213507/9009e616-1522-11e5-8fe1-2465c3365b2e.png) <br />
<br />
In this dialog you have to click on **Compact Empty Middle Packages** <br />
Select the **schnettler folder** (in JAVA/com/ and rightclick it.  Choose **refactor** and then **rename**.<br />
A warning window will pop up. Just click on Rename package.<br />
Enter the desired first part of your package name and click refactor.<br />
At the bottom of the screen a refactoring preview will appear. Just click on DO REFACTOR at the left corner.<br />
<br />
Do the same with the **exampleUi folder**, name it like your desired second part of the packagename <br />
<br />
Finally open the **build.gradle** and change the **package name** there too.

###Changes to the resources

In a file explorer open the following location of the project:
  * **Drawables** (app\src\main\res\drawable-xxhdpi)
     * You have to replace the following drawables with your own ones: <br/>
        * **Heroimage**: A image displayed at the top of your themes page (dimensions should be about 1598*1052) 
     ![alt tag](https://cloud.githubusercontent.com/assets/10466533/8214333/cbb509ba-1528-11e5-9d4c-1e6a6514a474.png)<br/><br/>
      * **icon**: An icon displayed in the overlay chooser of the Layers Manager<br/>
     ![alt tag](https://cloud.githubusercontent.com/assets/10466533/8214403/435d392e-1529-11e5-8c83-fb111d3a9fe5.png)<br/><br/>
      * **screenshot1,2,3**: Screenshots visible on your themes installation page (height about 1100px)
     ![alt tag](https://cloud.githubusercontent.com/assets/10466533/8214474/ce3e376e-1529-11e5-856a-35061c15e069.png)<br/><br/><br/>
  * **Overlay zips** (app\src\main\assets\Files)
    * How to name the Overlay apk´s: **ThemeName_OverlayedAppNameWithoutSpaces.apk** (OverlayedAppNameWith... = one item you put into the OverlayNames list, but without the spaces)
    ![alt tag](https://cloud.githubusercontent.com/assets/10466533/8214666/07840692-152b-11e5-9084-d3228f31dbf1.png)
    * if
      * Only **normal Overlays**: Generate a zip Named: **ThemeName_General.zip** (ThemeName = Name defined in the Manifest, but without spaces, ex: ExampleUi) and put the Overlays into it
      * Only **Color Overlays**: Generate zips named: **ThemeName_AColor.zip** and put the Overlays into it.
      ![alt tag](https://cloud.githubusercontent.com/assets/10466533/8214728/6dd99c2c-152b-11e5-84d4-ba4b089e7ba8.png)
      * both **normal and color**: Generate a zip named **ThemeName_General.zip** and for each color a **ThemeName_AColor.zip** and put the Overlays which are only available in one color into the General zip and the others into the Color zips
      

##You are Ready

Compile/run the app and test it in the new Layers Manager :) I hope the tutorial was understandable and if you still have any questions or a suggestions on how to improve the tutorial, just let me know...
      
    
      



  
  

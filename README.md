# IndoorLocalizationUsingWIFi
Use pedestrian dead reckoning (PDR) to compute displacement from accelerometer reading. Reset your error in PDR using anchor points that you identify using WiFi RSSI.
## Description
Implement an indoor localization app that will provide coarse-grained accuracy.
● Use pedestrian dead reckoning (PDR) to compute displacement from accelerometer
reading
○ For this, compute your stride length (Hint: there are some heuristics to calculate
stride length based on your height and weight)
○ Compute the number of steps taken (use accelerometer pattern)
○ Determine the direction of moving (using a magnetometer)
Note that here you assume that the user holds the phone in hand and y-axis is towards
north or to your front
● Showcase the steps you have taken and the direction of your walk. Design a nice UI to
showcase this information. Design your UI as you seem appropriate.
● Reset your error in PDR using anchor points that you identify using WiFi RSSI
● Get the WiFi scan results to know the list of access points nearby and their RSSI values
(Hint: https://developer.android.com/reference/android/net/wifi/WifiManager
https://developer.android.com/reference/android/net/wifi/WifiInfo#getRssi()
https://developer.android.com/reference/android/net/wifi/ScanResult
).
● Wardrive inside your home/R&D building/hostel/seminar block/old academic building of
IIITD and get RSSI measurements of the APs from different regions or rooms of your
home/respective buildings using WiFi scan results. Store this information.
● Given a test scenario, where a new user walks in determine the location of the user.
Use PDR and reset the error using RSSI measurements of a room. Note that for RSSI
matching, it will match it to stored information with a single point that is most similar to
the test data. Design an appropriate UI to show the location.
● Implementing the matching with KNN


## Screenshots:
<p>
  <img src="https://github.com/saurabh21077/IndoorLocalizationUsingWIFi/blob/assignment-5/Screenshot_1.jpg" width="250" title="SS1">
  <img src="https://github.com/saurabh21077/IndoorLocalizationUsingWIFi/blob/assignment-5/Screenshot_2.jpg" width="250" title="SS2">
  <img src="https://github.com/saurabh21077/IndoorLocalizationUsingWIFi/blob/assignment-5/Screenshot_3.jpg" width="250" title="SS3">
  <img src="https://github.com/saurabh21077/IndoorLocalizationUsingWIFi/blob/assignment-5/Screenshot_4.jpg" width="250" title="SS4">
</p>

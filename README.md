# Idea behind PairShare
An app to keep track of (shared) expenses between two people. 
The idea of PairShare is that you can host it on your own, using the (free) plan of google firebase/firestore.
Using the code from the repo you can setup your own google firebase/firestore backend for the app and therefore manage your data on your own.

## Current state:
The app can already be used as is, but there remains some testing for stability and also some planned features are missing yet (see below).
If you want to help me improving the app, just open an issue with bugs you find or features you'd like or create a pull request.

## Installation Instructions (video will follow once project progressed further)
1. clone the project and import it in AndroidStudio (The Android folder)
2. create a new project on https://console.firebase.google.com/u/0/
    1. Create an account if you don't have one yet
    2. Click add project
    3. Choose a nice name (e.g. PairShare). The projectname isn't really that important.
    4. Choose the location where firestore should be used (the closer to your location the better, but the difference for this app isn't highly relevant)
    5. For the other settings you can just skip all checkboxes or choose as you like. 
3. When finished, click on the android symbol to add an android app to your project
4. add the android package name ("com.sliebald.pairshare" if you didn't change it) and click register app
5. Download the google-services.json and copy it to the Android/app directory (as instructed by firebase)
6. You can skip adding the firebase sdk (already done). You can also skip the test step.
7. Setup the firebase authentication: 
    1. Click on Authentication on the left
    2. Click on Sign-in method
    3. Enable Google and Email/password authentication
8. Setup the firestore db (not sure if this is not done automatically, will test and udpdate this step):
    1. Click on Database on the left
    2. Click create Database, enable it in locked mode
    3. Go to Rules, copy the firestore access rules from below in the field (replace the previous) and publish them. This makes sure only people with your version of the app can read/write data
    4. Disclaimer: currently anyone authenticated can read/write any data in theory. For more fine granular access control a better ruleset is needed!
9. Start the app e.g. on an emulator or your phone using androidstudio and login. You need at least two people (or two accounts registered), then you can create new sharing lists and invite one other person to share that list with.

This is the short version, I'll update the instructions and add a video once I added more features (see below) and think the app is in a good state (it already should be usable, but not thouroghly tested and some features are missing).

### Firestore access rules:

```
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth.uid != null;
    }
  }
}
```
  


## Folder structure
### Android:
The android folder contains the source code for the pairshare Android app.

### functions
The functions folder contains firebase functions for serverless serverside (yeah, i know how that sounds :D) cloud functions to support the app.
Currently no functions are used.

## Planned features: 
* Firebase Notifications to inform the other participant when an Expense was added using firebase functions.
  * Implemented, update to installation soon
* Adding an image to a new expense, storing it using firebase storage.



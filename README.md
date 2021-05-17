# Dear FutureMe 🎦 

Your own personal video time capsule!

### Created by San Vu, Catherine Kuntoro, Caitlyn Chau 

📱 CS175 Mobile Device Development Final Project (Group 3)

## Our inspiration
Memories and treasures are things that should last a lifetime. Traditionally, a physical time capsule would be buried with physical goods and information and left for years to be unearthed by future people. In the modern world, a digital time capsule can act as a multimedia scrapbook of digital versions of records and momentos. Today, videos are one of the most common and convenient ways to take a snapshot of history. 

Aced that final? Went to a music festival? Having a bad day and want to cheer your future self up? Record a video documenting your present day experiences and feelings! We hope this app can bring happiness and nostalgia when you unearth your memories. 

## User requirements 
1. Android phone running API 29 with permission to take videos
2. Must be able to log in with SJSU email

## Running the App
Download the APK from this repository **`<add link>`** and drag it to an emulator running API 29. Make sure to log in with your SJSU email or you will not be able to use the app. 

## User Guide
Upon entering the app for the first time, the user is presented with the start screen with a Google sign-in button. Users must sign in using their SJSU emails, otherwise they will not be able to use our app. After logging in, a toast should appear saying the user has successfully logged in and the user will see a loading page briefly before going to the home page. 

The home page serves as a landing page for users to "bury" or "unearth" videos to their time capsule. If the user has not buried any videos yet, they will not be able to unearth any videos and the Unearth button will be grayed out. There is also a logout button for users to sign out of their SJSU account and return back to the home screen. 

Once a user clicks on the Bury button, they will see a dialog asking whether to record a new video or upload an existing video. If the user clicks on record, they will have to click accept permission for our app to use their camera. Then, they can record a video using the android's emulator. It is possible to change the emulator settings to use your computer’s web camera for front-facing video. Users can also upload videos from their own camera roll. After uploading a video, users can type a message that will accompany the video. Afterwards, the users will be able to choose the date and time where they could “unearth”, or view, all of the video and message that they buried. 


When the user returns to the homepage, they will be able to bury more videos and messages or unearth every video and messages, if it is exactly or past the date and time the user has set to unearth the video. Otherwise, they will not be able to access the unearth button, and a message reminding the user of the time and date for unearthing will appear. If the user wants to change the date and time for unearthing their videos and messages, the user will be able to do that through the “Change Time” button in the same homepage.

If it is time to unearth the videos and messages, the user can do so by clicking on the “Unearth” button which leads to a video gallery containing all of the videos the user has uploaded. To view the message that is paired with the video, simply click the “From Past Me” button at the bottom of the screen. To go to the previous or next videos, simply click the back or next button to the left and right of the “From Past Me” button respectively. 

### Frequently Asked Questions
Q: Why can’t I sign in with my non-SJSU email?

A: Due to constraints with our Google Firebase project, we have to limit sign-ins to users within our organization (SJSU) unless we submit our app for Google to review.

Q: What if I want to view my videos sooner?

A: If you would like to view your videos sooner, you can click on the “Change Time” button on the home screen to change to an earlier date and time. 

Q: Can I remove videos from my time capsule?

A: Unfortunately, you cannot remove videos from the time capsule. Just like a physical time capsule, once a relic has been buried, you will not be able to unearth it. However, you may keep burying more videos into the time capsule!

### Links
* [Backlog](https://docs.google.com/spreadsheets/d/14QSBeHhoq-kCaNUsZroGfzXufmJal-hgEQhRGciO-ug/edit#gid=0)
* [APK](https://drive.google.com/file/d/1z_nqSZSdzO2d5wXpqtiwbmBjVS6SeC15/view?usp=sharing)

Add video link here? If u want

# TV Cloud
Inspired from the soundcloud concept but a variant for video content. A project showcasing a second screen integration for social television. A prototype context aware Android application was developed. This is aware of the television show the user is watching. The user can post comments about the show at real time. This was an application developed to study the social aspects of using a second screen device with television.


## Modules
The modules listed here are:
        1. Server: This knows what video is playing and would relay appropriate tags/comments at the tagged second.
        2. Video player: This module is responsible to play the video (TV Screen)
        3. DtoLib: This is a data transfer object library that is used to ease the encapusulate communication between the server and application
        4. Android Application: The main part of the application is the second screen application receiving inputs and pushing it to the server while displaying the tags from the video.

### Anroid Application
The android application has two main functionalities where user can choose to __TAG NOW__ to tag immediately while watching the video or just press __TAG LATER__ to comment on the video at after the playback. The application plays a small excerpt of the video so that the user can remember the comment and enter it.

#### Design
![Alt text](http://s32.postimg.org/gp90tx70l/tvcloud_1.png "TVCloud Home")    ![Alt text](http://s32.postimg.org/cio6el7et/tvcloud_2.png "TAG Later")

The design philosophy is to keeping it extremely simple. The user while being immersed with consuming the content on the television, should not be interrupted by the application. Hence if the user choses to read the comments they're are available on the main view as shown in the left screenshot.

If the user chooses to concentrate on the TV content by tag later, the user would be taken to the screen shown on the right soon after the the video playback is over. To recollect the what the user might have wanted to comment as short excerpt -40s to +40s from the second of the video playback when the __TAG LATER__ was pressed is played back.


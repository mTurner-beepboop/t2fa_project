# Abstract

This is the repository for the level 4 study done on Tangible 2-Factor Authentication, a study put forward 
by Karola Marky, and performed by Mark Turner (2386300T) at the University of Glasgow. The project aims to assess the
usability of 3D printed objects as every-day authentication devices for touchscreens (in this project the
focus is on smart phones running android) by performing a 1 week user study on members of the general population.

# Structure of Repository

Here's an overview of the structure as it stands:

* `timelog.md` Log of how time was spent throughout the project.
* `plan.md` A plan of what needed to be done. 
* `data/` Data aquired during the user study as well as prior research done in the area
* `src/` Source code for the authentication app used in the user study
* `status_report/` The status report submitted in December
* `meetings/` Records of the meetings during the project
* `dissertation/` Source folder for the final project dissertation
* `presentation/` Folder containing files regarding the final presentation for the project
* `models/` Folder containing the digital 3D models that were designed for the project
* `images/` Images of models and development notes created during the project
* `ethics/` Folder containing all related ethical information regarding the user study


## Source code

The source code of the final study app created is located in 'src/t2fa_app/' 
and was created and tested in Android
Studio, as a result, to rebuild the code you can simply import the project
within android studio. No extra modules or packages are required.

The provided code also contains the APK used for the user study, this is located
in 'src/t2fa_app/app/release'. This was again built using Android Studio.

Utility functions written in python can be found in 'src/analysis_scripts' and were run 
through the command line in Python 3.9.0. These scripts will require alteration as they
use absolute paths for file read/write, these ares are pointed out via comments.

## Dissertation

The dissertation has been written using LaTeX with bibTeX.

## Submission

Some folders were omitted from the final submission archive due to size restrictions, this includes the `/images/`, 
`/status_report/`, `/data/`, `/presentation/images/` folders
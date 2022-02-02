## Model

This directory contains the model files for each of those that were 
completed, each within their own folder:

* `Animal - Configuration\` Contains the animal model, which was decided to be too complicated and require too much printing time to take part in the study
* `Circle Coin - Arrangement` Contains the coin model, which was removed from the study due to limitations in android's MotionEvent tracking
* `Credit card - Touch` Contains the credit card model, which made it into the study
* `Cube - Arrangement` Contains the original cube model, which made it into the study
* `Cube Variation - Arrangement` Contains a variant of the cube model, which was made to demonstrate possible methods of footprint placement
* `G-code` Contains the gcode files created from the final models, each of which sliced in PrusaSlicer
* `Pendant - Configuration` Contains the pendant model, which made it into the study
* `Square - Assembly` Contains the square models, which were ejected from th study due to the requirement of multiple parts being too cumbersome

Important to note, the g-code generated has some alterations to the 
models made to ensure that thy interact with the touch screen successfully. 
In particular, the creditcard model was stretched to 150% vertically to 
ensure strength, and the pendant models were stretched 125, 140 and 150% 
as measures of how each interacted with the touch screen, as the default 
size ended up being too small to distinguish the footprint.
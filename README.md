# Hackaton 24.7 by Herberts

### Things done:
* Tested Grid multi-select range with AbstractBackEndDataProvider.
  * Works fine, exception message about having to use `setItemIndexProvider(..)` was helpful.
* Tested new spacing API (`layout.setSpacing("50px")` for example) on Vertical layout 
  * Works nicely.
* Tested new HorizontalLayout slot API (`layout.addToStart(..)`, `layout.addToMiddle(..)`, `layout.addToEnd(..)`)
  * Works nicely.
* While testing multi-select Grid, with a frozen first column, the checkboxes get scrolled away. 
  * There should be an option to have them frozen as well.  
  * Would be nice to have `freezeToStart(..)` API
* Tested the optional pointer focus ring. 
  * Worked well on almost all fields. 
  * Doesn't work on rich text editor.
* While testing rich text editor, noticed it doesn't have a border at the bottom.
  * Can only replicate in some conditions where there is overflow of component under it. 
  * Reported here https://github.com/vaadin/flow-components/issues/7210
* Tested the newly supported min-rows = 1 on TextArea.
  * Worked nicely. 
* Tested the new `accessibleDisabledButtons` feature.
  * Worked nicely.

## Running the application

Open the project in an IDE. You can download the [IntelliJ community edition](https://www.jetbrains.com/idea/download) if you do not have a suitable IDE already.
Once opened in the IDE, locate the `Application` class and run the main method using "Debug".

For more information on installing in various IDEs, see [how to import Vaadin projects to different IDEs](https://vaadin.com/docs/latest/getting-started/import).

If you install the Vaadin plugin for IntelliJ, you should instead launch the `Application` class using "Debug using HotswapAgent" to see updates in the Java code immediately reflected in the browser.



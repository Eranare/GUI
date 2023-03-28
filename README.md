# GUI excercise

GUI app:
- takes in entries from input fields
- checks validity
- if input errors, display error message
- if no input errors, save the data (json?) 
- display data on the bottom text field.


first step, rudimentary gui

add event listener for THE BUTTON, send form action, should call a function to process whatever is in the input fields at the time, then save it accordingly.
potentially output a message in the text field just above THE BUTTON 

List saved data in the proper structured way below THE BUTTON.

ex: 

<APPLICANT NAME0> created at  : <H><M>
<APPLICANT NAME1> created at  : <H><M>
<APPLICANT NAME2> created at  : <H><M>
<APPLICANT NAME3> created at  : <H><M>

Finally.. cosmetics



#GUI 
- Checks if data.JSON exists, if not make new data.JSON file.
- Read and display existing applicant data from the data.JSON file in bottom Pane.
- User inputs; Name(restricted to letters only, field will clear if non alphabetical symbols are placed and field is unfocused), TimeH and TimeM (Also limited to numbers only, will clear if letters or symbols are insterted) *<Time inputs changed from formatted text input to spinners.>
- if invalid, top Pane will display the corresponding error.
- onclick eventlistener Button sets off the methods to validate entries. 
- writeFile verifies the input once more before reading the existing JSON file as an array, add recent input into array and save it as data.JSON again.
- Refreshes the displayed list.

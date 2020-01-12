# TrainStationManagement

This project contains the solution for the travel management project for users, called trainProject.
It contains the main application and its jar which is located under the ApplicationJAR folder (trainProject.jar), to execute it, insert the following command line in the directory where the jar is located: java -jar trainProject.jar <~ path \ testIn.txt> <~ path \ textOut.txt>, passing in the parameters two text files :
- testIn.txt: text file which contains the data to be processed.
- textOut.txt: text file which contains the result of the processing.

The project also contains a main class for the test (TrainStationManagement / trainProject / src / com / project / train / test / TestApp.java) which allows us to test different scenarios that may provoke errors, for that, we created the resources folder which contains different files of inputs containing errors and the log of the output will be saved in a file named test_result.txt under the folder testresult.

This project was carried out without using any framework or library.

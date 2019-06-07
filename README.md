# KEABank
mandatory_android_app


# Deploy
* First download the zip of this repository and unzip it in the desired location on your machine.
* In Android Studio click **File** --> **New** --> **Import Project** --> **Select the unzipped project**
* Finally click Run and select either your desired emulator, or a connected android device.

## Describing the App
* The app has a structure where the Activities act as the view and the controller. The Activies call Service classes, that handle
logic, if any is required. The services call a Repository layer when fetching of data is needed, or when saving data is needed.
* The app uses the SharedPreferences interface as its way of saving data, so there is no need for connection to a database to get the app running.
* The app validates the user's inputs, firstly by doing the normal checks, like checking for invalid email, or password mismatch. Once these
checks are passed, it prompts the user for a NemID validation to be able to sign in(the corresponding key is logged, check MainActivity logcat for key). Other validation  includes sending money between accounts,
ones own or otherwise.
* The app uses the LocationManager and LocationListener classes to get the current location of the user, and assign their branch on sign-up.

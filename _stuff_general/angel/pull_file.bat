@REM agregar al PATH de Windows la carpeta donde está el: "adb.exe"

@SET FILE_PATH=/storage/extSdCard/DCIM/Camera/20140408_020338.jpg
@SET TEMP_FOLDER=.

@adb shell "su -c chmod 7777 '%FILE_PATH%'"
adb.exe pull "%FILE_PATH%" %TEMP_FOLDER%

@REM agregar al PATH de Windows la carpeta donde está el: "adb.exe"
@REM usamos como TEMP_FOLDER la carpeta que tenemos en la conexión del "Navicat for SQLite"

@SET NAVICAT_PATH=C:\Program Files (x86)\PremiumSoft\Navicat for SQLite\navicat.exe
@SET PACKAGE=com.upv.adm.adm_personal_shapes
@SET DB_NAME=personal_shapes.db
@SET TEMP_FOLDER=.

@adb shell "su -c chmod 7777 '/data/data/%PACKAGE%/databases/%DB_NAME%'"

adb.exe pull "/data/data/%PACKAGE%/databases/%DB_NAME%" "%TEMP_FOLDER%"
"%NAVICAT_PATH%"
adb.exe push "%TEMP_FOLDER%\%DB_NAME%" "/data/data/%PACKAGE%/databases/"
del "%TEMP_FOLDER%\%DB_NAME%"
